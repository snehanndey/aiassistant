// InterviewRequest.java — What the user sends us
package com.project.aiassistant.dto;

import lombok.Data;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Data // Lombok: generates getters, setters, toString, equals
public class InterviewRequest {

    @NotBlank(message = "Job role cannot be empty")
    private String jobRole;

    @NotBlank(message = "Experience level cannot be empty")
    private String experienceLevel; // "Junior", "Mid", "Senior"

    @Min(value = 1, message = "Minimum 1 question")
    @Max(value = 10, message = "Maximum 10 questions")
    private int numberOfQuestions;
}