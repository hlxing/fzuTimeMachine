package com.west2.fzuTimeMachine.model.vo;

import lombok.Data;

/**
 * @description: 探索时光视图对象
 * @author: yyf 2018-10-16
 **/
@Data
public class TimeVO {
    private Integer timeId;

    private String title;

    private Integer userId;

    private String imgUrl;

    private String content;

    private Long createTime;

    private Long updateTime;

    private String label;

    private Integer praiseNum;

    public TimeVO() {
    }
}
