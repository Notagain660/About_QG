package com.example.demo.enums;

import java.util.Arrays;

public enum RepairStatus {
    PENDING("未处理"),
    PROCESSING("正在处理"),
    COMPLETED("处理完成"),
    CANCELED("已取消");

    private final String text;

    RepairStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static RepairStatus fromChoice(int sTatus) {
        return switch (sTatus){
            case 1 -> PROCESSING;
            case 2 -> COMPLETED;
            default -> throw new IllegalArgumentException("无效状态选择");
        };
    }

    public static RepairStatus fromText(String text) {
         for (RepairStatus status : values()) {
            if (status.getText().equals(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的状态：" + text);
    }


}
