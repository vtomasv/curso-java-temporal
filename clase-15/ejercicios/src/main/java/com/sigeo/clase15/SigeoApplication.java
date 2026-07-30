package com.sigeo.clase15;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SigeoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SigeoApplication.class, args);
    }
}
