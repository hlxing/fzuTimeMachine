package com.west2.fzuTimeMachine.service;

import com.west2.fzuTimeMachine.model.dto.UserAdminLoginDTO;
import com.west2.fzuTimeMachine.model.dto.UserOAuthDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 用户服务接口
 * @author: hlx 2018-10-02
 **/
public interface UserService {

    /**
     * 微信授权注册,获取用户信息并入库
     * @param userOAuthDTO 授权传输对象
     * @param request httpServletRequest,创建HttpSession
     */
    void oauth(UserOAuthDTO userOAuthDTO, HttpServletRequest request);


    /**
     * 微信登录
     * @param code 通过wx.login获取的短期凭证
     * @param request httpServletRequest,创建HttpSession
     */
    void login(String code, HttpServletRequest request);

    /**
     * 管理员登录
     * @param userAdminLoginDTO 管理员登录传输对象
     * @param request 请求
     */
    void adminLogin(UserAdminLoginDTO userAdminLoginDTO, HttpServletRequest request);
}
