package com.west2.fzuTimeMachine.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description: 时光上传回调的传输对象
 * @author: hlx 2018-10-03
 **/
@Data
public class TimeUploadBackDTO implements Serializable{

    @NotNull
    private String id;

    @NotNull
    private String encryptedId;

    public TimeUploadBackDTO() {
    }

    public TimeUploadBackDTO(String id, String encryptedId) {
        this.id = id;
        this.encryptedId = encryptedId;
    }
}
