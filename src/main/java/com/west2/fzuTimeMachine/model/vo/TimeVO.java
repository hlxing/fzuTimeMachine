package com.west2.fzuTimeMachine.model.vo;

import lombok.Data;

/**
 * @description: 探索时光视图对象
 * @author: yyf 2018-10-16
 **/
@Data
public class TimeVO {

    private Integer id;

    private Integer userId;

    private String imgUrl;

    private String nickName;

    private String avatarUrl;

    private String content;

    private String location;

    private Long updateTime;

    private Integer praiseNum;

    private Byte isPraise;

    public TimeVO() {
    }

}