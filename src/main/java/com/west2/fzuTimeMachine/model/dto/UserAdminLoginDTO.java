package com.west2.fzuTimeMachine.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description: 用户管理员登录传输对象
 * @author: hlx 2018-10-13
 **/
@Data
@ApiModel(description = "管理员登录传输对象")
public class UserAdminLoginDTO implements Serializable{

    @NotNull
    private String name;

    @NotNull
    private String pass;

    public UserAdminLoginDTO() {
    }
}
