package com.west2.fzuTimeMachine.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @description: 时光上传DTO
 * @author: hlx 2018-10-03
 **/
@Data
@ApiModel(description = "时光上传传输对象")
public class TimeUploadDTO implements Serializable{

    @Size(min = 1,max = 16)
    @ApiModelProperty(required = true, name = "标题", example = "那个食堂")
    private String title;

    @Size(min = 1,max = 1024)
    @ApiModelProperty(required = true, name = "内容", example = "真好吃!")
    private String content;

    // 当用户拒绝获取地理位置时为空
    @ApiModelProperty(name = "经度", example = "119.3")
    private Double longitude;

    @ApiModelProperty(name = "纬度", example = "26.08")
    private Double latitude;

    @Size(max = 255)
    @ApiModelProperty(notes = "标签", example = "食堂+美食")
    private String label;

    public TimeUploadDTO() {

    }

}
