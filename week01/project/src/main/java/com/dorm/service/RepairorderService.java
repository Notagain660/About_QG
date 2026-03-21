package com.dorm.service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import com.dorm.entity.Repairorder;
import com.dorm.mapper.RepairorderMapper;
import com.dorm.entity.User;
import com.dorm.enums.RepairStatus;



public class RepairorderService {

    private final SqlSessionFactory sqlSessionFactory;

    public RepairorderService() {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean newRepairorder(User currentUser, String phoneNumber, String deviceType, String descriptionText) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {

            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            Repairorder repairorder = new Repairorder();

            String id = currentUser.getId();

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

            mapper.insertRepairorder(repairorder);

            Repairorder repairorder2 = mapper.selectByOrderId(repairorder.getOrderId());
            if (repairorder2 != null) {
                return true;
            }
        }
        return false;
    }

    public List<Repairorder> checkMyrepairorder(User currentUser) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            String id = currentUser.getId();
            return mapper.selectByUserId(id);
        }
    }

    public Repairorder checkRepairorder(Long orderId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            Repairorder repairorder = mapper.selectByOrderId(orderId);
            mapper.selectByOrderId(orderId);
            return repairorder;
        }
    }

    public boolean updateRepairorder(RepairStatus status, Long orderId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            Repairorder repairorder = mapper.selectByOrderId(orderId);

            Date previousDate = repairorder.getUpdateTime();

            repairorder.setStatus(status);
            repairorder.setUpdateTime(new Date());
            mapper.updateRepairorder(repairorder);

            if(status == RepairStatus.COMPLETED) {
                repairorder.setProcessTime(new Date());
            }

            Date nextDate = repairorder.getUpdateTime();
            if(previousDate != nextDate) {
                return true;
            }
        }
        return false;
    }

    public List<Repairorder> checkAllRepairorder() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            return mapper.selectAll();
        }
    }

    public List<Repairorder> checkAllRepairorderByStatus(String status) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            return mapper.selectByStatus(status);
        }
    }

    public boolean finishRepairorder(String priorityLevel, Long orderId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            Repairorder repairorder = mapper.selectByOrderId(orderId);

            String previousPriorityLevel = repairorder.getPriorityLevel();

            repairorder.setPriorityLevel(priorityLevel);
            mapper.updateRepairorder(repairorder);

            if (!previousPriorityLevel.equals(priorityLevel)) {
                return true;
            }
        }
        return false;
    }

    public boolean deleteRepairorder(Long orderId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);

            mapper.deleteByOrderId(orderId);
            Repairorder repairorder = mapper.selectByOrderId(orderId);
            if(repairorder == null){
                return true;
            }
        }
        return false;
    }

    public boolean loginRepairorder(Long orderId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            Repairorder repairorder = mapper.selectByOrderId(orderId);
            if((repairorder != null) && (orderId.equals(repairorder.getOrderId()))){
                return true;
            }
        }
        return false;
    }
}
