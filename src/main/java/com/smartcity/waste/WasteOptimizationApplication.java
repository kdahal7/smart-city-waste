package com.smartcity.waste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WasteOptimizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(WasteOptimizationApplication.class, args);
    }
}
