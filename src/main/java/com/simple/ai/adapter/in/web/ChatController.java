package com.simple.ai.adapter.in.web;

import com.simple.ai.application.port.in.ChatUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatUseCase chatUseCase;

    @SubscriptionMapping
    public Flux<String> chat(@Argument String message, @Argument Long configId) {
        return chatUseCase.chat(message, configId);
    }
}
