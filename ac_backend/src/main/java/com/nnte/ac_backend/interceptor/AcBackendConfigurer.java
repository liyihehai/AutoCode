package com.nnte.ac_backend.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AcBackendConfigurer implements WebMvcConfigurer {
    @Autowired
    private AcBackendMainInterceptor acBackendMainInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(acBackendMainInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/**/login");//添加不拦截路径
    }
}
