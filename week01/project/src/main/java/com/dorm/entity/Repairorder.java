package com.dorm.entity;

import java.util.Date;
import com.dorm.enums.RepairStatus;

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

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getDormBuilding() {
        return dormBuilding;
    }

    public void setDormBuilding(String dormBuilding) {
        this.dormBuilding = dormBuilding;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public RepairStatus getStatus() {
        return status;
    }

    public void setStatus(RepairStatus status) {
        this.status = status;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String desctiptionText) {
        this.descriptionText = desctiptionText;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "repairOrder{" +
                "学号/工号" + id + '\n' +
                "报修表号" + orderId + '\n' +
                "宿舍号" + dormBuilding + roomNumber + '\n' +
                "联系方式（电话号码）" + phoneNumber + '\n' +
                "设备类型" + deviceType + '\n' +
                "状态" + status + '\n' +
                "问题描述" + descriptionText + '\n' +
                "创建时间" + createTime + '\n' +
                "更新时间" + updateTime + '\n' +
                "报修申请处理时间" + processTime + '\n' +
                "优先级" + priorityLevel + '\n' +
                "维修评价" + comments;
    }
}
