package com.west2.fzuTimeMachine.service;

/**
 * @description: redis 服务接口
 * @author: hlx 2018-10-27
 **/
public interface RedisService {

    /**
     * 非阻塞取锁
     */
    boolean tryLock(String key, String value);

    /**
     * 释放锁
     */
    void unLock(String key, String value);

    /**
     * 添加排行榜(原子性)
     *
     * @param keysAndArgs 键和时光排行视图 字节数组
     */
    void addRank(byte[][] keysAndArgs);
}
