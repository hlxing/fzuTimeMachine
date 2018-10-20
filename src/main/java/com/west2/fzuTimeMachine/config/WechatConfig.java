package com.west2.fzuTimeMachine.config;

import com.west2.fzuTimeMachine.util.WechatUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;

/**
 * @description: 微信配置类
 * @author: hlx 2018-09-19
 **/
@Configuration
@ConfigurationProperties(prefix = "wechat")
@Data
public class WechatConfig {

    private String appID;

    private String appSecret;

    @PostConstruct
    public void initAccessToken() {
        // 将微信配置注入微信工具类中
        WechatUtil.init(this);
    }

}
