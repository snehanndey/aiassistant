// InterviewResponse.java — What we send back to the user
package com.project.aiassistant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder // Lombok: enables InterviewResponse.builder().jobRole("...").build()
@NoArgsConstructor
@AllArgsConstructor
public class InterviewResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String jobRole;
    private String experienceLevel;
    private List<InterviewQuestion> questions;
}