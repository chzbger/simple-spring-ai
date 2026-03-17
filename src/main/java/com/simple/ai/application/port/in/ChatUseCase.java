package com.simple.ai.application.port.in;

import reactor.core.publisher.Flux;

public interface ChatUseCase {

    Flux<String> chat(String message, Long configId);
}
