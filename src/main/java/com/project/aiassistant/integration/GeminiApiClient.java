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

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class GeminiApiClient {

    private final WebClient geminiWebClient;

    @Value("${gemini.api.max-tokens}")
    private int  maxToken;
    @Value("${gemini.api.model}")
    private String apiModel;

    public String sendPrompt(String prompt) {

        log.debug("The gemini mpdel: {} And prompt length : {}",apiModel, prompt.length());
        Map<String,Object> requestBody= Map.of(
                "contents",
                Map.of("parts",
                        List.of(
                                Map.of("text", prompt ))),
                "generationConfig",Map.of("maxOutputTokens",maxToken)
        );
        try{
            JsonNode rawJson =
                    geminiWebClient
                            .post()
                            .bodyValue(requestBody)
                            .retrieve()
                            .bodyToMono(JsonNode.class)
                            .block();
            log.debug("Raw Gemini response: {}", rawJson);
            System.out.println(rawJson);

            assert rawJson != null;
            return rawJson
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text").asString();

        }   catch (WebClientResponseException e) {
        log.error("Full Gemini error body: {}", e.getResponseBodyAsString());
        int status = e.getStatusCode().value();
        if (status == 429) throw new RateLimitException();
        else if (status == 401) throw new ApiException("Invalid API key", status);
        else throw new ApiException("AI API call failed", status);

    } catch (WebClientRequestException e) {
        log.error("Network error reaching Gemini API: {}", e.getMessage());
        throw new ApiException("AI service unreachable", 503);

    } catch (Exception e) {
        log.error("Unexpected error parsing Gemini response: {}", e.getMessage());
        throw new ApiException("Failed to process AI response", 500);
    }
    }






}
