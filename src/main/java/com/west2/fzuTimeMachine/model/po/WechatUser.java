package com.west2.fzuTimeMachine.model.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @description: 微信用户
 * @author: hlx 2018-10-01
 **/
@Data
// 忽略垃圾字段!
@JsonIgnoreProperties(ignoreUnknown = true)
public class WechatUser implements Serializable{

    @JsonProperty("country")
    private String country;

    @JsonProperty("gender")
    private Integer gender;

    @JsonProperty("province")
    private String province;

    @JsonProperty("city")
    private String city;

    @JsonProperty("avatarUrl")
    private String avatarUrl;

    @JsonProperty("openId")
    private String openId;

    @JsonProperty("nickName")
    private String nickName;

    @JsonProperty("language")
    private String language;

    @JsonProperty("createTime")
    private Long createTime;

    public WechatUser() {
    }
}