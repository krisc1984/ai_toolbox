package com.aitoolbox;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.aitoolbox.mapper")
public class AiToolboxApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiToolboxApplication.class, args);
    }
}