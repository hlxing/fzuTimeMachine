package com.west2.fzuTimeMachine.config.shiro;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: cros配置类
 * @author: hlx 2018-10-13
 **/
public class CROSFilter extends BasicHttpAuthenticationFilter {

    //不阻挡所有请求，请求的最终合法性由接口上的注解判断-*
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "S-TOKEN");
        return true;
    }
}
