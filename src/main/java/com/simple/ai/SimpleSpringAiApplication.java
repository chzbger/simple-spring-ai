package com.simple.ai;

import org.springframework.ai.model.google.genai.autoconfigure.chat.GoogleGenAiChatAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = GoogleGenAiChatAutoConfiguration.class)
public class SimpleSpringAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSpringAiApplication.class, args);
    }
}
