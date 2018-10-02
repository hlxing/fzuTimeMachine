package com.west2.fzuTimeMachine.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 测试控制层, 用于测试服务器正常响应, 登录权限等
 * @author: hlx 2018-10-01
 **/
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 服务器连接测试
     */
    @GetMapping("/connect")
    public String connect() {
        return "connect check success";
    }

    /**
     * 登录测试
     */
    @GetMapping("/auth")
    public String auth() {
        return "auth check success";
    }

    /**
     * admin权限测试
     */
    @RequiresRoles("admin")
    @GetMapping("/role")
    public String admin2() {
        return "admin check success";
    }

}
