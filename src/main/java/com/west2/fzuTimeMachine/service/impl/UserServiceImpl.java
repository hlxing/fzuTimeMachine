package com.west2.fzuTimeMachine.service.impl;

import com.west2.fzuTimeMachine.dao.UserDao;
import com.west2.fzuTimeMachine.exception.error.ApiException;
import com.west2.fzuTimeMachine.exception.error.UserErrorEnum;
import com.west2.fzuTimeMachine.model.dto.UserAdminLoginDTO;
import com.west2.fzuTimeMachine.model.dto.UserOAuthDTO;
import com.west2.fzuTimeMachine.model.po.Jscode2session;
import com.west2.fzuTimeMachine.model.po.WechatUser;
import com.west2.fzuTimeMachine.service.UserService;
import com.west2.fzuTimeMachine.util.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @description: 用户服务实现类
 * @author: hlx 2018-10-02
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void oauth(UserOAuthDTO userOAuthDTO, HttpServletRequest request) {
        log.info("userOAuthDTO->>" + userOAuthDTO);
        Jscode2session jscode2session = WechatUtil.getJscode2session(userOAuthDTO.getCode());
        if (null == jscode2session) {
            throw new ApiException(UserErrorEnum.CODE_INVALID);
        }
        WechatUser test = userDao.getByOpenId(jscode2session.getOpenid());
        if (test != null) {
            throw new ApiException(UserErrorEnum.EXIST);
        }
        log.info("session->>" + jscode2session);
        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute("sessionKey", jscode2session.getSessionKey());

        WechatUser wechatUser = WechatUtil.decryptUser(jscode2session.getSessionKey(), userOAuthDTO.getEncryptedData(), userOAuthDTO.getIvStr());
        wechatUser.setCreateTime(System.currentTimeMillis() / 1000);
        userDao.save(wechatUser);
        httpSession.setAttribute("userId", wechatUser.getUserId());
        log.info("wechatUser->>" + wechatUser);
    }

    @Override
    public void login(String code, HttpServletRequest request) {
        log.info("code->>" + code);
        Jscode2session jscode2session = WechatUtil.getJscode2session(code);
        if (null == jscode2session) {
            throw new ApiException(UserErrorEnum.CODE_INVALID);
        }
        WechatUser wechatUser = userDao.getByOpenId(jscode2session.getOpenid());
        if (null == wechatUser) {
            throw new ApiException(UserErrorEnum.OAUTH_NOT_FOUND);
        }
        log.info("session->>" + jscode2session);
        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute("userId", wechatUser.getUserId());
        httpSession.setAttribute("sessionKey", jscode2session.getSessionKey());
    }

    @Override
    public void adminLogin(UserAdminLoginDTO userAdminLoginDTO, HttpServletRequest request) {
        if (userAdminLoginDTO.getName().equals("w2") && userAdminLoginDTO.getPass().equals("10086")) {
            request.getSession(true);
        } else {
            throw new ApiException(UserErrorEnum.PASS_INVALID);
        }
    }


}
