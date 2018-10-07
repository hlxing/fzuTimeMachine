package com.west2.fzuTimeMachine.model.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 时光收藏
 * @author: hlx 2018-10-04
 **/
@Data
public class TimeCollection implements Serializable{

    private Integer id;

    private Integer userId;

    private Integer timeId;

    private Long createTime;

    public TimeCollection() {
    }

}
