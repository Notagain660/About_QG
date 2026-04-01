package com.example.demo.verifier;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class defender implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(defender.class);
    private final AuthInterceptor authinterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Request URI: {}", registry);
        registry.addInterceptor(authinterceptor)
                .addPathPatterns("/**");  // 拦截所有请求
    }

}
