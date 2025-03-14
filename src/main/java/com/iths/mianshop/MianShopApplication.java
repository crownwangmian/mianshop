package com.iths.mianshop;

import com.iths.mianshop.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
public class MianShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MianShopApplication.class, args);
    }

}
