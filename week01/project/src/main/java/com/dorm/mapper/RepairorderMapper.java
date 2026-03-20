package com.dorm.mapper;

import com.dorm.entity.Repairorder;

import java.util.Date;
import java.util.List;

public interface RepairorderMapper {

    void insertRepairorder(Repairorder repairorder);
    void updateRepairorder(Repairorder repairorder);

    List<Repairorder> selectAll();
    List<Repairorder> selectByUserId(String id);
    Repairorder selectByOrderId(Long orderId);
    List<Repairorder> selectByStatus(String status);
    List<Repairorder> selectByCreateTime(Date createTime);
    List<Repairorder> selectByUpdateTime(Date updateTime);
    List<Repairorder> selectByProcessTime(Date processTime);

    void deleteByOrderId(Long orderId);
}
