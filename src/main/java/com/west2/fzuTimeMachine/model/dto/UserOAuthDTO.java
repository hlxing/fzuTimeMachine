package com.west2.fzuTimeMachine.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @description: 用户授权回调的传输对象
 * @author: hlx 2018-10-01
 **/
@Data
@ApiModel(description = "授权注册传输对象")
public class UserOAuthDTO implements Serializable{

    // 短期凭证
    @Size(min = 1)
    @ApiModelProperty(required = true, name = "短期凭证code", example = "061xAqJa2YfIcP0KQZHa29pvJa2xAqJK")
    private String code;

    // 用户加密数据
    @Size(min = 1)
    @ApiModelProperty(required = true, name = "用户加密数据", example = "adf5e654ef123")
    private String encryptedData;

    // iv字符串
    @Size(min = 1)
    @ApiModelProperty(required = true, name = "iv字符串", example = "ZNt76BcMNcyiZ3q1T62w/Q==")
    private String ivStr;

    public UserOAuthDTO() {
    }
}
