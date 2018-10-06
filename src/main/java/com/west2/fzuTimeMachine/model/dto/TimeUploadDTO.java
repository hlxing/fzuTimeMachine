package com.west2.fzuTimeMachine.model.dto;

import lombok.Data;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @description: 时光上传DTO
 * @author: hlx 2018-10-03
 **/
@Data
public class TimeUploadDTO implements Serializable{

    @Size(min = 1,max = 16)
    private String title;

    @Size(min = 1,max = 1024)
    private String content;

    // 当用户拒绝获取地理位置时为空
    private Double longitude;

    private Double latitude;

    @Size(max = 255)
    private String label;

    public TimeUploadDTO() {

    }

}
