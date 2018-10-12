package com.west2.fzuTimeMachine.model.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 时光
 * @author: hlx 2018-10-03
 **/
@Data
public class Time implements Serializable{

    private Integer id;

    // 发布者id
    private Integer userId;

    private String title;

    private String imgUrl;

    private String content;

    private Double longitude;

    private Double latitude;

    private Long createTime;

    private Long updateTime;

    private String label;

    private Integer praiseNum;

    /**
     * 审核状态列表:
     * 0代表未审核
     * 1代表审核成功
     */
    private Byte checkStatus;

    private Byte visible;

    public Time() {
    }

}
