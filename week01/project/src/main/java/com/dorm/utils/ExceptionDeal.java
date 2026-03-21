package com.dorm.utils;

public class ExceptionDeal {
    public static void deal(Runnable action){
        try {
            action.run();
        }catch (RuntimeException e){
            System.err.printf("运行错误！" + e.getMessage());
            //e.printStackTrace(System.err);
        }
    }
}
