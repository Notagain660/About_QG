package com.example.demo.mapper;

import com.example.demo.entity.Repairorder;
import com.example.demo.enums.RepairStatus;

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
