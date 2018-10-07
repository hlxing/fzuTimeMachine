package com.west2.fzuTimeMachine.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 我的时光视图对象
 * @author: hlx 2018-10-07
 **/
@Data
public class TimeMeVO implements Serializable{

    private String title;

    private String imgUrl;

    private String content;

    private Long createTime;

    private Long updateTime;

    private String label;

    private Integer praiseNum;

    public TimeMeVO() {
    }
}
