package com.west2.fzuTimeMachine.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 上传响应视图
 * @author: hlx 2018-10-03
 **/
@Data
public class UploadVO implements Serializable{

    private String key;

    private String uploadToken;

    public UploadVO() {
    }
}
