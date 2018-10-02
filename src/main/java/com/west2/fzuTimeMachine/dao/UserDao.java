package com.west2.fzuTimeMachine.dao;

import com.west2.fzuTimeMachine.model.po.WechatUser;

/**
 * @description: 用户DAO
 * @author: hlx 2018-10-02
 **/
public interface UserDao {

    void save(WechatUser wechatUser);

    WechatUser getByOpenId(String openId);
}
