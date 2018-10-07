package com.west2.fzuTimeMachine.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.west2.fzuTimeMachine.config.QiniuConfig;
import com.west2.fzuTimeMachine.dao.TimeDao;
import com.west2.fzuTimeMachine.exception.error.ApiException;
import com.west2.fzuTimeMachine.exception.error.TimeErrorEnum;
import com.west2.fzuTimeMachine.model.dto.TimeUpdateDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUploadDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUploadBackDTO;
import com.west2.fzuTimeMachine.model.po.Time;
import com.west2.fzuTimeMachine.model.vo.TimeMeVO;
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

    @Autowired
    public TimeServiceImpl(QiniuConfig qiniuConfig, ObjectMapper jsonMapper,
                           ModelMapper modelMapper, TimeDao timeDao) {
        this.qiniuConfig = qiniuConfig;
        this.jsonMapper = jsonMapper;
        this.modelMapper = modelMapper;
        this.timeDao = timeDao;
    }

    @Override
    public TimeUploadVO upload(TimeUploadDTO timeUploadDTO, Integer userId) {
        String key = String.valueOf(UUID.randomUUID()).replace("-","");
        log.info("key->>" + key);

        // 保存Time->>
        Time time = modelMapper.map(timeUploadDTO, Time.class);
        // -1为图片尚未上传,上传成功后设置为0
        time.setCheckStatus(-1);
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
            // 更新时光状态为待审核0
            timeDao.updateStatus(Integer.valueOf(timeUploadBackDTO.getId()),0);
        }else{
            throw new ApiException(TimeErrorEnum.BACK_INVALID);
        }
    }
    @Override
    public void update(TimeUpdateDTO timeUpdateDTO){
        Time time = timeDao.get(timeUpdateDTO.getTimeId());
        if(time != null){
            timeDao.update(modelMapper.map(timeUpdateDTO,Time.class));
        }else{
            throw new ApiException(TimeErrorEnum.NOT_FOUND);
        }
    }

    @Override
    public void delete(Integer timeId) {
        Time time = timeDao.get(timeId);
        if (time != null) {
            timeDao.delete(timeId);
        }else{
            throw new ApiException(TimeErrorEnum.NOT_FOUND);
        }
    }

    @Override
    public List<TimeMeVO> getMe(Integer userId) {
        List<Time> timeList = timeDao.getByUserId(userId);
        List<TimeMeVO> timeMeVOList = new ArrayList<>();
        for (Time time : timeList) {
            TimeMeVO timeMeVO = modelMapper.map(time, TimeMeVO.class);
            timeMeVOList.add(timeMeVO);
        }
        return timeMeVOList;
    }
}
