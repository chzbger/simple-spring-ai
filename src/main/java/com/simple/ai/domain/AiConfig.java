package com.simple.ai.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class AiConfig {

    private Long id;
    private Long userId;
    private String type;
    private String model;
    private String apiKey;

    public static AiConfig ofCreate(String type, String model, String apiKey) {
        return AiConfig.builder()
                .type(type)
                .model(model)
                .apiKey(apiKey)
                .build();
    }
}
