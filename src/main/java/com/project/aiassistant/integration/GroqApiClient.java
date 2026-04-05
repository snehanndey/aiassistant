package com.project.aiassistant.integration;


import com.project.aiassistant.exception.ApiException;
import com.project.aiassistant.exception.RateLimitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Slf4j              // Lombok: gives us a 'log' variable backed by SLF4J
@Component          // Registers this as a Spring Bean
@RequiredArgsConstructor // Lombok: generates constructor for final fields
public class GroqApiClient {

    private final WebClient groqWebClient; // Injected from GroqConfig
    private final ObjectMapper objectMapper;

    @Value("${groq.api.model}")
    private String model;

    @Value("${groq.api.max-tokens}")
    private int maxTokens;


    public String sendPrompt(String prompt) {

        log.debug("Sending prompt to Groq. Model: {}, Prompt length: {}",
                model, prompt.length());

        // This is the exact JSON structure Groq (OpenAI-compatible) expects
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "max_tokens", maxTokens,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        try {
            String rawJson = groqWebClient
                    .post()
                    .uri("/chat/completions")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println(rawJson);

            JsonNode response = objectMapper.readTree(rawJson);

            String content = response
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            log.debug("Received response from Groq. Length: {}", content.length());
            return content;

        } catch (WebClientResponseException e) {
            log.error("Full Groq error body: {}", e.getResponseBodyAsString());
            int status = e.getStatusCode().value();
            if (status == 429) throw new RateLimitException();
            else if (status == 401) throw new ApiException("Invalid API key", status);
            else throw new ApiException("AI API call failed", status);

        } catch (WebClientRequestException e) {
            log.error("Network error reaching Groq API: {}", e.getMessage());
            throw new ApiException("AI service unreachable", 503);

        } catch (Exception e) {
            log.error("Unexpected error parsing Groq response: {}", e.getMessage());
            throw new ApiException("Failed to process AI response", 500);
        }
    }
}