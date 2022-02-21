package com.springboot.dynamodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SpringbootAndDynamodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootAndDynamodbApplication.class, args);
        log.info("Springboot and dynamodb application started successfully.");
    }
}
