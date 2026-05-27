package com.gongbotao.vcash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class VCashApplication {

    public static void main(String[] args) {
        SpringApplication.run(VCashApplication.class, args);
    }
}
