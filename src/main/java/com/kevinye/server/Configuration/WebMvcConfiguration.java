package com.kevinye.server.Configuration;

import com.kevinye.server.interceptor.UserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    private UserInterceptor userInterceptor;
    public WebMvcConfiguration(UserInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器");
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/login");
    }
}
