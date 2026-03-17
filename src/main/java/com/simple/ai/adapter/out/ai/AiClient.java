package com.simple.ai.adapter.out.ai;

import reactor.core.publisher.Flux;

public interface AiClient {

    Flux<String> streamChat(String message, String apiKey, String model);
}
