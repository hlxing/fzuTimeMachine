package com.west2.fzuTimeMachine.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 时光排行VO
 * @author: hlx 2018-10-15
 **/
@Data
public class TimeRankVO implements Serializable {

    // 头像Url
    private String avatarUrl;

    // 昵称
    private String nickName;

    private Integer praiseNum;

    private String imgUrl;

    private Integer id;

    public TimeRankVO() {
    }

}
