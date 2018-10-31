package com.west2.fzuTimeMachine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * @description: HttpSession配置
 * @author: hlx 2018-08-18
 **/
@Configuration
@EnableSpringHttpSession
public class HttpSessionConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private int database;

    //存储容器连接池
    @Bean
    public LettuceConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setDatabase(database);
        config.setPassword(RedisPassword.of(password));
        config.setPort(port);
        config.setHostName(host);

        return new LettuceConnectionFactory(config);
    }

    //通过头部传递session
    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        //自定义头部为S-TOKEN
        return new HeaderHttpSessionIdResolver("S-TOKEN");
    }

}
