package com.gongbotao.vcash;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gongbotao.vcash.infrastructure.persistence.mapper")
public class VCashApplication {
    public static void main(String[] args) {
        SpringApplication.run(VCashApplication.class, args);
    }
}