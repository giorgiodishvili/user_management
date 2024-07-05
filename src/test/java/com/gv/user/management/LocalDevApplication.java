package com.gv.user.management;

import com.gv.user.management.config.ContainersConfig;
import org.springframework.boot.SpringApplication;

public class LocalDevApplication {
    public static void main(String[] args) {
        SpringApplication.from(UserManagementApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}
