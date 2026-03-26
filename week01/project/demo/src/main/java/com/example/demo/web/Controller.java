package com.example.demo.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWTUtil;

import com.example.demo.entity.LoginreturnData;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.RepairorderService;
import com.example.demo.service.UserService;
import com.example.demo.utils.LoginResult;
import com.example.demo.utils.MapperResult;
import com.example.demo.verifier.UserContext;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Controller {
    private final DataSource dataSource;
    private final UserService userService;
    private final UserMapper userMapper;
    private final RepairorderService repairorderService;

    private String secretkey;
    @Value("${jwt.secretkey}")//value注解只能用在Spring管理的Bean的字段或方法参数上

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @GetMapping
    public String trial(){
        log.info("Ciallo");
        return "Hello World";
    }


    @PostMapping("/register")
    public MapperResult<Object> register(@RequestBody User user) {

        boolean success = userService.register(
                user.getRole(),
                user.getId(),
                user.getName(),
                user.getPassword()
        );

        if (success) {
            return new MapperResult<>(200, "注册成功", null);
        } else  {
            return MapperResult.error("注册失败");
        }

    }


    @PostMapping("/login")
    public MapperResult<LoginreturnData> login(@RequestBody Map<String, String> loginInfo) {

        String id = loginInfo.get("id");
        String password = loginInfo.get("password");
        LoginResult logintrial = userService.login(id, password);

        if(logintrial.success()){
            User user = logintrial.user();
            Map<String, Object> map = new HashMap<>();
            map.put("issue_time", DateUtil.now());
            map.put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
            //过期时间和统一身份认证一致
            map.put("id", user.getId());
            map.put("role", user.getRole());

            String token =  JWTUtil.createToken(map, secretkey.getBytes(StandardCharsets.UTF_8));
            //统一使用UTF-8 字符集防止不同平台默认字符集不同导致的不匹配
            LoginreturnData data = new LoginreturnData(token, user);//把token和user一起放进data

            return MapperResult.success("登录成功", data);
        } else {
            return MapperResult.error(logintrial.message());//user是null，message来判断不同原因导致的失败
        }
    }


    @PutMapping("/update")
    public MapperResult<Object> update(@RequestBody User user) {

        boolean x = userService.updater(user,user.getDormBuilding(),user.getRoomNumber(), user.getId() );
        if (x) {
            return new MapperResult<>(200, "更新成功", null);
        } else {
            return MapperResult.error("更新失败");
        }

    }


    @GetMapping("/checkme")
    public MapperResult<User> checkme() {
        User currentUser = userMapper.selectById(UserContext.getCurrentUser().getUserId());
        return  MapperResult.success("访问个人信息成功", currentUser);
    }

    @PostMapping("/callfix")
    public MapperResult<Object> callfix(@RequestBody Map<String, Object> params) {

        String phoneNumber = params.getOrDefault("phoneNumber", "").toString();
        String deviceType = params.getOrDefault("deviceType", "").toString();
        String descriptionText = params.getOrDefault("descriptionText", "").toString();

        if(phoneNumber.isEmpty() || descriptionText.isEmpty() || deviceType.isEmpty()){
            return MapperResult.error("必填项不能为空");
        }

        boolean x = repairorderService.newRepairorder(UserContext.getCurrentUser().getUserId(),
                phoneNumber, deviceType, descriptionText);
        if (x) {
            User user = userMapper.selectById(UserContext.getCurrentUser().getUserId());
            return new MapperResult<>(200, "创建报修单成功", user);
        } else {
            return MapperResult.error("创建报修单失败");
        }

    }
}


