package com.west2.fzuTimeMachine.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.west2.fzuTimeMachine.config.QiniuConfig;
import com.west2.fzuTimeMachine.dao.TimeCollectionDao;
import com.west2.fzuTimeMachine.dao.TimeDao;
import com.west2.fzuTimeMachine.dao.TimePraiseDao;
import com.west2.fzuTimeMachine.exception.error.ApiException;
import com.west2.fzuTimeMachine.exception.error.TimeErrorEnum;
import com.west2.fzuTimeMachine.model.dto.TimeCheckDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUpdateDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUploadDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUploadBackDTO;
import com.west2.fzuTimeMachine.model.po.Time;
import com.west2.fzuTimeMachine.model.po.TimeCollection;
import com.west2.fzuTimeMachine.model.po.TimePraise;
import com.west2.fzuTimeMachine.model.vo.TimeCollectionVO;
import com.west2.fzuTimeMachine.model.vo.TimeMeVO;
import com.west2.fzuTimeMachine.model.vo.TimeUnCheckVO;
import com.west2.fzuTimeMachine.model.vo.TimeUploadVO;
import com.west2.fzuTimeMachine.service.TimeService;
import com.west2.fzuTimeMachine.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    private TimeDao timeDao;

    private TimePraiseDao timePraiseDao;

    private TimeCollectionDao timeCollectionDao;

    @Autowired
    public TimeServiceImpl(QiniuConfig qiniuConfig, ObjectMapper jsonMapper,
                           ModelMapper modelMapper, TimeDao timeDao, TimePraiseDao timePraiseDao, TimeCollectionDao timeCollectionDao) {
        this.qiniuConfig = qiniuConfig;
        this.jsonMapper = jsonMapper;
        this.modelMapper = modelMapper;
        this.timeDao = timeDao;
        this.timePraiseDao = timePraiseDao;
        this.timeCollectionDao = timeCollectionDao;
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
    public void update(TimeUpdateDTO timeUpdateDTO) {
        Time time = timeDao.get(timeUpdateDTO.getTimeId());
        if (time != null) {
            timeDao.update(modelMapper.map(timeUpdateDTO, Time.class));
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
        TimePraise timePraise = timePraiseDao.getByUserId(userId);
        //如果不存在此记录，表示用户没点过赞，添加记录并且文章点赞数+1
        if (timePraise == null) {
            Time time = timeDao.get(timeId);
            int praiseNum = time.getPraiseNum() + 1;
            timeDao.updatePraise(timeId, praiseNum);
            TimePraise praise = new TimePraise();
            praise.setTimeId(timeId);
            praise.setUserId(userId);
            Long now = System.currentTimeMillis();
            praise.setCreateTime(now);
            timePraiseDao.save(praise);
            log.info("+1");
        } else {
            //如果存在此纪录,表示用户点过赞，这次是取消点赞，删除记录并且文章点赞数-1
            timePraiseDao.deleteByUserId(timePraise.getUserId());
            Time time = timeDao.get(timeId);
            int praiseNum = time.getPraiseNum() - 1;
            timeDao.updatePraise(timeId, praiseNum);
            log.info("-1");
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
        if (timeCollection == null) {
            TimeCollection collection = new TimeCollection();
            collection.setTimeId(timeId);
            collection.setUserId(userId);
            Long now = System.currentTimeMillis();
            collection.setCreateTime(now);
            timeCollectionDao.save(collection);
        } else {
            throw new ApiException(TimeErrorEnum.COLLECTION_EXIST);
        }

    }

    @Override
    public void unCollect(Integer id, Integer userId) {
        TimeCollection timeCollection = timeCollectionDao.get(id);
        if (timeCollection == null) {
            throw new ApiException(TimeErrorEnum.COLLECTION_NOT_FOUND);
        } else if (!timeCollection.getUserId().equals(userId)) {
            throw new ApiException(TimeErrorEnum.COLLECTION_NOT_ME);
        } else {
            timeCollectionDao.delete(id);
        }

    }

    @Override
    public List<TimeCollectionVO> getCollection(Integer userId) {
        List<TimeCollection> timeCollectionList = timeCollectionDao.getByUserId(userId);
        List<TimeCollectionVO> timeCollectionVOS = new ArrayList<>();
        for (TimeCollection timeCollection : timeCollectionList) {
            TimeCollectionVO timeCollectionVO = modelMapper.map(timeCollection, TimeCollectionVO.class);
            Time time = timeDao.get(timeCollection.getTimeId());
            timeCollectionVO.setImgUrl(time.getImgUrl());
            timeCollectionVO.setContent(time.getContent());
            timeCollectionVO.setTitle(time.getTitle());
            timeCollectionVOS.add(timeCollectionVO);
        }
        return timeCollectionVOS;
    }


}
