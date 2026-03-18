package com.dorm.main;

import java.util.Scanner;
import java.util.List;

import com.dorm.entity.User;
import com.dorm.service.UserService;

public class Main {
    public static void main(String[] args) {

        var scanner = new Scanner(System.in);
        var userservice = new UserService();
        User currentUser;

      while(true){
          System.out.println("===========================");
          System.out.println("\uD83C\uDFE0 宿舍报修管理系统");
          System.out.println("===========================");
          System.out.println("1.登录");
          System.out.println("2.注册");
          System.out.println("3.退出");
          System.out.println("请选择操作（输入 1-3 ）");//list 优化

          int menuChoice;
            try {
                menuChoice = scanner.nextInt();
            } catch (Exception e) {
                // 如果输入不是整数，清除输入并重新循环
                scanner.nextLine(); // 清除错误输入
                System.out.println("输入无效，请输入数字。");
                continue;//(?)
            }
          switch (menuChoice) {
              case 1 -> {
                  scanner.nextLine();
                  clear();
                  currentUser = login(scanner, userservice);
                  clear();
                  if(currentUser != null){
                      studentMenu(currentUser, scanner, userservice);
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

        String role = null, id, password;


            System.out.println("===== 用户注册 =====");
            System.out.println("请选择角色（1-学生，2-维修人员）：");

            int roleNumber;int i = 0;

            while(i == 0){
                try {
                    roleNumber = scanner.nextInt();
                }  catch (Exception e) {
                scanner.nextLine();
                System.out.println("输入无效，请重新输入：");
                continue;
            }
                if(roleNumber != 1 && roleNumber != 2) {
                    scanner.nextLine();
                    System.out.println("输入无效，请重新输入：");
                } else {
                    role = (roleNumber == 1) ? "student" : "administer";
                    i = 1;
                }
            }

            scanner.nextLine();
            System.out.println("请输入学号（前缀3125或3225）：");
            id = scanner.nextLine();//待优化：学号或工号格式校验（字符串操作）

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

            if(userservice.register(role, id, password)) {
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
        user = userservice.login(id, password);//这里需要根据学号前几位校验
        if(user != null) {
            System.out.println("登录成功！角色：" + user.getRole());
            return user;
        }else{
            //System.out.println("(账号或密码错误。");
            System.out.println("登录失败！请重新登录。");
            return null;
        }//还能优化一下是用户不存在还是账号或密码错误
    }

    public static void studentMenu(User currentUser, Scanner scanner, UserService userservice){


        int i = 0, menuChoice = 0;

        List<String> list = List.of("===== 学生菜单 =====", "1. 绑定/修改宿舍", "2. 查看个人信息", "3. 创建报修单", "4. 查看我的报修记录", "5. 取消报修单", "6. 修改密码", "7. 退出");
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
                currentUser = dormFix(currentUser, scanner, userservice);//话说菜单功能返回上一步怎么做啊。
            }
               case 2 -> {
                clear();
                printInformation(currentUser);
            }
            case 6 -> {
                clear();
                currentUser = passwordChange(currentUser, scanner, userservice);
            }
        }
 }

    public static User dormFix(User currentUser, Scanner scanner, UserService userservice) {
        //宿舍绑定细化功能可以是进入之后看到绑定的宿舍（与否）然后再可选是否修改/绑定/退出，并且宿舍号输入的检验（异常）可以包括宿舍楼和宿舍号，0318先写基础功能
        //if条件判断成功还是失败（参考注册和登录）
        String dormBuilding, roomNumber, password = currentUser.getPassword();
        boolean x;

        if(currentUser.getDormBuilding() == null) {
            System.out.println("宿舍暂未绑定，已进入绑定程序······");
            x = true;
        }else {
            System.out.println("宿舍号已存在，已进入修改程序······");
            x = false;
        }
        System.out.println("请输入宿舍楼号：（示例：西六）");
        dormBuilding = scanner.nextLine();
        System.out.println("请输入宿舍房间号：（示例：304）");
        roomNumber = scanner.nextLine();
        currentUser = userservice.updater(currentUser, dormBuilding, roomNumber, password);
        if(x){
            System.out.println("宿舍号绑定完毕！");
        }else{
            System.out.println("宿舍号修改完毕！");
        }
        return currentUser;
 }

    public static void printInformation(User currentUser){
        System.out.println("=====用户基本信息=====");
        System.out.println("学号/工号：" + currentUser.getId());
        System.out.println("姓名：" +  currentUser.getName());
        System.out.println("身份：" + currentUser.getRole());
        System.out.println("宿舍号：" + currentUser.getDormBuilding() + currentUser.getRoomNumber());
 }

    public static User passwordChange(User currentUser,Scanner scanner, UserService userservice){
        //if条件判断成功还是失败（参考登录）

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
        currentUser = userservice.updater(currentUser, dormBuilding, roomNumber, password);
        return  currentUser;
 }

}



