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
import com.dorm.utils.LoginResult;
import com.dorm.utils.ScanfNumberResult;
import com.dorm.utils.ExceptionDeal;
import org.jspecify.annotations.NonNull;

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

          int menuChoice = ScanfNumberResult.getInt(scanner, i -> i >= 1 && i <= 3);

          switch (menuChoice) {
              case 1 -> {
                  clear();
                  currentUser = login(scanner, userservice);
                  clear();

                  if (currentUser != null) {
                      if (currentUser.getRole() == Role.ADMIN) {
                          administerMenu(currentUser, scanner, repairorderservice, userservice);
                      } else {
                          studentMenu(currentUser, scanner, userservice, repairorderservice);
                      }
                  }

              }

              case 2 -> {
                  clear();
                  ExceptionDeal.deal(() ->  Register(scanner, userservice));
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

            int roleNumber = ScanfNumberResult.getInt(scanner, n -> n == 1 || n == 2);

            role = Role.fromChoice(roleNumber);

            if(role == Role.ADMIN) {
                System.out.println("请输入工号（前四位是0025）：");
            }else if(role == Role.STUDENT) {
                System.out.println("请输入学号（前四位是3225/3125/5124）：");
            }

            id = ScanfNumberResult.getString(scanner,n -> n.matches("^(3125|3225|5124|0025)\\d{6}$" ));

            System.out.println("请输入姓名：");
            name = scanner.nextLine();

            System.out.println("请输入密码：");
            password = scanner.nextLine();//待优化：判断密码强度和加密

        password = getString(scanner, password);

        if(userservice.register(role, id, name, password)) {
                System.out.println("注册成功！请返回主界面登录。");
            }
            else {
                System.out.println("注册失败，请返回主界面重新注册。");
            }
    }

    @NonNull
    private static String getString(Scanner scanner, String password) {
        System.out.println("请确认密码：");
        String password1 = scanner.nextLine();

        while (!password.equals(password1)) {
            System.out.println("请重新输入密码：");
            password = scanner.nextLine();
            System.out.println("请确认密码：");
            password1 = scanner.nextLine();
        }
        return password;
    }

    public static User login(Scanner scanner, UserService userservice) {

        String id, password;


        System.out.println("===== 用户登录 =====");
        System.out.println("请输入账号：");
        id = scanner.nextLine();
        System.out.println("请输入密码：");
        password = scanner.nextLine();

            LoginResult loginresult = userservice.login(id, password);

            if(loginresult.success()) {
                System.out.println("登录成功！角色：" + loginresult.user().getRole().getRoleName());
                return loginresult.user();
            }else{
                System.out.println("登录失败，" + loginresult.message());
                return null;
            }

    }

    public static void studentMenu(User currentUser, Scanner scanner, UserService userservice,
                                   RepairorderService repairorderservice) {

    while(true){
        List<String> list = List.of("===== 学生菜单 =====",
                "1. 绑定/修改宿舍", "2. 查看个人信息", "3. 创建报修单", "4. 查看我的报修记录", "5. 取消报修单", "6. 修改密码",
                "7. 退出");
        for (var s : list) {
            System.out.println(s);
        }
        System.out.println("请选择操作（输入1-7）");

        int menuChoice = ScanfNumberResult.getInt(scanner, i -> i >= 1 && i <= 7);

        switch (menuChoice) {
            case 1 -> {
                clear();
                ExceptionDeal.deal(() ->dormFix(currentUser, scanner, userservice));
            }
            case 2 -> {
                clear();
                ExceptionDeal.deal(() -> printInformation(currentUser));
            }

            case 3 -> {
                clear();
                ExceptionDeal.deal(() -> newRepairorder(currentUser, scanner, repairorderservice));
            }

            case 4 -> {
                clear();
                ExceptionDeal.deal(() -> checkMyRepairorder(currentUser, scanner,repairorderservice));
            }

            case 5 -> {
                clear();
                ExceptionDeal.deal(() -> cancelRepairorder(scanner,repairorderservice));
            }

            case 6 -> {
                clear();
                ExceptionDeal.deal(() -> passwordChange(currentUser, scanner, userservice));
            }

            case 7 -> {
                clear();
                System.out.println("感谢您的使用！");
                return;
            }
            default -> {
                clear();
                System.out.println("输入无效，请重新输入：");
            }
        }
    }

 }

    public static void dormFix(User currentUser, Scanner scanner, UserService userservice) {

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
        System.out.println("身份：" + currentUser.getRole().getRoleName());
        System.out.println("宿舍号：" + currentUser.getDormBuilding() + currentUser.getRoomNumber());
 }

    public static void passwordChange(User currentUser,Scanner scanner, UserService userservice){

        String dormBuilding = currentUser.getDormBuilding(), roomNumber = currentUser.getRoomNumber();

        System.out.println("请输入新密码：");
        String password = scanner.nextLine();
        password = getString(scanner, password);
        if(userservice.updater(currentUser, dormBuilding, roomNumber, password)){
            currentUser.setPassword(password);
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

        System.out.printf("%-12s %-11s %-14s %-7s %-20s %-20s %-20s\n", "学号", "报修单号", "设备类型", "状态", "创建时间", "最新提交时间", "完成时间");

        for (Repairorder repairorder : r) {
            var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String deviceType = repairorder.getDeviceType() == null ? "-" : repairorder.getDeviceType();
            String status = repairorder.getStatus().getText();
            String createTime = repairorder.getCreateTime()  == null ? "-" : sdf.format(repairorder.getCreateTime());
            String updateTime = repairorder.getUpdateTime()  == null ? "-" : sdf.format(repairorder.getUpdateTime());
            String processTime = repairorder.getProcessTime() == null ? "-" : sdf.format(repairorder.getProcessTime());

            System.out.printf("%-12s %-11d %-14s %-7s %-20s %-20s %-20s\n", repairorder.getId(), repairorder.getOrderId(), deviceType, status, createTime, updateTime, processTime);

        }

        if(r.isEmpty()) {
            System.out.println("请按任意键返回：");
            scanner.nextLine();
            clear();
        }else {
            System.out.println("输入报修单号以查看详情······");

            long orderId = ScanfNumberResult.getValidLong(scanner, "报修单号不存在，请重新输入：", repairorderservice::loginRepairorder);

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

            System.out.printf("学号/工号：" + repairorder.getId() + "\n");
            System.out.printf("报修单号：" + repairorder.getOrderId() + "\n");
            System.out.printf("宿舍号：" + dormBuilding + roomNumber + "\n");
            System.out.printf("联系方式（电话号码：" + phoneNumber + "\n");
            System.out.printf("设备类型：" + deviceType + "\n");
            System.out.printf("状态：" + repairorder.getStatus().getText() + "\n");
            System.out.printf("问题描述：" + descriptionText + "\n");
            System.out.printf("创建时间：" + createTime + "\n");
            System.out.printf("最新提交时间：" + updateTime + "\n");
            System.out.printf("处理完成时间：" + processTime + "\n");
            System.out.printf("维修评价：" + comments + "\n");

            if(repairorder.getStatus() == RepairStatus.COMPLETED) {
                System.out.println("请输入评价：");
                comments = scanner.nextLine();
                if(repairorderservice.updateRepairorder(repairorder.getStatus(), orderId, comments)){
                    System.out.println("上传报修评价成功！");
                } else {
                    System.out.println("上传报修评价失败，请稍后再试！");
                }
            }

        }

        System.out.println("请按任意键返回：");
        scanner.nextLine();
        clear();

    }

    public static void cancelRepairorder(Scanner scanner, RepairorderService repairorderservice) {

        System.out.println("请输入需要取消的报修单号：");
        Long orderId = ScanfNumberResult.getValidLong(scanner, "报修单号不存在，请重新输入：", repairorderservice::loginRepairorder);
        String comments = scanner.nextLine();

        RepairStatus status = RepairStatus.CANCELED;

            if(repairorderservice.updateRepairorder(status,orderId, comments)){
                System.out.println("取消报修单成功！");
            } else {
                System.out.println("取消报修单失败，请稍后再试！");
            }



    }

    public static void administerMenu(User currentUser, Scanner scanner,
                                      RepairorderService repairorderservice,
                                      UserService userservice) {

    while(true){
        List<String> list = List.of("===== 管理员菜单 =====",
                "1. 查看所有报修单", "2. 查看报修单详情", "3. 更新报修单状态", "4. 删除报修单", "5. 修改密码", "6. 退出");
        for (String s : list) {
            System.out.println(s);
        }
        System.out.println("请选择操作（输入1-6）");

        int menuChoice = ScanfNumberResult.getInt(scanner, n -> n >= 1 && n <= 6);

        switch (menuChoice) {
            case 1 -> {
                clear();
                ExceptionDeal.deal(() -> adCheckAll(scanner, repairorderservice));
            }
            case 2 -> {
                clear();
                ExceptionDeal.deal(() -> checkCertain(scanner,repairorderservice));
            }

            case 3 -> {
                clear();
                ExceptionDeal.deal(() -> adUpdatestatus(scanner, repairorderservice));
            }

            case 4 -> {
                clear();
                ExceptionDeal.deal(() -> deleteRepairorder(scanner,repairorderservice));
            }

            case 5 -> {
                clear();
                ExceptionDeal.deal(() -> passwordChange(currentUser, scanner, userservice));
            }

            case 6 -> {
                clear();
                System.out.println("感谢您的使用！");
                return;
            }
            default -> {
                clear();
                System.out.println("输入无效，请重新输入：");
            }
        }
    }


    }

    public static void adCheckAll(Scanner scanner, RepairorderService repairorderservice) {

        List<String> checkMenu = List.of("1. 查看所有报修单",
                "2. 查看未处理的报修单", "3. 查看正在处理的报修单", "4. 查看处理完成的报修单", "5. 查看已取消的报修单", "6. 退出",
                "请选择报修单查看方式（如查看所有报修单，请按1：）");
        for(String s : checkMenu) {
            System.out.println(s);
        }

        int wayChoice = ScanfNumberResult.getInt(scanner, n -> n >= 1 && n <= 6);

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
                    List<Repairorder> r = repairorderservice.checkAllRepairorderByStatus(RepairStatus.PENDING);
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
                    List<Repairorder> r = repairorderservice.checkAllRepairorderByStatus(RepairStatus.PROCESSING);
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
                    List<Repairorder> r = repairorderservice.checkAllRepairorderByStatus(RepairStatus.COMPLETED);
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

                case 5 -> {
                    clear();
                    List<Repairorder> r = repairorderservice.checkAllRepairorderByStatus(RepairStatus.CANCELED);
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

                case 6 -> {
                    clear();
                    System.out.println("感谢您的使用！");
                }

                default -> {
                    clear();
                    System.out.println("输入无效，请重新输入：");
                }
            }



    }

    public static void checkCertain(Scanner scanner, RepairorderService repairorderservice) {

        System.out.println("输入报修单号以查看详情······");
        Long orderId = ScanfNumberResult.getValidLong(scanner, "报修单号不存在，请重新输入：", repairorderservice::loginRepairorder);

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

            System.out.printf("学号/工号：" + repairorder.getId() + "\n");
            System.out.printf("报修单号：" + repairorder.getOrderId() + "\n");
            System.out.printf("宿舍号：" + dormBuilding + roomNumber + "\n");
            System.out.printf("联系方式（电话号码：" + phoneNumber + "\n");
            System.out.printf("设备类型：" + deviceType + "\n");
            System.out.printf("状态：" + status + "\n");
            System.out.printf("问题描述：" + descriptionText + "\n");
            System.out.printf("创建时间：" + createTime + "\n");
            System.out.printf("最新提交时间：" + updateTime + "\n");
            System.out.printf("处理完成时间：" + processTime + "\n");
            System.out.println("优先级：" + prorityLevel + "\n");
            System.out.printf("维修评价：" + comments + "\n");

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

        System.out.println("=====更新状态=====");
        System.out.println("请输入需要更新状态的报修单号");
        Long orderId = ScanfNumberResult.getValidLong(scanner, "报修单号不存在，请重新输入：", repairorderservice::loginRepairorder);

        System.out.println("请输入状态（正在处理请按1，处理完成请按2）：");
            int sTatus = ScanfNumberResult.getInt(scanner, n -> n == 1 || n == 2);

            RepairStatus status = RepairStatus.fromChoice(sTatus);
            String comments = scanner.nextLine();
            if(repairorderservice.updateRepairorder(status, orderId, comments)){
                System.out.println("更新报修单状态成功！");
            } else {
                System.out.println("更新报修单状态失败，请稍后再试！");
            }


    }

    public static void deleteRepairorder(Scanner scanner, RepairorderService repairorderservice) {
        System.out.println("请输入需要删除的报修单号：");

        Long orderId = ScanfNumberResult.getValidLong(scanner, "报修单号不存在，请重新输入：", repairorderservice::loginRepairorder);

            if(repairorderservice.deleteRepairorder(orderId)){
                System.out.println("删除报修单成功！");
            } else {
                System.out.println("删除失败，请稍后再试");
            }
    }

}



