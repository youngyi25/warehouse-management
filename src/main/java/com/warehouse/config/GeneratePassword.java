package com.warehouse.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "admin123";
        String hash = encoder.encode(password);

        System.out.println("\n===========================================");
        System.out.println("密码: " + password);
        System.out.println("bcrypt 哈希: " + hash);
        System.out.println("===========================================");
        System.out.println("\n请复制上面的哈希值，然后在MySQL中执行：");
        System.out.println("UPDATE sys_user SET password = '" + hash + "' WHERE username = 'admin';");
        System.out.println("===========================================\n");
    }
}
