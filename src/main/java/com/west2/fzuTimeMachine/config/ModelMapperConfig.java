package com.west2.fzuTimeMachine.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: modelMapper配置类
 * @author: hlx 2018-08-28
 **/
@Configuration
public class ModelMapperConfig {

    // 注册mapper
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
