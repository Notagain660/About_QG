package com.example.demo.service;

import java.util.*;
import com.example.demo.entity.Repairorder;
import com.example.demo.entity.User;

import com.example.demo.enums.RepairStatus;
import com.example.demo.mapper.RepairorderMapper;
import com.example.demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RepairorderService {

    private final RepairorderMapper repairorderMapper;
    private final UserMapper userMapper;

    public long newRepairorder(String id, String phoneNumber, String deviceType, String descriptionText) {

            Repairorder repairorder = new Repairorder();

            User currentUser = userMapper.selectById(id);

            String dormBuilding = currentUser.getDormBuilding();
            String roomNumber = currentUser.getRoomNumber();
            RepairStatus status = RepairStatus.PENDING;

            repairorder.setId(id);
            repairorder.setDormBuilding(dormBuilding);
            repairorder.setRoomNumber(roomNumber);
            repairorder.setPhoneNumber(phoneNumber);
            repairorder.setDeviceType(deviceType);
            repairorder.setDescriptionText(descriptionText);
            repairorder.setCreateTime(new Date());
            repairorder.setStatus(status);

            repairorderMapper.insertRepairorder(repairorder);

            Repairorder repairorder2 = repairorderMapper.selectByOrderId(repairorder.getOrderId());
            if(repairorder2 != null){
                return repairorder.getOrderId();
            }
            return 0;
    }

    public boolean updateRepairorder(RepairStatus status, Long orderId, String comments) {

            Repairorder repairorder = repairorderMapper.selectByOrderId(orderId);

            Date previousDate = repairorder.getUpdateTime();

            repairorder.setStatus(status);
            repairorder.setUpdateTime(new Date());
            repairorder.setComments(comments);
            repairorderMapper.updateRepairorder(repairorder);

            if(status == RepairStatus.COMPLETED) {
                repairorder.setProcessTime(new Date());
            }

            Date nextDate = repairorder.getUpdateTime();
            return previousDate != nextDate;

    }

    public boolean finishRepairorder(String priorityLevel, Long orderId) {

            Repairorder repairorder = repairorderMapper.selectByOrderId(orderId);

            String previousPriorityLevel = repairorder.getPriorityLevel();

            repairorder.setPriorityLevel(priorityLevel);
            repairorderMapper.updateRepairorder(repairorder);
            if (previousPriorityLevel == null) {
                return true;
            }
            return !previousPriorityLevel.equals(priorityLevel);


    }

    public boolean deleteRepairorder(Long orderId) {

            repairorderMapper.deleteByOrderId(orderId);
            Repairorder repairorder = repairorderMapper.selectByOrderId(orderId);
            return repairorder == null;

    }

}
