package com.west2.fzuTimeMachine.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 微信错误,当微信接口访问异常时抛出
 * @author: hlx 2018-09-30
 **/
@Data
public class WechatError implements Serializable{

    @JsonProperty("errcode")
    private Integer errcode;

    @JsonProperty("errmsg")
    private String errmsg;

    public WechatError() {
    }
}