package com.example.demo.verifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@Configuration
public class defender implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authinterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("Request URI: " + registry);
        registry.addInterceptor(authinterceptor)
                .addPathPatterns("/**");  // 拦截所有请求
    }

}
