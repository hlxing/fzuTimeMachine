package com.west2.fzuTimeMachine.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 用户视图
 * @author: hlx 2018-10-20
 **/
@Data
public class UserVO implements Serializable {

    private Integer userId;

    private String nickName;

    private String avatarUrl;
}
