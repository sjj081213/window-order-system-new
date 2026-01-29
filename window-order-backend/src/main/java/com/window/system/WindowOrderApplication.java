package com.window.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.window.system.mapper")
public class WindowOrderApplication {


    public static void main(String[] args) {
        SpringApplication.run(WindowOrderApplication.class, args);
    }

}
