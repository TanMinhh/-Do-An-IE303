package com.project.hotelXpress.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    private static final Logger log = LoggerFactory.getLogger(ChatbotController.class);

    @Value("${spring.ai.openai.api-key}")
    private String geminiApiKey;

    private final WebClient webClient;

    public ChatbotController(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    // Inner class static để JSON map đúng
    public static class PromptRequest {
        private String prompt;

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }
    }

    @PostMapping(value = "/ask", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> chatbot(@RequestBody PromptRequest request) {
        try {
            String prompt = request.getPrompt();

            String requestBody = """
                    {
                      "contents": [
                        {
                          "parts": [
                            {
                              "text": "%s"
                            }
                          ]
                        }
                      ]
                    }
                    """.formatted(prompt);

            ResponseEntity<String> response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1beta/models/gemini-2.0-flash:generateContent")
                            .queryParam("key", geminiApiKey)
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            if (response == null || response.getBody() == null) {
                return ResponseEntity.internalServerError()
                        .body("{\"error\":\"Empty response from Gemini API\"}");
            }

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            log.error("❌ Lỗi khi gọi Gemini chatbot API", e);
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"Exception calling Gemini API\"}");
        }
    }
}
