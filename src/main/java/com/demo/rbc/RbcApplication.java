package com.demo.rbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class RbcApplication {
    public static void main(String[] args) {
        SpringApplication.run(RbcApplication.class, args);
    }
}
