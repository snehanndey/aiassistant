# AI Interview Assistant

## Project Overview

The AI Interview Assistant is a production-style application built with Java Spring Boot, designed to help users generate technical interview questions. It leverages AI models (Gemini/Groq) to create tailored questions based on a specified job role and experience level, with an advanced feature to generate questions directly from a candidate's resume. The project emphasizes modern backend architecture, API integration, caching, and robust error handling.

This project was developed as part of a mentorship program, focusing on best practices in Java, Spring Boot, Microservices, and AI integration.

## Features

-   **General Question Generation:** Generate a specified number of technical interview questions for a given job role and experience level.
-   **Resume-Aware Question Generation:** Upload a candidate's resume (PDF, DOCX, TXT) to generate highly tailored interview questions based on their specific skills and experience.
-   **AI API Integration:** Seamless integration with external AI models (currently Gemini/Groq) for question generation.
-   **Redis Caching:** Utilizes Redis for caching generated questions, improving performance and reducing API calls for repeated requests.
-   **Modern Frontend UI:** A clean, responsive web interface built with plain HTML, CSS, and JavaScript, featuring a tabbed layout for different generation modes.
-   **Real-time Feedback:** Provides loading indicators, processing messages, and response times for a better user experience.
-   **Robust Error Handling:** Implements custom exception handling and global error management.
-   **Prompt Engineering:** Carefully crafted prompts to guide the AI in generating relevant and well-formatted output.
-   **Clean Architecture:** Adheres to SOLID principles and a clear package structure for maintainability and scalability.

## Technology Stack

**Backend:**
-   **Java 17+**
-   **Spring Boot 4.0.5**
-   **Spring WebFlux** (for `WebClient` for non-blocking HTTP calls)
-   **Jackson 3.x** (for JSON processing)
-   **Lombok** (for boilerplate code reduction)
-   **Redis** (for caching, via `spring-boot-starter-data-redis` and `spring-boot-starter-cache`)
-   **Apache Tika** (for resume parsing from various document formats)
-   **Maven** (build automation)

**AI APIs (Free Tier):**
-   **Gemini API** (configurable)
-   **Groq API** (configurable)

**Frontend:**
-   **HTML5**
-   **CSS3**
-   **JavaScript (ES6+)**

## Getting Started

### Prerequisites

-   **Java Development Kit (JDK) 17 or higher**
-   **Apache Maven 3.6.0 or higher**
-   **Docker** (for running Redis)
-   **API Keys** for Gemini or Groq (configured via environment variables)

### Setup

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/aiassistant.git
    cd aiassistant
    ```

2.  **Configure API Keys:**
    Set your AI API keys as environment variables. For example:
    ```bash
    export GEMINI_API_KEY="YOUR_GEMINI_API_KEY"
    export GROQ_API_KEY="YOUR_GROQ_API_KEY"
    ```
    *(Note: The application is configured to use Groq by default in `InterviewService.java`, but can be switched to Gemini by uncommenting the relevant lines.)*

3.  **Start Redis using Docker:**
    ```bash
    docker run --name aiassistant-redis -p 6379:6379 -d redis/redis-stack-server:latest
    ```
    This will start a Redis instance accessible on `localhost:6379`.

4.  **Build the project:**
    ```bash
    mvn clean install
    ```

5.  **Run the Spring Boot application:**
    ```bash
    mvn spring-boot:run
    ```
    The application will start on `http://localhost:8080`.

## API Endpoints

### 1. Generate General Interview Questions

-   **URL:** `POST http://localhost:8080/api/v1/interview/generate`
-   **Content-Type:** `application/json`
-   **Request Body Example:**
    ```json
    {
      "jobRole": "Senior Java Developer",
      "experienceLevel": "Mid",
      "numberOfQuestions": 3
    }
    ```
-   **Response Body Example:**
    ```json
    {
      "jobRole": "Senior Java Developer",
      "experienceLevel": "Mid",
      "questions": [
        {
          "question": "Explain the internal mechanics of ConcurrentHashMap in Java 8 and later...",
          "suggestedAnswer": "ConcurrentHashMap uses a combination of techniques..."
        },
        // ... more questions
      ]
    }
    ```

### 2. Generate Resume-Aware Interview Questions

-   **URL:** `POST http://localhost:8080/api/v1/resume-interview/generate`
-   **Content-Type:** `multipart/form-data`
-   **Form Fields:**
    -   `resume`: (File) The resume file (e.g., `.pdf`, `.docx`, `.txt`).
    -   `jobRole`: (String) e.g., "Senior Java Developer"
    -   `experienceLevel`: (String) e.g., "Mid"
    -   `numberOfQuestions`: (Integer) e.g., 3

## Frontend Usage

Access the application in your web browser at `http://localhost:8080`.

The UI features two tabs:

1.  **General Questions:**
    -   Fill in the "Job Role", "Experience Level", and "Number of Questions".
    -   Click "Generate Questions".
    -   The AI will generate general technical questions based on your input.

2.  **From Resume:**
    -   Click "Choose File" to upload a candidate's resume (PDF, DOCX, or TXT).
    -   Fill in the "Job Role", "Experience Level", and "Number of Questions".
    -   Click "Generate From Resume".
    -   The AI will analyze the resume content and generate highly personalized interview questions.

Both tabs provide real-time feedback, including a loading spinner, a "processing" message, and the total response time.

## Project Structure

```
com.project.aiassistant
  ├── controller   → InterviewController.java, ResumeInterviewController.java
  ├── service      → InterviewService.java, ResumeParserService.java
  ├── integration  → GroqApiClient.java (or GeminiApiClient.java)
  ├── config       → GroqConfig.java, RedisCacheConfig.java
  ├── dto          → InterviewRequest.java, InterviewResponse.java, InterviewQuestion.java
  ├── exception    → ApiException.java, RateLimitException.java, GlobalExceptionHandler.java
  └── util         → PromptBuilder.java
src/main/resources/static
  ├── index.html   → Main frontend page
  ├── style.css    → Frontend styling
  └── script.js    → Frontend logic
```

## Key Technical Decisions

-   **WebClient over RestTemplate:** Chosen for non-blocking I/O and modern reactive programming principles.
-   **Plain REST over Spring AI:** To gain a deeper understanding of AI API internals and prompt engineering without framework abstraction.
-   **Config-based Model Name:** Allows easy switching between AI models (e.g., Groq to Gemini) without code changes.
-   **Separate Integration Layer:** Ensures Single Responsibility, testability, and easy replacement of AI providers.
-   **PromptBuilder Utility:** Centralizes prompt engineering logic, making it easy to manage and optimize AI instructions.
-   **JSON-format Prompt Instruction:** Guarantees parseable AI output, crucial for structured data.
-   **Redis for Caching:** Provides a shared, external cache suitable for microservice deployments, with careful Jackson configuration to handle classloader issues.
-   **Apache Tika for Resume Parsing:** A robust, industry-standard library for extracting text from diverse document formats.

## Future Enhancements

-   **Prompt Optimization:** Continuous refinement of AI prompts for more accurate, relevant, and diverse questions.
-   **Advanced Error Handling:** Implement specific error handling for AI API rate limits, invalid API keys, and other external service failures.
-   **More AI Features:** Explore adding features like AI-powered resume analysis, answer evaluation, or prompt optimization.
-   **API Security:** Implement authentication and authorization mechanisms (e.g., OAuth2, JWT).
-   **Event-Driven Architecture:** Introduce messaging queues (e.g., Kafka, RabbitMQ) for asynchronous processing of long-running tasks.
-   **Dockerization:** Containerize the application and Redis for easier deployment and scalability.
-   **Database Integration:** Store generated questions, user profiles, or interview sessions.

## Mentorship Notes

This project serves as a practical learning experience, demonstrating how to build a production-ready AI-powered application using modern Java and Spring Boot practices. It covers critical aspects from architectural design and API integration to frontend development and robust error handling, aiming to equip developers with the skills to build similar systems independently.
