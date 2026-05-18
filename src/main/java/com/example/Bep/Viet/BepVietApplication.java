package com.example.Bep.Viet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = { org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class })
public class BepVietApplication {
    public static void main(String[] args) {
        SpringApplication.run(BepVietApplication.class, args);
    }
}
