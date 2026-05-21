package com.simple.ai.application.service;

import com.simple.ai.application.port.in.ChatUseCase;
import com.simple.ai.application.port.out.AiConfigPort;
import com.simple.ai.application.port.out.AiPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService implements ChatUseCase {

    private final AiPort aiPort;
    private final AiConfigPort aiConfigPort;

    @Override
    public Flux<String> chat(Long userId, String message, Long configId) {
        if (message == null || message.isBlank()) {
            return Flux.just("메시지를 입력해주세요.");
        }
        var config = aiConfigPort.findById(userId, configId)
                .orElseThrow(() -> new IllegalArgumentException("AI Config not found: " + configId));
        log.info(message);
        return aiPort.streamChat(message, config).doOnNext(log::info);
    }
}
