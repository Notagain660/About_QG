package com.example.demo.utils;

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

    public static <T> MapperResult<T> error(String message) {
        return new MapperResult<>(400, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
