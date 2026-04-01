package com.example.demo.entity;

import java.util.Date;
import com.example.demo.enums.RepairStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Repairorder {
    private String id;
    private Long orderId;
    private String dormBuilding;
    private String roomNumber;
    private String phoneNumber;
    private String deviceType;
    private RepairStatus status;
    private String descriptionText;
    private Date createTime;
    private Date updateTime;
    private Date processTime;
    private String priorityLevel;
    private String comments;

    public Repairorder() {

    }

    @Override
    public String toString() {
        return STR."repairOrder{学号/工号\{id}\n报修表号\{orderId}\n宿舍号\{dormBuilding}\{roomNumber}\n联系方式（电话号码）\{phoneNumber}\n设备类型\{deviceType}\n状态\{status}\n问题描述\{descriptionText}\n创建时间\{createTime}\n更新时间\{updateTime}\n报修申请处理时间\{processTime}\n优先级\{priorityLevel}\n维修评价\{comments}";
    }
}
