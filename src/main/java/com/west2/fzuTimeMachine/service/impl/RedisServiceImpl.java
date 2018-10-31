package com.west2.fzuTimeMachine.service.impl;

import com.west2.fzuTimeMachine.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @description: redis 服务实现层
 * @author: hlx 2018-10-27
 **/
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    private static final Expiration expiration = Expiration.milliseconds(1000 * 60 * 60);

    private static final String LOCK_PREFIX = "lock:";

    private static String UNLOCK_SHA;

    private static String SET_RANK_SHA;

    private RedisConnectionFactory connectionFactory;

    @Autowired
    public RedisServiceImpl(LettuceConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @PostConstruct
    private void initScript() {
        DefaultStringRedisConnection defaultStringRedisConnection =
                new DefaultStringRedisConnection(connectionFactory.getConnection());

        ScriptSource lockSource = new ResourceScriptSource(
                new ClassPathResource("script/lock.lua"));
        ScriptSource addRankSource = new ResourceScriptSource(
                new ClassPathResource("script/set_rank.lua"));
        try {
            UNLOCK_SHA = RedisScript.of(lockSource.getScriptAsString()).getSha1();
            SET_RANK_SHA = RedisScript.of(addRankSource.getScriptAsString()).getSha1();

            defaultStringRedisConnection.scriptLoad(lockSource.getScriptAsString());
            defaultStringRedisConnection.scriptLoad(addRankSource.getScriptAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        defaultStringRedisConnection.close();
        log.info("init script success");
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
        RedisConnection redisCon = connectionFactory.getConnection();
        redisCon.evalSha(UNLOCK_SHA, ReturnType.INTEGER, 1,
                (LOCK_PREFIX + key).getBytes(), value.getBytes());
        redisCon.close();
    }

    @Override
    public void addRank(byte[][] keysAndArgs) {
        RedisConnection redisCon = connectionFactory.getConnection();
        redisCon.evalSha(SET_RANK_SHA, ReturnType.BOOLEAN, 1,
                keysAndArgs);
        redisCon.close();
    }

}
