package com.west2.fzuTimeMachine.service.impl;

import com.west2.fzuTimeMachine.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * @description: redis 服务实现层
 * @author: hlx 2018-10-27
 **/
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    private static final Expiration expiration = Expiration.milliseconds(1000 * 60 * 60);
    private static final String LOCK_PREFIX = "lock:";
    private RedisConnectionFactory connectionFactory;

    @Autowired
    public RedisServiceImpl(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Bean
    public RedisScript<Integer> lockScript() {
        ScriptSource scriptSource = new ResourceScriptSource(
                new ClassPathResource("script/lock.lua"));
        try {
            return RedisScript.of(scriptSource.getScriptAsString());
        } catch (IOException e) {
            throw new RuntimeException("script not found");
        }
    }

    @Override
    public boolean tryLock(String key, String value) {
        RedisConnection redisCon = connectionFactory.getConnection();
        Boolean result = redisCon.set((LOCK_PREFIX + key).getBytes(), value.getBytes(), expiration,
                RedisStringCommands.SetOption.SET_IF_ABSENT);
        redisCon.close();
        return result != null ? result : false;
    }

    @Override
    public void unLock(String key, String value) {
        RedisScript<Integer> redisScript = lockScript();
        RedisConnection redisCon = connectionFactory.getConnection();
        redisCon.evalSha(redisScript.getSha1(), ReturnType.INTEGER, 1,
                (LOCK_PREFIX + key).getBytes(), value.getBytes());
        redisCon.close();
    }
}
