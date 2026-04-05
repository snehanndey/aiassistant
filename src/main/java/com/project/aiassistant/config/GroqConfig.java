package com.project.aiassistant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration // Tells Spring: this class produces Beans
public class GroqConfig {

    @Value("${groq.api.key}")       // Reads from application.properties
    private String apiKey;          // Which reads from GROQ_API_KEY env var

    @Value("${groq.api.base-url}")
    private String baseUrl;

    @Bean // This method's return value is registered as a Spring Bean
    public WebClient groqWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}