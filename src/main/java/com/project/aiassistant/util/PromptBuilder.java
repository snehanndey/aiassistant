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
                - Questions must be technical and role-specific.
                - Each answer must be detailed and accurate.
                - Do not add any explanation outside the JSON.
                - Do not wrap the JSON in markdown code blocks.
                
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

    public static String buildResumeInterviewPrompt(String jobRole, String experienceLevel, int numberOfQuestions, String resumeText) {
        return String.format("""
                You are a senior technical interviewer conducting an interview with a candidate.
                You are speaking directly to the candidate. You have their resume in front of you.
                
                **Candidate's Resume Text:**
                ---
                %s
                ---
                
                **Your Task:**
                Directly ask the candidate exactly %d insightful, technical questions based on their resume.
                Frame the questions as if you are speaking to them in a real interview.
                
                **Rules:**
                1.  **Speak to the Candidate:** Start questions by referencing their resume (e.g., "I see on your resume you worked on...", "You mentioned experience with [Technology], can you tell me about...").
                2.  **Be Specific:** The questions MUST be directly tied to the projects, skills, or technologies mentioned in the resume text provided.
                3.  **Format:** You MUST respond with ONLY a valid JSON array in this exact format. Do not add any conversational text, explanations, or markdown.
                4.  ** Each answer must be detailed and accurate.
                
                **JSON Format:**
                [
                  {
                    "question": "Your direct question to the candidate, based on their resume.",
                    "suggestedAnswer": "A detailed, expert-level answer to the question you just asked."
                  }
                ]
                """,
                resumeText, numberOfQuestions, jobRole, experienceLevel);
    }
}