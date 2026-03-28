package com.example.demo.web;

import cn.hutool.core.io.resource.NoResourceException;
import com.example.demo.utils.MapperResult;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice//处理controller抛出的异常，返回为json，Spring启动时会自动扫描并注册这个 Bean
@Slf4j
public class exceptionDeal {

    private static final Logger logger = LoggerFactory.getLogger(exceptionDeal.class);

    @ExceptionHandler(Exception.class)
    public MapperResult<Void> handleException(Exception e) {//可以定义多个 @ExceptionHandler方法，处理不同的异常类型
        log.error("系统异常", e);
        return MapperResult.error(404,"服务器繁忙，请稍后再试");
    }

    @ExceptionHandler(NoResourceException.class)
    public MapperResult<Void> handleNoResourceException(NoResourceException e) {
        log.error("资源不存在", e);
        return MapperResult.error(500, "请求路径不存在");
    }
}
