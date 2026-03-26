package com.example.demo.verifier;

public class UserContext {
    private static final ThreadLocal<UserInfo> currentUser = new ThreadLocal<>();//新建线程

    public static void setCurrentUser(UserInfo user) {
        currentUser.set(user);
    }//线程和userinfo（简化版id和角色）一对一绑定

    public static UserInfo getCurrentUser() {
        return currentUser.get();
    }//从线程获取userinfo

    public static void clear() {
        currentUser.remove();
    }//清理线程数据，否则下一个人用了这个人的数据

    // 简单封装用户信息
    public static class UserInfo {//构造器
        private String userId;
        private String role;

        public UserInfo(String userId, String role) {
            this.userId = userId;
            this.role = role;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }


}
