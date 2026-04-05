package com.project.aiassistant.service;


import com.project.aiassistant.dto.InterviewQuestion;
import com.project.aiassistant.dto.InterviewRequest;
import com.project.aiassistant.dto.InterviewResponse;
import com.project.aiassistant.exception.ApiException;
import com.project.aiassistant.integration.GroqApiClient;
import com.project.aiassistant.util.PromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewService {

    private final GroqApiClient groqApiClient;
    private final ObjectMapper objectMapper;
    /*private final PromptBuilder promptBuilder;
    private final GeminiApiClient geminiApiClient;*/

    @Cacheable(value = "interviewQuestions",
            key = "#request.jobRole + '-' + #request.experienceLevel + '-' + #request.numberOfQuestions"
    )
    public InterviewResponse generateInterviewResponse
            (InterviewRequest request) {
        log.info("generateInterviewResponse for the job role: {}, Experience: {}, No of question: {}",
                request.getJobRole(),
                request.getExperienceLevel(),
                request.getNumberOfQuestions());
        String prompt = PromptBuilder.buildInterviewPrompt(
                request.getJobRole(),
                request.getExperienceLevel(),
                request.getNumberOfQuestions());
        String rawResponse1 = groqApiClient.sendPrompt(prompt);
            List<InterviewQuestion> interviewQuestionList =  parceResponce(rawResponse1);
       /* String rawResponse = geminiApiClient.sendPrompt(prompt);
        List<InterviewQuestion> interviewQuestionList =  parceResponce(rawResponse);*/



        return InterviewResponse.builder()
                .experienceLevel(request.getExperienceLevel())
                .jobRole(request.getJobRole())
                .questions(interviewQuestionList)
                .build();
    }

    private List<InterviewQuestion> parceResponce(String rawResponse) {
        try{
            return objectMapper.readValue(
                    rawResponse, new TypeReference<>() {
                    });
        } catch (Exception e) {
            log.error("Error while parsing response: {}", e.getMessage());
            throw new ApiException("Error while parsing response", 500);
        }
    }
}
