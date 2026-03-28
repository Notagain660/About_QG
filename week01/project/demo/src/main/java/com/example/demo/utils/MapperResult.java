package com.example.demo.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapperResult<T> {
    private int code;
    private String message;
    private T data;
    public MapperResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 静态工厂方法
    public static <T> MapperResult<T> success(String message, T data) {
        return new MapperResult<>(200, message, data);
    }

    public static <T> MapperResult<T> error(int code, String message) {
        return new MapperResult<>(code, message, null);
    }

}
