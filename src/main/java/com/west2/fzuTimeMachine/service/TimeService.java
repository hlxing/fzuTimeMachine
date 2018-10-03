package com.west2.fzuTimeMachine.service;

import com.west2.fzuTimeMachine.model.dto.UploadBackDTO;
import com.west2.fzuTimeMachine.model.dto.UploadDTO;
import com.west2.fzuTimeMachine.model.vo.UploadVO;


/**
 * @description: 时光服务接口
 * @author: hlx 2018-10-03
 **/
public interface TimeService {

    /**
     * 时光上传
     * @param uploadDTO 上传传输对象
     * @param userId 用户id
     * @return 上传响应视图对象,包括key和图片上传凭证uploadToken
     */
    UploadVO upload(UploadDTO uploadDTO, Integer userId);

    /**
     * 时光图片上传回调,用于校验图片是否成功上传
     * @param uploadBackDTO 上传回调传输对象
     */
    void uploadBack(UploadBackDTO uploadBackDTO);

}
