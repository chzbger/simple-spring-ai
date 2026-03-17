package com.simple.ai.adapter.out.ai;

import com.simple.ai.application.port.out.AiPort;
import com.simple.ai.domain.AiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Primary
@Component
@RequiredArgsConstructor
public class RoutingAiAdapter implements AiPort {

    private final GeminiClient geminiClient;

    private AiClient getClient(String type) {
        return geminiClient;
    }

    @Override
    public Flux<String> streamChat(String message, AiConfig config) {
        return getClient(config.getType()).streamChat(message, config.getApiKey(), config.getModel());
    }
}
