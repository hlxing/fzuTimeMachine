package com.west2.fzuTimeMachine.dao;

import com.west2.fzuTimeMachine.model.po.WechatUser;
import com.west2.fzuTimeMachine.model.vo.UserVO;
import org.apache.ibatis.annotations.Param;

/**
 * @description: 用户DAO
 * @author: hlx 2018-10-02
 **/
public interface UserDao {

    void save(WechatUser wechatUser);

    WechatUser getByOpenId(String openId);

    WechatUser get(Integer id);

    String getSessionIdByUserId(Integer userId);

    void updateSessionIdByUserId(@Param("userId") Integer userId, @Param("sessionId") String sessionId);

    WechatUser getInfo(Integer userId);

}
