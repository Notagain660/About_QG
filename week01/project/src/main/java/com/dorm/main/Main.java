package com.dorm.main;

import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.List;

import com.dorm.entity.Repairorder;
import com.dorm.entity.User;
import com.dorm.service.UserService;
import com.dorm.service.RepairorderService;
import com.dorm.enums.Role;
import com.dorm.enums.RepairStatus;

public class Main {
    public static void main(String[] args) {

        var scanner = new Scanner(System.in);
        var userservice = new UserService();
        var repairorderservice = new RepairorderService();
        User currentUser;

      while(true){

          List<String> menu = List.of("===========================",
                  "\uD83C\uDFE0 宿舍报修管理系统",
                  "===========================",
                  "1.登录", "2.注册", "3.退出",
                  "请选择操作（输入 1-3 ）");

          for(var menuItem : menu){
              System.out.println(menuItem);
          }

          int menuChoice = 1, i = 0;
          while(i == 0){
              try {
                  menuChoice = scanner.nextInt();
                  scanner.nextLine();
              }  catch (Exception e) {
                  scanner.nextLine();
                  System.out.println("输入无效，请重新输入：");
                  continue;
              }
              if(menuChoice < 1 || menuChoice > 3) {
                  scanner.nextLine();
                  System.out.println("输入无效，请重新输入：");
              } else {
                  i = 1;
              }
          }
          switch (menuChoice) {
              case 1 -> {
                  scanner.nextLine();
                  clear();
                  currentUser = login(scanner, userservice);
                  clear();
                  if (!currentUser.equals(new User())) {
                      if (currentUser.getRole() == Role.ADMIN) {
                          administerMenu(currentUser, scanner, repairorderservice, userservice);
                      } else {
                          studentMenu(currentUser, scanner, userservice, repairorderservice);
                      }
                  }

              }


              case 2 -> {
                  scanner.nextLine();
                  clear();
                  Register(scanner, userservice);
              }

              case 3 -> {
                  clear();
                  System.out.println("感谢您的使用！");
                  System.exit(0);
              }
              default -> {
                  clear();
                  System.out.println("输入无效，请重新输入。");
              }
          }
      }
    }

    public static void clear(){
        System.out.print("\033[H\033[2J"); // 发送清屏指令
        System.out.flush();
    }

    public static void Register(Scanner scanner, UserService userservice) {

        String id, name, password;
        Role role;


            System.out.println("===== 用户注册 =====");
            System.out.println("请选择角色（1-学生，2-维修人员）：");

            int roleNumber;int i = 0;


            roleNumber = scanner.nextInt();
            scanner.nextLine();

            role = Role.fromChoice(roleNumber);

            if(role == Role.ADMIN) {
                System.out.println("请输入工号（前四位是0025）：");
            }else if(role == Role.STUDENT) {
                System.out.println("请输入学号（前四位是3225/3125/5124）：");
            }
            id = scanner.nextLine();

            while(i == 0){
                try {
                    id = scanner.nextLine();
                }  catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("输入无效，请重新输入：");
                    continue;
                }
                if(!id.matches("^(3125|3225|5124|0025)\\{6}$")) {
                    scanner.nextLine();
                    System.out.println("学号不符合格式，请重新输入：");
                } else {
                    i = 1;
                }
            }

            System.out.println("请输入姓名：");
            name = scanner.nextLine();

            System.out.println("请输入密码：");
            password = scanner.nextLine();//待优化：判断密码强度和加密

            System.out.println("请确认密码：");
            String password1 = scanner.nextLine();

            while (!password.equals(password1)) {
                System.out.println("请重新输入密码：");
                password = scanner.nextLine();
                System.out.println("请确认密码：");
                password1 = scanner.nextLine();
            }

            if(userservice.register(role, id, name, password)) {
                System.out.println("注册成功！请返回主界面登录。");
            }
            else {
                System.out.println("注册失败，请返回主界面重新注册。");
            }
    }

    public static User login(Scanner scanner, UserService userservice) {

        String id, password;
        var user = new User();

        System.out.println("===== 用户登录 =====");
        System.out.println("请输入账号：");
        id = scanner.nextLine();
        System.out.println("请输入密码：");
        password = scanner.nextLine();
        user = userservice.login(id, password);
        if(user != null) {
            System.out.println("登录成功！角色：" + user.getRole().getRoleName());
            return user;
        }else{
            //System.out.println("(账号或密码错误。");
            System.out.println("登录失败！请重新登录。");
            return new User();
        }//还能优化一下是用户不存在还是账号或密码错误
    }

    public static void studentMenu(User currentUser, Scanner scanner, UserService userservice,
                                   RepairorderService repairorderservice) {

        int i = 0, menuChoice = 0;

        List<String> list = List.of("===== 学生菜单 =====",
                "1. 绑定/修改宿舍", "2. 查看个人信息", "3. 创建报修单", "4. 查看我的报修记录", "5. 取消报修单", "6. 修改密码",
                "7. 退出");
        for (var s : list) {
            System.out.println(s);
        }
        System.out.println("请选择操作（输入1-6）");

        while(i == 0){
            try {
                menuChoice = scanner.nextInt();
                scanner.nextLine();
            }  catch (Exception e) {
                scanner.nextLine();
                System.out.println("输入无效，请重新输入：");
                continue;
            }
            if(menuChoice < 1 || menuChoice > 7) {
                scanner.nextLine();
                System.out.println("输入无效，请重新输入：");
            } else {
                       i = 1;
            }
        }
        switch (menuChoice) {
            case 1 -> {
                clear();
                dormFix(currentUser, scanner, userservice);//话说菜单功能返回上一步怎么做啊。
            }
               case 2 -> {
                clear();
                printInformation(currentUser);
            }

            case 3 -> {
                clear();
                newRepairorder(currentUser, scanner, repairorderservice);
            }

            case 4 -> {
                clear();
                checkMyRepairorder(currentUser, scanner,repairorderservice);
            }

            case 5 -> {
                clear();
                cancelRepairorder(scanner,repairorderservice);
            }

            case 6 -> {
                clear();
                passwordChange(currentUser, scanner, userservice);
            }

            case 7 -> {
                clear();
                System.out.println("感谢您的使用！");
            }
        }
 }

    public static void dormFix(User currentUser, Scanner scanner, UserService userservice) {
        //宿舍绑定细化功能可以是进入之后看到绑定的宿舍（与否）然后再可选是否修改/绑定/退出，
        // 并且宿舍号输入的检验（异常）可以包括宿舍楼和宿舍号，0318先写基础功能
        String dormBuilding, roomNumber, password = currentUser.getPassword();
        boolean x;

        if (currentUser.getDormBuilding() == null) {
            System.out.println("宿舍暂未绑定，已进入绑定程序······");
            x = true;
        } else {
            System.out.println("宿舍号已存在，已进入修改程序······");
            x = false;
        }
        System.out.println("请输入宿舍楼号：（示例：西六）");
        dormBuilding = scanner.nextLine();
        System.out.println("请输入宿舍房间号：（示例：304）");
        roomNumber = scanner.nextLine();

        if (x) {
            if(userservice.updater(currentUser, dormBuilding, roomNumber, password)){
                System.out.println("宿舍号绑定完毕！");
            }else{
                System.out.println("宿舍号绑定失败！");
            }
        } else {
            if(userservice.updater(currentUser, dormBuilding, roomNumber, password)){
            System.out.println("宿舍号修改完毕！");
            }else{
                System.out.println("宿舍号修改失败！");
            }
        }
    }

    public static void printInformation(User currentUser){
        System.out.println("=====用户基本信息=====");
        System.out.println("学号/工号：" + currentUser.getId());
        System.out.println("姓名：" +  currentUser.getName());
        System.out.println("身份：" + currentUser.getRole());
        System.out.println("宿舍号：" + currentUser.getDormBuilding() + currentUser.getRoomNumber());
 }

    public static void passwordChange(User currentUser,Scanner scanner, UserService userservice){

        String dormBuilding = currentUser.getDormBuilding(), roomNumber = currentUser.getRoomNumber();

        System.out.println("请输入新密码：");
        String password = scanner.nextLine();
        System.out.println("请确认密码：");
        String password1 = scanner.nextLine();

        while (!password.equals(password1)) {
         System.out.println("请重新输入密码：");
         password = scanner.nextLine();
         System.out.println("请确认密码：");
         password1 = scanner.nextLine();
     }
        if(userservice.updater(currentUser, dormBuilding, roomNumber, password)){
            System.out.println("密码修改成功！");
        } else {
            System.out.println("密码修改失败，请检查新密码与原密码是否一致。");
        }
 }

    public static void newRepairorder(User currentUser, Scanner scanner, RepairorderService repairorderservice) {

        String phoneNumber, deviceType, descriptionText;

        System.out.println("=====创建报修表=====");
        System.out.println("请填写联系方式（电话号码）：");
        phoneNumber = scanner.nextLine();
        System.out.println("请填写设备类型：");
        deviceType = scanner.nextLine();
        System.out.println("请填写问题描述：");
        descriptionText = scanner.nextLine();

        if(repairorderservice.newRepairorder(currentUser, phoneNumber,  deviceType, descriptionText )) {
            System.out.println("报修表提交成功！");
        }else {
            System.out.println("报修表提交失败，请重新填写。");
        }
    }

    public static void checkMyRepairorder(User currentUser, Scanner scanner, RepairorderService repairorderservice) {

        List<Repairorder> r = repairorderservice.checkMyrepairorder(currentUser);
        long orderId = r.get(0).getOrderId();

        System.out.printf("%-12s %-10s %-10s %-5s %-20s %-20s %-20s\n", "学号", "报修单号", "设备类型", "状态", "创建时间", "最新提交时间", "完成时间");

        for (Repairorder repairorder : r) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String deviceType = repairorder.getDeviceType() == null ? "-" : repairorder.getDeviceType();
            String status = repairorder.getStatus().getText();
            String createTime = repairorder.getCreateTime()  == null ? "-" : sdf.format(repairorder.getCreateTime());
            String updateTime = repairorder.getUpdateTime()  == null ? "-" : sdf.format(repairorder.getUpdateTime());
            String processTime = repairorder.getProcessTime() == null ? "-" : sdf.format(repairorder.getProcessTime());

            System.out.printf("%-12s %-10d %-10s %-5s %-20s %-20s %-20s\n", repairorder.getId(), repairorder.getOrderId(), deviceType, status, createTime, updateTime, processTime);

        }

        if(r.isEmpty()) {
            System.out.println("请按任意键返回：");
            scanner.nextLine();
            clear();
        }else {
            System.out.println("输入报修单号以查看详情······");

            int i = 0;
            while(i == 0){
                try {
                    orderId = scanner.nextLong();
                    scanner.nextLine();
                }catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("输入无效，请重新输入！");
                    continue;
                }
                if(repairorderservice.loginRepairorder(orderId)){
                    System.out.println("报修单号不存在，请重新输入！");
                }else{
                    i = 1;
                }
            }

            clear();

            Repairorder repairorder = repairorderservice.checkRepairorder(orderId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dormBuilding = repairorder.getDormBuilding() == null ? "-" : repairorder.getDormBuilding();
            String roomNumber = repairorder.getRoomNumber() == null ? "-" : repairorder.getRoomNumber();
            String phoneNumber = repairorder.getPhoneNumber() == null ? "-" : repairorder.getPhoneNumber();
            String deviceType = repairorder.getDeviceType() == null ? "-" : repairorder.getDeviceType();
            String descriptionText = repairorder.getDescriptionText() == null ? "-" : repairorder.getDescriptionText();
            String createTime = repairorder.getCreateTime() == null ? "-" : sdf.format(repairorder.getCreateTime());
            String updateTime = repairorder.getUpdateTime() == null ? "-" : sdf.format(repairorder.getUpdateTime());
            String processTime = repairorder.getProcessTime() == null ? "-" : sdf.format(repairorder.getProcessTime());
            String comments;
            if (processTime.equals("-")) {
                comments = "尚未维修完毕！";
            } else {
                comments = repairorder.getComments() == null ? "-" : repairorder.getComments();
            }

            System.out.printf("学号/工号：" + repairorder.getId());
            System.out.printf("报修单号：" + repairorder.getOrderId());
            System.out.printf("宿舍号：" + dormBuilding + roomNumber);
            System.out.printf("联系方式（电话号码：" + phoneNumber);
            System.out.printf("设备类型：" + deviceType);
            System.out.printf("状态：" + repairorder.getStatus());
            System.out.printf("问题描述：" + descriptionText);
            System.out.printf("创建时间：" + createTime);
            System.out.printf("最新提交时间：" + updateTime);
            System.out.printf("处理完成时间：" + processTime);
            System.out.printf("维修评价：" + comments);

        }

        System.out.println("请按任意键返回：");
        scanner.nextLine();
        clear();

    }

    public static void cancelRepairorder(Scanner scanner, RepairorderService repairorderservice) {

        System.out.println("请输入需要取消的报修单号：");
        long orderId = scanner.nextLong();
        scanner.nextLine();

        int i = 0;
        while(i == 0){
            try {
                orderId = scanner.nextLong();
                scanner.nextLine();
            }catch (Exception e) {
                System.out.println("输入无效，请重新输入：");
                scanner.nextLine();
                continue;
            }
            if(repairorderservice.loginRepairorder(orderId)){
                System.out.println("报修单号不存在，请重新输入！");
            }else{
                i = 1;
            }
        }

            RepairStatus status = RepairStatus.CANCELED;

            if(repairorderservice.updateRepairorder(status,orderId)){
                System.out.println("取消报修单成功！");
            } else {
                System.out.println("取消报修单失败，请稍后再试！");
            }



    }

    public static void administerMenu(User currentUser, Scanner scanner,
                                      RepairorderService repairorderservice, UserService userservice) {

        int i = 0, menuChoice = 0;

        List<String> list = List.of("===== 管理员菜单 =====",
                "1. 查看所有报修单", "2. 查看报修单详情", "3. 更新报修单状态", "4. 删除报修单", "5. 修改密码", "6. 退出");
        for (String s : list) {
            System.out.println(s);
        }
        System.out.println("请选择操作（输入1-6）");

        while(i == 0){
            try {
                menuChoice = scanner.nextInt();
                scanner.nextLine();
            }  catch (Exception e) {
                scanner.nextLine();
                System.out.println("输入无效，请重新输入：");
                continue;
            }
            if(menuChoice < 1 || menuChoice > 7) {
                scanner.nextLine();
                System.out.println("输入无效，请重新输入：");
            } else {
                i = 1;
            }
        }
        switch (menuChoice) {
            case 1 -> {
                clear();
                adCheckAll(scanner, repairorderservice);
            }
            case 2 -> {
                clear();
                checkCertain(scanner,repairorderservice);
            }

            case 3 -> {
                clear();
                adUpdatestatus(scanner, repairorderservice);
            }

            case 4 -> {
                clear();
                deleteRepairorder(scanner,repairorderservice);
            }

            case 5 -> {
                clear();
                passwordChange(currentUser, scanner, userservice);
            }

            case 6 -> {
                clear();
                System.out.println("感谢您的使用！");
            }
        }
    }

    public static void adCheckAll(Scanner scanner, RepairorderService repairorderservice) {

        int i = 0;

        List<String> checkMenu = List.of("1. 查看所有报修单",
                "2. 查看未处理的报修单", "3. 查看正在处理的报修单", "4. 查看处理完成的报修单",
                "请选择报修单查看方式（如查看所有报修单，请按1：）");
        for(String s : checkMenu) {
            System.out.println(s);
        }

        int wayChoice = scanner.nextInt();
        scanner.nextLine();

        while(i == 0){
            try {
                wayChoice = scanner.nextInt();
                scanner.nextLine();
            }  catch (Exception e) {
                scanner.nextLine();
                System.out.println("输入无效，请重新输入：");
                continue;
            }
            if(wayChoice < 1 || wayChoice > 4) {
                scanner.nextLine();
                System.out.println("输入无效，请重新输入：");
            } else {
                i = 1;
            }
        }

        switch (wayChoice) {
            case 1 -> {
                clear();
                List<Repairorder> r = repairorderservice.checkAllRepairorder();

                System.out.printf("%-12s %-10s %-10s %-5s %-20s %-20s %-20s %-5s\n", "学号", "报修单号", "设备类型", "状态",
                        "创建时间", "最新提交时间", "完成时间", "优先级");

                for (Repairorder repairorder : r) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String deviceType = repairorder.getDeviceType() == null ? "-" : repairorder.getDeviceType();
                    String status = repairorder.getStatus().getText();
                    String createTime = repairorder.getCreateTime()  == null ? "-" : sdf.format(repairorder.getCreateTime());
                    String updateTime = repairorder.getUpdateTime()  == null ? "-" : sdf.format(repairorder.getUpdateTime());
                    String processTime = repairorder.getProcessTime() == null ? "-" : sdf.format(repairorder.getProcessTime());
                    String priorityLevel = repairorder.getPriorityLevel() == null ? "-" : repairorder.getPriorityLevel();

                    System.out.printf("%-12s %-10d %-10s %-5s %-20s %-20s %-20s %-5s\n", repairorder.getId(),
                            repairorder.getOrderId(),
                            deviceType, status, createTime, updateTime, processTime, priorityLevel);
                }

            }

            case 2 -> {
                clear();
                List<Repairorder> r = repairorderservice.checkAllRepairorderByStatus("未处理");
                for (Repairorder repairorder : r) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String deviceType = repairorder.getDeviceType() == null ? "-" : repairorder.getDeviceType();
                    String status = repairorder.getStatus().getText();
                    String createTime = repairorder.getCreateTime()  == null ? "-" : sdf.format(repairorder.getCreateTime());
                    String updateTime = repairorder.getUpdateTime()  == null ? "-" : sdf.format(repairorder.getUpdateTime());
                    String processTime = repairorder.getProcessTime() == null ? "-" : sdf.format(repairorder.getProcessTime());
                    String priorityLevel = repairorder.getPriorityLevel() == null ? "-" : repairorder.getPriorityLevel();

                    System.out.printf("%-12s %-10d %-10s %-5s %-20s %-20s %-20s %-5s\n", repairorder.getId(),
                            repairorder.getOrderId(),
                            deviceType, status, createTime, updateTime, processTime, priorityLevel);
                }

            }

            case 3 -> {
                clear();
                List<Repairorder> r = repairorderservice.checkAllRepairorderByStatus("正在处理");
                for (Repairorder repairorder : r) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String deviceType = repairorder.getDeviceType() == null ? "-" : repairorder.getDeviceType();
                    String status = repairorder.getStatus().getText();
                    String createTime = repairorder.getCreateTime()  == null ? "-" : sdf.format(repairorder.getCreateTime());
                    String updateTime = repairorder.getUpdateTime()  == null ? "-" : sdf.format(repairorder.getUpdateTime());
                    String processTime = repairorder.getProcessTime() == null ? "-" : sdf.format(repairorder.getProcessTime());
                    String priorityLevel = repairorder.getPriorityLevel() == null ? "-" : repairorder.getPriorityLevel();

                    System.out.printf("%-12s %-10d %-10s %-5s %-20s %-20s %-20s %-5s\n", repairorder.getId(),
                            repairorder.getOrderId(),
                            deviceType, status, createTime, updateTime, processTime, priorityLevel);
                }
            }

            case 4 -> {
                clear();
                List<Repairorder> r = repairorderservice.checkAllRepairorderByStatus("处理完成");
                for (Repairorder repairorder : r) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String deviceType = repairorder.getDeviceType() == null ? "-" : repairorder.getDeviceType();
                    String status = repairorder.getStatus().getText();
                    String createTime = repairorder.getCreateTime()  == null ? "-" : sdf.format(repairorder.getCreateTime());
                    String updateTime = repairorder.getUpdateTime()  == null ? "-" : sdf.format(repairorder.getUpdateTime());
                    String processTime = repairorder.getProcessTime() == null ? "-" : sdf.format(repairorder.getProcessTime());
                    String priorityLevel = repairorder.getPriorityLevel() == null ? "-" : repairorder.getPriorityLevel();

                    System.out.printf("%-12s %-10d %-10s %-5s %-20s %-20s %-20s %-5s\n", repairorder.getId(),
                            repairorder.getOrderId(),
                            deviceType, status, createTime, updateTime, processTime, priorityLevel);
                }
            }
        }


    }

    public static void checkCertain(Scanner scanner, RepairorderService repairorderservice) {

        System.out.println("输入报修单号以查看详情······");
        Long orderId = scanner.nextLong();


        int i = 0;
        while(i ==0){
            try{
                scanner.nextLine();
            }catch(Exception e){
                System.out.println("输入无效，请重新输入：");
                scanner.nextLine();
                //System.out.println(e.getMessage());
                continue;
            }
            if(repairorderservice.loginRepairorder(orderId)){
                System.out.println("报修单号不存在，请重新输入！");
            }else{
                i = 1;
            }

        }

            clear();

            Repairorder repairorder = repairorderservice.checkRepairorder(orderId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dormBuilding = repairorder.getDormBuilding() == null ? "-" : repairorder.getDormBuilding();
            String roomNumber = repairorder.getRoomNumber() == null ? "-" : repairorder.getRoomNumber();
            String phoneNumber = repairorder.getPhoneNumber() == null ? "-" : repairorder.getPhoneNumber();
            String deviceType = repairorder.getDeviceType() == null ? "-" : repairorder.getDeviceType();
            String status = repairorder.getStatus().getText();
            String descriptionText = repairorder.getDescriptionText() == null ? "-" : repairorder.getDescriptionText();
            String createTime = repairorder.getCreateTime() == null ? "-" : sdf.format(repairorder.getCreateTime());
            String updateTime = repairorder.getUpdateTime() == null ? "-" : sdf.format(repairorder.getUpdateTime());
            String processTime = repairorder.getProcessTime() == null ? "-" : sdf.format(repairorder.getProcessTime());
            String prorityLevel =  repairorder.getPriorityLevel() == null ? "-" : repairorder.getPriorityLevel();
            String comments = repairorder.getComments() == null ? "-" : repairorder.getComments();
            if (processTime.equals("-")) {
                comments = "-";
            }

            System.out.printf("学号/工号：" + repairorder.getId());
            System.out.printf("报修单号：" + repairorder.getOrderId());
            System.out.printf("宿舍号：" + dormBuilding + roomNumber);
            System.out.printf("联系方式（电话号码：" + phoneNumber);
            System.out.printf("设备类型：" + deviceType);
            System.out.printf("状态：" + status);
            System.out.printf("问题描述：" + descriptionText);
            System.out.printf("创建时间：" + createTime);
            System.out.printf("最新提交时间：" + updateTime);
            System.out.printf("处理完成时间：" + processTime);
            System.out.println("优先级：" + prorityLevel);
            System.out.printf("维修评价：" + comments);

            System.out.println("请输入该报修单的优先级：");
            String priorityLevel = scanner.nextLine();
            if(repairorderservice.finishRepairorder(priorityLevel, orderId)) {
                System.out.println("优先级设置成功！");
            }else {System.out.println("优先级设置失败，请稍后再试。");}

            System.out.println("请按任意键返回：");
            scanner.nextLine();
            clear();

    }

    public static void adUpdatestatus(Scanner scanner, RepairorderService repairorderservice) {

        int i = 0;
        System.out.println("=====更新状态=====");
        System.out.println("请输入需要更新状态的报修单号");
        Long orderId = scanner.nextLong();
        scanner.nextLine();

        while(i ==0){
            try{
                scanner.nextLine();
            }catch(Exception e){
                System.out.println("输入无效，请重新输入：");
                scanner.nextLine();
                continue;
            }
            if(repairorderservice.loginRepairorder(orderId)){
                System.out.println("报修单号不存在，请重新输入：");
            }else{
                i = 1;
            }
        }

        i = 0;

            System.out.println("请输入状态（正在处理请按1，处理完成请按2）：");
            int sTatus = scanner.nextInt();
            scanner.nextLine();

            while(i == 0){
                try {
                    sTatus = scanner.nextInt();
                    scanner.nextLine();
                }  catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("输入无效，请重新输入：");
                    continue;
                }
                if(sTatus < 1 || sTatus > 2) {
                    scanner.nextLine();
                    System.out.println("输入无效，请重新输入：");
                } else {
                    i = 1;
                }
            }

            RepairStatus status = RepairStatus.fromChoice(sTatus);

            if(repairorderservice.updateRepairorder(status,orderId)){
                System.out.println("更新报修单状态成功！");
            } else {
                System.out.println("更新报修单状态失败，请稍后再试！");
            }


    }

    public static void deleteRepairorder(Scanner scanner, RepairorderService repairorderservice) {
        System.out.println("请输入需要删除的报修单号：");
        long orderId = 0L;

        int i = 0;
        while(i ==0){
            try {
                orderId = scanner.nextLong();
                scanner.nextLine();
            }catch (Exception e) {
                System.out.println("输入无效，请重新输入！");
                scanner.nextLine();
                continue;
            }
            if(repairorderservice.deleteRepairorder(orderId)){
                System.out.println("报修单号不存在，请重新输入：");
            }else{
                i = 1;
            }
        }

            if(repairorderservice.deleteRepairorder(orderId)){
                System.out.println("删除报修单成功！");
            } else {
                System.out.println("删除失败，请稍后再试");
            }
    }

}



