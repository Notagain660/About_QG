package com.example.demo.web;

import com.example.demo.entity.LoginreturnData;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.LoginResult;
import com.example.demo.utils.MapperResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
public class Controller {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserService userService;

    @GetMapping
    public String trial(){
        return "Hello World";
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();

        // 调用第一周写的 register 方法
        boolean success = userService.register(
                user.getRole(),
                user.getId(),
                user.getName(),
                user.getPassword()
        );

        if (success) {
            result.put("code", 200);
            result.put("message", "注册成功");
        } else  {
            result.put("code", 400);
            result.put("message", "用户名已存在");
        }
        return result;
    }

    @PostMapping("/login")
    public MapperResult<LoginreturnData> login(@RequestBody User user) {

        LoginResult logintrial = userService.login(user.getId(), user.getPassword());

        if(logintrial.success()){
            String token = generateToken(user.getId());//生成token，目前还没配jwt
            LoginreturnData data = new LoginreturnData(token, logintrial.user());
            return MapperResult.success(data);
        } else {
            return MapperResult.error(logintrial.message());
        }
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();

        boolean x = userService.updater(user,user.getDormBuilding(),user.getRoomNumber(), user.getId() );
        if (x) {
            result.put("code", 200);
            result.put("message", "更新成功");
        } else {
            result.put("code", 400);
            result.put("message", "更新失败");
        }
        return result;
    }


}
