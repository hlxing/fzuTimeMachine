package com.west2.fzuTimeMachine.config;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import springfox.documentation.service.Parameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: Swagger2配置类, 构建api文档
 * @author: hlx 2018-08-14
 **/
@Configuration
@EnableSwagger2
@Setter
public class Swagger2Config extends WebMvcConfigurationSupport {

    /**
     * 解决spring boot2 与 swagger2 兼容问题
     * 详情 @see https://github.com/springfox/springfox/issues/2155
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Value("${swagger.host}")
    private String swaggerHost;

    @Bean
    public Docket docket() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name("S-TOKEN")
                .description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        pars.add(ticketPar.build());

        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        if (StringUtils.isNotBlank(swaggerHost)) {
            docket = docket.host(swaggerHost);
        }
        return docket
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.west2.fzuTimeMachine.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("FzuTimeMachine-API")
                .description("福大时光机API文档")
                .termsOfServiceUrl("https://github.com/hlxing/fzuTimeMachine")
                .contact(new Contact("hlx", "https://github.com/hlxing", "603773962@qq.com"))
                .version("1.0")
                .build();
    }

}
