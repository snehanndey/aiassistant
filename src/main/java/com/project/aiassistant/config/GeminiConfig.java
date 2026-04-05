package com.project.aiassistant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GeminiConfig {


    @Value("${gemini.api.key}")
    private String apiKey;
    @Value("${gemini.api.url}")
    private String apiUrl;
    @Value("${gemini.api.model}")
    private String apiModel;

    @Bean
    public WebClient geminiWebClient() {
        return WebClient.builder().
                baseUrl(apiUrl+apiModel+":generateContent")
                .defaultHeader("Content-Type","application/json")
                .defaultHeader("X-goog-api-key",apiKey).build();

    }
}
