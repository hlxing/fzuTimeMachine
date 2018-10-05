package com.west2.fzuTimeMachine.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 时光上传DTO
 * @author: hlx 2018-10-03
 **/
@Data
public class UploadDTO implements Serializable{

    private String title;

    private String content;

    private Double longitude;

    private Double latitude;

    private String label;

    public UploadDTO() {

    }

}
