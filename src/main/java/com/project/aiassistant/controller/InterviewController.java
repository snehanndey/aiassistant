package com.project.aiassistant.controller;


import com.project.aiassistant.dto.InterviewRequest;
import com.project.aiassistant.dto.InterviewResponse;
import com.project.aiassistant.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/interview")
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/generate")
    public ResponseEntity<InterviewResponse> generateInterviewResponse(@Valid @RequestBody InterviewRequest interviewRequest) {
        InterviewResponse  interviewResponse= interviewService.generateInterviewResponse(interviewRequest);

        return ResponseEntity.ok(interviewResponse);
    }


}
