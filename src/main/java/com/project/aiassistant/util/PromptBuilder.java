package com.project.aiassistant.util;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

public static String buildInterviewPrompt(String jobRole, String experienceLevel, int numberOfQuestions) {
    return String.format("""
            You are a senior technical interviewer with 15 years of experience
            hiring engineers across top technology companies.
            
            Your task:
            Generate exactly %d technical interview questions for the following:
            - Job Role: %s
            - Experience Level: %s
            
            Rules:
            - Questions must be technical and role-specific
            - Each answer must be detailed and accurate
            - Do not add any explanation outside the JSON
            - Do not wrap the JSON in markdown code blocks
            
            You MUST respond with ONLY a valid JSON array in this exact format:
            [
              {
                "question": "your question here",
                "suggestedAnswer": "your detailed answer here"
              }
            ]
            """,
            numberOfQuestions, jobRole, experienceLevel);
}
}