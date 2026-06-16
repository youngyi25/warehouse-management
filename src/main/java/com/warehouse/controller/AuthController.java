package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.get("username"),
                        loginRequest.get("password")
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        Map<String, Object> result = new HashMap<>();
        result.put("token", jwt);
        result.put("tokenType", "Bearer");

        return Result.success(result);
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", authentication.getName());
        return Result.success(userInfo);
    }

    @PostMapping("/reset-admin-password")
    @GetMapping("/reset-admin-password")
    public Result<String> resetAdminPassword() {
        String newPassword = "admin123";
        String encodedPassword = passwordEncoder.encode(newPassword);
        
        String sql = "UPDATE sys_user SET password = ? WHERE username = 'admin'";
        jdbcTemplate.update(sql, encodedPassword);
        
        System.out.println("===========================================");
        System.out.println("✓ Admin 用户密码已重置为: " + newPassword);
        System.out.println("加密后的哈希值: " + encodedPassword);
        System.out.println("===========================================");
        System.out.println("现在可以使用 admin / admin123 登录了！");
        System.out.println("===========================================");
        
        return Result.success("密码已重置为: " + newPassword);
    }
}