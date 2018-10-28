package com.west2.fzuTimeMachine.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.west2.fzuTimeMachine.config.QiniuConfig;
import com.west2.fzuTimeMachine.dao.TimeCollectionDao;
import com.west2.fzuTimeMachine.dao.TimeDao;
import com.west2.fzuTimeMachine.dao.TimePraiseDao;
import com.west2.fzuTimeMachine.dao.UserDao;
import com.west2.fzuTimeMachine.exception.error.ApiException;
import com.west2.fzuTimeMachine.exception.error.TimeErrorEnum;
import com.west2.fzuTimeMachine.model.dto.TimeCheckDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUpdateDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUploadDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUploadBackDTO;
import com.west2.fzuTimeMachine.model.po.Time;
import com.west2.fzuTimeMachine.model.po.TimeCollection;
import com.west2.fzuTimeMachine.model.po.TimePraise;
import com.west2.fzuTimeMachine.model.po.WechatUser;
import com.west2.fzuTimeMachine.model.vo.*;
import com.west2.fzuTimeMachine.service.TimeService;
import com.west2.fzuTimeMachine.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @description: 时光服务实现
 * @author: hlx 2018-10-03
 **/
@Slf4j
@Service
public class TimeServiceImpl implements TimeService {

    private QiniuConfig qiniuConfig;

    private ObjectMapper jsonMapper;

    private ModelMapper modelMapper;

    private static final String RANK_KEY = "rank";

    private TimeDao timeDao;

    private TimePraiseDao timePraiseDao;

    private TimeCollectionDao timeCollectionDao;
    private UserDao userDao;
    private RedisTemplate redisTemplate;

    @Autowired
    public TimeServiceImpl(QiniuConfig qiniuConfig, ObjectMapper jsonMapper, ModelMapper modelMapper,
                           TimeDao timeDao, TimePraiseDao timePraiseDao, TimeCollectionDao timeCollectionDao,
                           RedisTemplate redisTemplate, UserDao userDao) {
        this.qiniuConfig = qiniuConfig;
        this.jsonMapper = jsonMapper;
        this.modelMapper = modelMapper;
        this.timeDao = timeDao;
        this.timePraiseDao = timePraiseDao;
        this.timeCollectionDao = timeCollectionDao;
        this.redisTemplate = redisTemplate;
        this.userDao = userDao;
    }

    @Override
    public TimeUploadVO upload(TimeUploadDTO timeUploadDTO, Integer userId) {
        String key = String.valueOf(UUID.randomUUID()).replace("-", "");
        log.info("key->>" + key);

        // 保存Time->>
        Time time = modelMapper.map(timeUploadDTO, Time.class);
        // 图片尚未上传成功,不可见,且未审核
        time.setVisible((byte) 0);
        time.setCheckStatus((byte) 0);
        // 图片地址为:七牛云域名+key
        time.setImgUrl(qiniuConfig.getCloudUrl() + key);
        time.setUserId(userId);
        Long now = System.currentTimeMillis();
        time.setCreateTime(now);
        time.setUpdateTime(now);
        time.setPraiseNum(0);
        time.setLocation(timeUploadDTO.getLocation());
        timeDao.save(time);


        // 返回上传凭证->>
        // AES加密timeId,后续用来校验回调消息的合法性
        String timeId = String.valueOf(time.getId());
        String encryptedId = AESUtil.encrypt(timeId, qiniuConfig.getBackSecretKey());
        TimeUploadBackDTO timeUploadBackDTO = new TimeUploadBackDTO(timeId, encryptedId);
        try {
            String uploadBackDTOJson = jsonMapper.writeValueAsString(timeUploadBackDTO);
            // 添加回调数据
            qiniuConfig.getStringMap()
                    .put("callbackBody", uploadBackDTOJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        TimeUploadVO timeUploadVO = new TimeUploadVO();
        timeUploadVO.setId(timeId);
        timeUploadVO.setKey(key);
        timeUploadVO.setUploadToken(qiniuConfig.createToken(key));
        return timeUploadVO;
    }

    @Override
    public void uploadBack(TimeUploadBackDTO timeUploadBackDTO) {
        // 校验回调消息是否正确
        String rightKey = AESUtil.decrypt(timeUploadBackDTO.getEncryptedId(), qiniuConfig.getBackSecretKey());
        if (rightKey != null && timeUploadBackDTO.getId().equals(rightKey)) {
            // 时光变为可见状态
            timeDao.updateVisible(Integer.valueOf(timeUploadBackDTO.getId()), (byte) 1);
        } else {
            throw new ApiException(TimeErrorEnum.BACK_INVALID);
        }
    }

    @Override
    public void update(TimeUpdateDTO timeUpdateDTO, Integer userId) {
        Time time = timeDao.get(timeUpdateDTO.getTimeId());
        if (time != null) {
            if (time.getUserId().equals(userId))
                timeDao.update(modelMapper.map(timeUpdateDTO, Time.class));
            else
                throw new ApiException(TimeErrorEnum.NOT_ME);
        } else {
            throw new ApiException(TimeErrorEnum.NOT_FOUND);
        }
    }

    @Override
    public void delete(Integer timeId, Integer userId) {
        Time time = timeDao.get(timeId);
        if (time == null) {
            throw new ApiException(TimeErrorEnum.NOT_FOUND);
        } else if (!time.getUserId().equals(userId)) {
            throw new ApiException(TimeErrorEnum.NOT_ME);
        } else {
            timeDao.updateVisible(timeId, (byte) 0);
        }
    }

    @Override
    public List<TimeMeVO> getMe(Integer userId) {
        List<Time> timeList = timeDao.getByUserId(userId);
        List<TimeMeVO> timeMeVOList = new ArrayList<>();
        timeList.forEach((time) -> timeMeVOList.add(modelMapper.map(time, TimeMeVO.class)));
        return timeMeVOList;
    }

    @Override
    public void praise(Integer timeId, Integer userId) {
        TimePraise timePraise = timePraiseDao.getByUserIdAndTimeId(userId, timeId);
        Time time = timeDao.get(timeId);
        if (time == null) {
            throw new ApiException(TimeErrorEnum.NOT_FOUND);
        }
        //如果不存在此记录，表示用户没点过赞，添加记录并且文章点赞数+1
        if (timePraise == null) {
            int praiseNum = time.getPraiseNum() + 1;
            timeDao.updatePraise(timeId, praiseNum);
            TimePraise praise = new TimePraise();
            praise.setTimeId(timeId);
            praise.setUserId(userId);
            Long now = System.currentTimeMillis();
            praise.setCreateTime(now);
            timePraiseDao.save(praise);
        } else {
            //如果存在此纪录,表示用户点过赞，这次是取消点赞，删除记录并且文章点赞数-1
            timePraiseDao.deleteByUserIdAndTimeId(timePraise.getUserId(), timeId);
            int praiseNum = time.getPraiseNum() - 1;
            timeDao.updatePraise(timeId, praiseNum);
        }
    }

    @Override
    public void check(TimeCheckDTO timeCheckDTO) {
        Integer timeId = timeCheckDTO.getTimeId();
        Time time = timeDao.get(timeId);
        if (time == null) {
            throw new ApiException(TimeErrorEnum.NOT_FOUND);
        } else {
            time.setCheckStatus((byte) 1);
            if (timeCheckDTO.getStatus().equals(0)) {
                time.setVisible((byte) 1);
            } else {
                time.setVisible((byte) 0);
            }
            timeDao.updateStatusAndVisible(time);
        }
    }

    @Override
    public List<TimeUnCheckVO> getUnCheck() {
        List<Time> timeList = timeDao.getByUncheck();
        List<TimeUnCheckVO> timeUnCheckVOList = new ArrayList<>();
        timeList.forEach((time) -> timeUnCheckVOList.add(modelMapper.map(time, TimeUnCheckVO.class)));
        return timeUnCheckVOList;
    }

    @Override
    public void Collect(Integer timeId, Integer userId) {
        TimeCollection timeCollection = timeCollectionDao.getByTimeIdAndUserId(timeId, userId);
        Time time = timeDao.get(timeId);
        if (time == null) {
            throw new ApiException(TimeErrorEnum.NOT_FOUND);
        }
        if (timeCollection == null) {
            TimeCollection collection = new TimeCollection();
            collection.setTimeId(timeId);
            collection.setUserId(userId);
            Long now = System.currentTimeMillis();
            collection.setCreateTime(now);
            timeCollectionDao.save(collection);
        } else {
            //如果存在此纪录,表示用户收藏，这次是取消收藏
            timeCollectionDao.deleteByUserIdAndTimeId(userId, timeId);
        }

    }

    @Override
    public List<TimeCollectionVO> getCollection(Integer userId) {
        List<TimeCollection> timeCollectionList = timeCollectionDao.getByUserId(userId);
        List<TimeCollectionVO> timeCollectionVOS = new ArrayList<>();
        for (TimeCollection timeCollection : timeCollectionList) {
            TimeCollectionVO timeCollectionVO = modelMapper.map(timeCollection, TimeCollectionVO.class);
            Time time = timeDao.get(timeCollection.getTimeId());
            WechatUser wechatUser = userDao.get(time.getUserId());
            timeCollectionVO.setImgUrl(time.getImgUrl());
            timeCollectionVO.setUpdateTime(time.getUpdateTime());
            timeCollectionVO.setAvatarUrl(wechatUser.getAvatarUrl());
            timeCollectionVO.setNickName(wechatUser.getNickName());
            timeCollectionVOS.add(timeCollectionVO);
        }
        return timeCollectionVOS;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TimeRankVO> getRank() {
        return redisTemplate.opsForList().range(RANK_KEY, 0, -1);
    }

    @Override
    public TimeVO explore(Integer userId) {
        List<Integer> timeList = timeDao.getAllIdByVisible(1);
        int len = timeList.size();
        int index = (int) ((System.currentTimeMillis()) % len);
        int timeId = timeList.get(index);
        Time time = timeDao.get(timeId);
        TimePraise timePraise = timePraiseDao.getByUserIdAndTimeId(userId, timeId);
        TimeCollection timeCollection = timeCollectionDao.getByTimeIdAndUserId(timeId, userId);
        WechatUser wechatUser = userDao.get(time.getUserId());
        TimeVO timeVO = modelMapper.map(time, TimeVO.class);
        timeVO.setAvatarUrl(wechatUser.getAvatarUrl());
        timeVO.setNickName(wechatUser.getNickName());
        if (timePraise == null) {
            timeVO.setIsPraise((byte) 0);
        } else {
            timeVO.setIsPraise((byte) 1);
        }
        if (timeCollection == null) {
            timeVO.setIsCollect((byte) 0);
        } else {
            timeVO.setIsCollect((byte) 1);
        }
        return timeVO;
    }

    @Override
    public TimeVO get(Integer timeId, Integer userId) {
        Time time = timeDao.get(timeId);
        if (time == null) {
            throw new ApiException(TimeErrorEnum.NOT_FOUND);
        }
        TimeVO timeVO = modelMapper.map(time, TimeVO.class);
        WechatUser wechatUser = userDao.get(time.getUserId());
        TimePraise timePraise = timePraiseDao.getByUserIdAndTimeId(userId, timeId);
        TimeCollection timeCollection = timeCollectionDao.getByTimeIdAndUserId(timeId, userId);
        timeVO.setAvatarUrl(wechatUser.getAvatarUrl());
        timeVO.setNickName(wechatUser.getNickName());
        if (timePraise == null) {
            timeVO.setIsPraise((byte) 0);
        } else {
            timeVO.setIsPraise((byte) 1);
        }
        if (timeCollection == null) {
            timeVO.setIsCollect((byte) 0);
        } else {
            timeVO.setIsCollect((byte) 1);
        }
        return timeVO;
    }

    @Override
    public List<TimeMapVO> getMap() {
        PageHelper.startPage(1, 100);
        List<Time> timeList = timeDao.getMap();
        List<TimeMapVO> timeMapVOS = new ArrayList<>();
        timeList.forEach((time) -> {
            TimeMapVO timeMapVO = modelMapper.map(time, TimeMapVO.class);
            timeMapVOS.add(timeMapVO);
        });
        return timeMapVOS;
    }
}
