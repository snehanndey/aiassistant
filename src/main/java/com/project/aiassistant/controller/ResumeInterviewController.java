package com.project.aiassistant.controller;

import com.project.aiassistant.dto.InterviewResponse;
import com.project.aiassistant.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/resume-interview")
@RequiredArgsConstructor
public class ResumeInterviewController {

    private final InterviewService interviewService;

    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public InterviewResponse generateResumeBasedQuestions(
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("jobRole") String jobRole,
            @RequestParam("experienceLevel") String experienceLevel,
            @RequestParam("numberOfQuestions") int numberOfQuestions) {

        // We will delegate the complex logic to the service layer.
        // The controller's job is just to handle the web request.
        return interviewService.generateQuestionsFromResume(
                resume,
                jobRole,
                experienceLevel,
                numberOfQuestions
        );
    }
}
