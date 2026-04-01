package com.example.demo.enums;

public enum RepairStatus {
    PENDING("未处理"),
    COMPLETED("处理完成"),
    CANCELED("已取消");

    private final String text;

    RepairStatus(String text) {
        this.text = text;
    }


}
