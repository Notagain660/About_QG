package com.dorm.service;

import org.apache.ibatis.exceptions.PersistenceException;
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
            return repairorder2 != null;
        }catch (PersistenceException e) {
            // 记录日志
            System.err.println("数据库操作失败：" + e.getMessage());
            e.printStackTrace();
            // 转换为运行时异常，让上层统一处理
            throw new RuntimeException("创建报修单失败，请稍后重试", e);
        }

    }

    public List<Repairorder> checkMyrepairorder(User currentUser) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            String id = currentUser.getId();
            return mapper.selectByUserId(id);
        }catch (PersistenceException e) {
            // 数据库操作异常（如连接失败、SQL错误）
            System.err.println("数据库操作失败：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("系统繁忙，请稍后重试", e);
        } catch (Exception e) {
            // 其他未预料异常
            System.err.println("未知错误：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("操作失败", e);
        }

    }

    public Repairorder checkRepairorder(Long orderId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            Repairorder repairorder = mapper.selectByOrderId(orderId);
            mapper.selectByOrderId(orderId);
            return repairorder;
        }catch (PersistenceException e) {
            // 数据库操作异常（如连接失败、SQL错误）
            System.err.println("数据库操作失败：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("系统繁忙，请稍后重试", e);
        } catch (Exception e) {
            // 其他未预料异常
            System.err.println("未知错误：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("操作失败", e);
        }
    }

    public boolean updateRepairorder(RepairStatus status, Long orderId, String comments) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            Repairorder repairorder = mapper.selectByOrderId(orderId);

            Date previousDate = repairorder.getUpdateTime();

            repairorder.setStatus(status);
            repairorder.setUpdateTime(new Date());
            repairorder.setComments(comments);
            mapper.updateRepairorder(repairorder);

            if(status == RepairStatus.COMPLETED) {
                repairorder.setProcessTime(new Date());
            }

            Date nextDate = repairorder.getUpdateTime();
            return previousDate != nextDate;
        }catch (PersistenceException e) {
            // 记录日志
            System.err.println("数据库操作失败：" + e.getMessage());
            e.printStackTrace();
            // 转换为运行时异常，让上层统一处理
            throw new RuntimeException("更新报修单失败，请稍后重试", e);
        }

    }

    public List<Repairorder> checkAllRepairorder() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            return mapper.selectAll();
        }catch (PersistenceException e) {
            // 数据库操作异常（如连接失败、SQL错误）
            System.err.println("数据库操作失败：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("系统繁忙，请稍后重试", e);
        } catch (Exception e) {
            // 其他未预料异常
            System.err.println("未知错误：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("操作失败", e);
        }

    }

    public List<Repairorder> checkAllRepairorderByStatus(RepairStatus status) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            return mapper.selectByStatus(status);
        }catch (PersistenceException e) {
            // 数据库操作异常（如连接失败、SQL错误）
            System.err.println("数据库操作失败：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("系统繁忙，请稍后重试", e);
        } catch (Exception e) {
            // 其他未预料异常
            System.err.println("未知错误：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("操作失败", e);
        }

    }

    public boolean finishRepairorder(String priorityLevel, Long orderId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            Repairorder repairorder = mapper.selectByOrderId(orderId);

            String previousPriorityLevel = repairorder.getPriorityLevel();

            repairorder.setPriorityLevel(priorityLevel);
            mapper.updateRepairorder(repairorder);
            if (previousPriorityLevel == null) {
                return true;
            }
            return !previousPriorityLevel.equals(priorityLevel);
        }catch (PersistenceException e) {
            // 记录日志
            System.err.println("数据库操作失败：" + e.getMessage());
            e.printStackTrace();
            // 转换为运行时异常，让上层统一处理
            throw new RuntimeException("上传报修单优先级失败，请稍后重试", e);
        }

    }

    public boolean deleteRepairorder(Long orderId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);

            mapper.deleteByOrderId(orderId);
            Repairorder repairorder = mapper.selectByOrderId(orderId);
            return repairorder == null;
        }catch (PersistenceException e) {
            // 记录日志
            System.err.println("数据库操作失败：" + e.getMessage());
            e.printStackTrace();
            // 转换为运行时异常，让上层统一处理
            throw new RuntimeException("删除报修单失败，请稍后重试", e);
        }

    }

    public boolean loginRepairorder(Long orderId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            RepairorderMapper mapper = session.getMapper(RepairorderMapper.class);
            Repairorder repairorder = mapper.selectByOrderId(orderId);
            return (repairorder != null) && (orderId.equals(repairorder.getOrderId()));
        }catch (PersistenceException e) {
            // 记录日志
            System.err.println("数据库操作失败：" + e.getMessage());
            e.printStackTrace();
            // 转换为运行时异常，让上层统一处理
            throw new RuntimeException("载入报修单失败，请稍后重试", e);
        }

    }
}
