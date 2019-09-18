package com.nnte.ac_backend.controller.autoCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AutoCodeConfigurer implements WebMvcConfigurer {
    @Autowired
    private AutoCodeInterceptor autoCodeInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录拦截的管理器
        InterceptorRegistration registration = registry.addInterceptor(autoCodeInterceptor);
        registration.addPathPatterns("/autoCode/**");//所有路径都被拦截
        registration.excludePathPatterns("/","/login","/error","/static/**","/logout");//添加不拦截路径
    }
}
