package com.project.aiassistant.service;

import com.project.aiassistant.dto.InterviewQuestion;
import com.project.aiassistant.dto.InterviewRequest;
import com.project.aiassistant.dto.InterviewResponse;
import com.project.aiassistant.exception.ApiException;
import com.project.aiassistant.integration.GroqApiClient;
import com.project.aiassistant.util.PromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewService {

    private final GroqApiClient groqApiClient;
    private final ObjectMapper objectMapper;
    /*private final PromptBuilder promptBuilder;
    private final GeminiApiClient geminiApiClient;*/
    private final ResumeParserService resumeParserService;

    @Cacheable(value = "interviewQuestions",
            key = "#request.jobRole + '-' + #request.experienceLevel + '-' + #request.numberOfQuestions"
    )
    public InterviewResponse generateInterviewResponse(InterviewRequest request) {
        log.info("Generating interview questions for job role: {}, Experience: {}, Count: {}",
                request.getJobRole(), request.getExperienceLevel(), request.getNumberOfQuestions());

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
    public InterviewResponse generateQuestionsFromResume(
            MultipartFile resume, String jobRole, String experienceLevel, int numberOfQuestions) {
        try {
            log.info("Parsing resume and generating questions for job role: {}", jobRole);
            String resumeText = resumeParserService.parse(resume.getInputStream());

            String prompt = PromptBuilder.buildResumeInterviewPrompt(
                    jobRole,
                    experienceLevel,
                    numberOfQuestions,
                    resumeText
            );

            String rawResponse = groqApiClient.sendPrompt(prompt);
            List<InterviewQuestion> interviewQuestionList = parseResponse(rawResponse);

            return InterviewResponse.builder()
                    .experienceLevel(experienceLevel)
                    .jobRole(jobRole)
                    .questions(interviewQuestionList)
                    .build();

        } catch (IOException | TikaException e) {
            log.error("Error parsing resume file: {}", e.getMessage());
            throw new ApiException("Failed to parse resume file. It may be corrupted or an unsupported format.", 400);
        }
    }

    private List<InterviewQuestion> parseResponse(String rawResponse) {
        try {
            return objectMapper.readValue(rawResponse, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("Error while parsing AI model response: {}", e.getMessage());
            log.debug("Raw failing response: {}", rawResponse);
            throw new ApiException("Error while parsing the AI's response. The format was unexpected.", 500);
        }
    }
}
