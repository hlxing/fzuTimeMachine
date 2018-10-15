package com.west2.fzuTimeMachine.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 用户管理员登录传输对象
 * @author: hlx 2018-10-13
 **/
@Data
@ApiModel(description = "管理员登录传输对象")
public class UserAdminLoginDTO implements Serializable{

    private String name;

    private String pass;

    public UserAdminLoginDTO() {
    }
}
