package com.project.aiassistant.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {RateLimitException.class})
    public ResponseEntity<Map<String, Object>> handleRateLimitException(RateLimitException ex) {
        log.warn("Rate limit exceeded: {}", ex.getMessage());
        return buildErrorResponce(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage());
    }

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex) {
        log.error("AI service error: {}", ex.getMessage());
        return buildErrorResponce(HttpStatus.BAD_GATEWAY, "AI service is not responding, please try after sometime");
    }

    // Handles @Valid annotation failures on request DTOs → 400
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> haldellMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMassage = ex.getBindingResult().getFieldErrors().stream().map(f -> f.getField() + ":" + f.getDefaultMessage()).findFirst().orElse("Invalid Request");
        log.error("Validation error: {}", ex.getMessage());
        return buildErrorResponce(HttpStatus.BAD_REQUEST, errorMassage);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage());
        return buildErrorResponce(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again.");
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponce(HttpStatus statusCode, String message) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("Time Stamp", LocalDateTime.now().toString());
        errorMap.put("Status", statusCode);
        errorMap.put("Error", statusCode.getReasonPhrase());
        errorMap.put("Massage", message);
        return ResponseEntity.status(statusCode).body(errorMap);
    }
}
