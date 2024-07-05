package com.gv.user.management;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@RequiredArgsConstructor
@SpringBootApplication
@EnableCaching
@EnableMethodSecurity(securedEnabled = true)
public class UserManagementApplication {

    public static void main(final String[] args) {
        SpringApplication.run(UserManagementApplication.class, args);
    }
}
