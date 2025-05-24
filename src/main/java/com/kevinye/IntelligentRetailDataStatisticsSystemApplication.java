package com.kevinye;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IntelligentRetailDataStatisticsSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelligentRetailDataStatisticsSystemApplication.class, args);
    }

}
