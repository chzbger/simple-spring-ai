package com.simple.ai.application.port.out;

import com.simple.ai.domain.AiConfig;
import reactor.core.publisher.Flux;

public interface AiPort {

    Flux<String> streamChat(String message, AiConfig config);
}
