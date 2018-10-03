package com.west2.fzuTimeMachine.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.west2.fzuTimeMachine.config.QiniuConfig;
import com.west2.fzuTimeMachine.dao.TimeDao;
import com.west2.fzuTimeMachine.exception.error.ApiException;
import com.west2.fzuTimeMachine.exception.error.TimeErrorEnum;
import com.west2.fzuTimeMachine.model.dto.UploadBackDTO;
import com.west2.fzuTimeMachine.model.dto.UploadDTO;
import com.west2.fzuTimeMachine.model.po.Time;
import com.west2.fzuTimeMachine.model.vo.UploadVO;
import com.west2.fzuTimeMachine.service.TimeService;
import com.west2.fzuTimeMachine.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public UploadVO upload(UploadDTO uploadDTO, Integer userId) {
        String key = String.valueOf(UUID.randomUUID()).replace("-","");
        log.info("key->>" + key);

        // 保存Time->>
        Time time = modelMapper.map(uploadDTO, Time.class);
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
        UploadBackDTO uploadBackDTO = new UploadBackDTO(timeId, encryptedId);
        try {
            String uploadBackDTOJson = jsonMapper.writeValueAsString(uploadBackDTO);
            // 添加回调数据
            qiniuConfig.getStringMap()
                    .put("callbackBody", uploadBackDTOJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        UploadVO uploadVO = new UploadVO();
        uploadVO.setKey(key);
        uploadVO.setUploadToken(qiniuConfig.createToken(key));
        return uploadVO;
    }

    @Override
    public void uploadBack(UploadBackDTO uploadBackDTO) {
        // 校验回调消息是否正确
        String rightKey = AESUtil.decrypt(uploadBackDTO.getEncryptedId(), qiniuConfig.getBackSecretKey());
        if (rightKey != null && uploadBackDTO.getId().equals(rightKey)) {
            // 更新时光状态为待审核0
            timeDao.updateStatus(Integer.valueOf(uploadBackDTO.getId()),0);
        }else{
            throw new ApiException(TimeErrorEnum.BACK_INVALID);
        }
    }

}
