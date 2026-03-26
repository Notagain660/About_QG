package com.example.demo.verifier;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtHelper {

    @Value("${jwt.secretkey}")
    private String secretkey;

    public boolean validateToken(String token) {//验证签名，hutool的方法
        try {
            return JWTUtil.verify(token, secretkey.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return false;
        }
    }

    public UserContext.UserInfo getUserInfoFromToken(String token) {
        if (!validateToken(token)) {
            return null;
        }
        JWT jwt = JWTUtil.parseToken(token);//解析token
        String id = (String) jwt.getPayload("id");
        String role = (String) jwt.getPayload("role");//读出id和role

        UserContext.UserInfo userInfo = new UserContext.UserInfo(id,role);
        userInfo.setUserId(id);
        userInfo.setRole(role);
        return userInfo;
    }

    public boolean validateTokenTime(String token) {
        try {
            JWTValidator.of(token)
                    .validateDate(new Date())  // 验证签发时间、生效时间、过期时间是否有效
                    .validateAlgorithm(JWTSignerUtil.hs256(secretkey.getBytes(StandardCharsets.UTF_8)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
