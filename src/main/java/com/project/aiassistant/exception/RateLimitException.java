package com.project.aiassistant.exception;

public class RateLimitException extends RuntimeException{


    public RateLimitException(){
       super("AI service rate limit hit. Please try again later.");
    }

}
