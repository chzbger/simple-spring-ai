package com.simple.ai.application.port.in;

import reactor.core.publisher.Flux;

public interface ChatUseCase {

    Flux<String> chat(Long userId, String message, Long configId);
}
