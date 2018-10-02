package com.west2.fzuTimeMachine.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 微信临时登录凭证code换取的长期凭证
 * @author: hlx 2018-09-26
 **/
@Data
public class Jscode2session implements Serializable{

    @JsonProperty("unionid")
    private String unionid;

    @JsonProperty("openid")
    private String openid;

    @JsonProperty("session_key")
    private String sessionKey;

}