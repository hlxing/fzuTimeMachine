package com.west2.fzuTimeMachine;

import com.west2.fzuTimeMachine.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: hlx 2018-10-27
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void tryLockTest() {
        log.info("r->" + redisService.tryLock("rank", "123456"));
    }

    @Test
    public void unlockTest() {
        redisService.unLock("rank", "123456");
    }

}
