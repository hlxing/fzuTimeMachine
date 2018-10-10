package com.west2.fzuTimeMachine.model.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 时光点赞
 * @author: hlx 2018-10-10
 **/
@Data
public class TimePraise implements Serializable{

    private Integer id;

    private Integer userId;

    private Long createTime;

    public TimePraise() {
    }
}
