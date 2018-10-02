package com.west2.fzuTimeMachine.model.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: access_token
 * @author: hlx 2018-09-19
 **/
@Data
public class UserAccessToken implements Serializable{

    private String access_token;

    private Integer expires_in;

    private String refresh_token;

    private String openid;

    private String scope;

    public UserAccessToken() {
    }

}
