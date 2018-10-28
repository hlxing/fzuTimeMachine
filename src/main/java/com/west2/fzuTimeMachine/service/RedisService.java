package com.west2.fzuTimeMachine.service;

/**
 * @description: redis 服务接口
 * @author: hlx 2018-10-27
 **/
public interface RedisService {

    boolean tryLock(String key, String value);

    void unLock(String key, String value);

}
