package com.project.aiassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AiassistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiassistantApplication.class, args);
    }

}
