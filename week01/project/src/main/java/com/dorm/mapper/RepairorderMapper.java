package com.dorm.mapper;

import com.dorm.entity.Repairorder;
import com.dorm.enums.RepairStatus;

import java.util.List;

public interface RepairorderMapper {

    void insertRepairorder(Repairorder repairorder);
    void updateRepairorder(Repairorder repairorder);

    List<Repairorder> selectAll();
    List<Repairorder> selectByUserId(String id);
    Repairorder selectByOrderId(Long orderId);
    List<Repairorder> selectByStatus(RepairStatus status);

    void deleteByOrderId(Long orderId);
}
