package com.simple.ai.adapter.out.ai;

import com.google.genai.Client;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GeminiClient implements AiClient {

    @Override
    public Flux<String> streamChat(String message, String apiKey, String model) {
        var client = Client.builder().apiKey(apiKey).build();
        var options = GoogleGenAiChatOptions.builder()
                .model(model)
                .temperature(0.7)
                .build();
        var chatModel = GoogleGenAiChatModel.builder()
                .genAiClient(client)
                .defaultOptions(options)
                .build();
        var chatClient = ChatClient.builder(chatModel).build();

        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
}
