package com.west2.fzuTimeMachine.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 时光未审核VO
 * @author: hlx 2018-10-12
 **/
@Data
public class TimeUnCheckVO implements Serializable{

    private Integer id;

    private String title;

    private String imgUrl;

    private String content;

    private String label;

    public TimeUnCheckVO() {
    }

}
