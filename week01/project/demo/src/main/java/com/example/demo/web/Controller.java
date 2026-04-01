package com.example.demo.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWTUtil;

import com.example.demo.entity.LoginreturnData;
import com.example.demo.entity.Repairorder;
import com.example.demo.entity.User;
import com.example.demo.enums.RepairStatus;
import com.example.demo.enums.Role;
import com.example.demo.mapper.RepairorderMapper;
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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Controller {
    private final UserService userService;
    private final UserMapper userMapper;
    private final RepairorderService repairorderService;
    private final RepairorderMapper repairorderMapper;

    @Value("${jwt.secretkey}")//value注解只能用在Spring管理的Bean的字段或方法参数上，且必须直接放在字段上否则无法注入
    private String secretkey;

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
            return MapperResult.error(400,"注册失败");
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
            return MapperResult.error(403, logintrial.message());//user是null，message来判断不同原因导致的失败
        }
    }


    @PutMapping("/user/update")
    public MapperResult<Object> update(@RequestBody Map<String, String> params) {
        String dormBuilding =  params.get("dormBuilding");
        String roomNumber = params.get("roomNumber");
        User user = userMapper.selectById(UserContext.getCurrentUser().getUserId());
        boolean x = userService.updater(user, dormBuilding,roomNumber, user.getPassword());
        if (x) {
            return new MapperResult<>(200, "更新成功", null);
        } else {
            return MapperResult.error(400,"更新失败");
        }
    }


    @PutMapping("/changepassword")
    public MapperResult<Object> changePassword(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");//应该是检验是不是用户本人操作
        String newPassword = params.get("newPassword");
        if(oldPassword.isEmpty() || newPassword.isEmpty()){
            return MapperResult.error(400,"新旧密码不能为空");
        } else if(oldPassword.equals(newPassword)){
            return MapperResult.error(400,"两次密码不能为同一个");
        } else {
            User user = userMapper.selectById(UserContext.getCurrentUser().getUserId());
            if(!user.getPassword().equals(oldPassword)){
                return MapperResult.error(400,"旧密码错误");
            } else {
                boolean x = userService.updater(user, user.getDormBuilding(), user.getRoomNumber(), newPassword);
                return x ? MapperResult.success("修改成功", null) : MapperResult.error(400,"修改失败");
            }
        }
    }


    @GetMapping("/user/checkme")
    public MapperResult<User> checkme() {
        User currentUser = userMapper.selectById(UserContext.getCurrentUser().getUserId());
        return  MapperResult.success("访问个人信息成功", currentUser);
        //因为直接从线程获得的user所以不用校验
    }


    @PostMapping("/user/callfix")
    public MapperResult<Long> callfix(@RequestBody Map<String, Object> params) {

        String phoneNumber = params.getOrDefault("phoneNumber", "").toString();
        String deviceType = params.getOrDefault("deviceType", "").toString();
        String descriptionText = params.getOrDefault("descriptionText", "").toString();

        if(phoneNumber.isEmpty() || descriptionText.isEmpty() || deviceType.isEmpty()){
            return MapperResult.error(400,"必填项不能为空");
        }

        long orderId = repairorderService.newRepairorder(UserContext.getCurrentUser().getUserId(),
                phoneNumber, deviceType, descriptionText);
        if (orderId != 0) {

            return new MapperResult<>(200, "创建报修单成功", orderId);
        } else {
            return MapperResult.error(400,"创建报修单失败");
        }
    }


    @GetMapping("/user/myrepairorder")
    public MapperResult<List<Repairorder>> myrepairorder() {
        List<Repairorder> r = repairorderMapper.selectByUserId(UserContext.getCurrentUser().getUserId());
        return MapperResult.success("访问个人所有表单成功", r);
    }


    @GetMapping("/thisrepairorder/{orderId}")
    public MapperResult<Repairorder> thisrepairorder(@PathVariable long orderId) {
        Repairorder r = repairorderMapper.selectByOrderId(orderId);//报修单
        User user = userMapper.selectById(UserContext.getCurrentUser().getUserId());
        //当前用户

        if(r == null){
            return MapperResult.error(400, "报修单不存在");
        } else if(!user.getRole().equals(Role.ADMIN)  && !r.getId().equals(user.getId()) ){
            //当学生权限时，校验报修单上的id和当前用户id是否一致
                return MapperResult.error(400,"非本人报修单");
        }
        return MapperResult.success("访问单个表单成功", r);
    }


    @PutMapping("/user/{orderId}/comments")
    public MapperResult<Object> comments(@PathVariable long orderId, @RequestBody Map<String, Object> params) {
        String comments =  params.getOrDefault("comments", "").toString();
        Repairorder r = repairorderMapper.selectByOrderId(orderId);

        if(r == null){
            return MapperResult.error(400, "报修单不存在");
        } else if(r.getStatus() != RepairStatus.COMPLETED){
            return MapperResult.error(400, "维修未完成");
        }else if (!r.getId().equals(UserContext.getCurrentUser().getUserId())) {
            return MapperResult.error(400,"非本人报修单");
        } else {
            boolean x = repairorderService.updateRepairorder(r.getStatus(), orderId, comments);
            return x ? MapperResult.success("更新成功", null)
                    : MapperResult.error(400,"更新失败");
        }
    }


    @PutMapping("/{orderId}/cancel")
    public MapperResult<Object> cancel(@PathVariable long orderId) {
        String id = repairorderMapper.selectByOrderId(orderId).getId();
        String id1 = UserContext.getCurrentUser().getUserId();
        Repairorder r = repairorderMapper.selectByOrderId(orderId);
        if(r == null){
            return MapperResult.error(400,"报修单不存在");
        } else if(r.getStatus() != RepairStatus.PENDING){
            return MapperResult.error(400,"报修正在处理");
        } else if(UserContext.getCurrentUser().getRole().equals("STUDENT") && !id.equals(id1)){
            return MapperResult.error(400,"非本人报修单");
        } else  {
            boolean x = repairorderService.updateRepairorder(RepairStatus.CANCELED, orderId, r.getComments());
            return x ? MapperResult.success("取消成功", null) :
                    MapperResult.error(400,"取消失败");
        }
    }


    @GetMapping("/administer/select")
    public MapperResult<List<Repairorder>> select(@RequestParam(required = false) String status) {
        String role = UserContext.getCurrentUser().getRole();
        RepairStatus rr;
        List<Repairorder> r;

        if (!"ADMIN".equals(role)) {
            return MapperResult.error(400,"无权限访问");
        }

        if(status == null || status.isBlank()){//blank方法把空字符串的情况也包括了（覆盖empty）
            r =  repairorderMapper.selectAll();
        }else {
            rr = RepairStatus.valueOf(status.toUpperCase()); //枚举名全是大写

            r =  repairorderMapper.selectByStatus(rr);
        }
        return MapperResult.success("访问成功", r);
    }



    @PutMapping("/{orderId}/restatus")
    public MapperResult<Object> reStatus(@PathVariable long orderId, @RequestParam String status) {
        //前端传的参数基本是字符串
        Repairorder r = repairorderMapper.selectByOrderId(orderId);
        RepairStatus sTatus;
        if(r == null){
            return MapperResult.error(400,"报修单不存在");
        } else if(!"ADMIN".equals(UserContext.getCurrentUser().getRole())){
            return MapperResult.error(400,"无权限修改");
        } else {
            sTatus = RepairStatus.valueOf(status.toUpperCase());//前端要传英文参数

            boolean x = repairorderService.updateRepairorder(sTatus, orderId, r.getComments());
            return x ? MapperResult.success("修改状态成功", null) :
                    MapperResult.error(400,"修改状态失败");
        }
    }


    @PutMapping("/{orderId}/priority")
    public MapperResult<Object> priority(@PathVariable long orderId, @RequestBody  Map<String, Object> params) {
        String priority = (String) params.getOrDefault("priority", "");
        Repairorder r = repairorderMapper.selectByOrderId(orderId);

        if(priority.isBlank()){
            return MapperResult.error(400, "优先级不能为空");
        }
        boolean x;
        if(r == null){
            return MapperResult.error(400, "报修单不存在");
        } else if(!"ADMIN".equals(UserContext.getCurrentUser().getRole())){
            return MapperResult.error(400, "无权限设置优先级");
        } else {

            x = repairorderService.finishRepairorder(priority, orderId);

            return x ? MapperResult.success("设置优先级成功", null) :
                    MapperResult.error(400,"设置优先级失败");
        }
    }


    @DeleteMapping("/{orderId}/delete")
    public MapperResult<Object> delete(@PathVariable long orderId) {
        Repairorder r = repairorderMapper.selectByOrderId(orderId);
        boolean x;
        if(r == null){
            return MapperResult.error(400, "报修单不存在");
        } else if(!"ADMIN".equals(UserContext.getCurrentUser().getRole())){
            return MapperResult.error(400,"无权限删除");
        } else {
            x = repairorderService.deleteRepairorder(orderId);

            return x ? MapperResult.success("删除成功", null) :
                    MapperResult.error(400,"删除失败");
        }
    }
}


