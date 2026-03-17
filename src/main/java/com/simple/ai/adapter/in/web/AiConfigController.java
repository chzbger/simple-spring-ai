package com.simple.ai.adapter.in.web;

import com.simple.ai.application.port.in.AiConfigUseCase;
import com.simple.ai.domain.AiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AiConfigController {

    private final AiConfigUseCase aiConfigUseCase;

    @QueryMapping
    public String health() {
        return "OK";
    }

    @QueryMapping
    public List<AiConfigResponse> aiConfigs() {
        return aiConfigUseCase.findAll().stream().map(AiConfigResponse::from).toList();
    }

    @QueryMapping
    public AiConfigResponse aiConfig(@Argument Long id) {
        return aiConfigUseCase.findById(id).map(AiConfigResponse::from).orElse(null);
    }

    @MutationMapping
    public AiConfigResponse createAiConfig(@Argument("input") AiConfigInput input) {
        return AiConfigResponse.from(aiConfigUseCase.create(input.type(), input.model(), input.apiKey()));
    }

    @MutationMapping
    public AiConfigResponse updateAiConfig(@Argument Long id, @Argument("input") AiConfigInput input) {
        return AiConfigResponse.from(aiConfigUseCase.update(id, input.type(), input.model(), input.apiKey()));
    }

    @MutationMapping
    public boolean deleteAiConfig(@Argument Long id) {
        aiConfigUseCase.delete(id);
        return true;
    }

    public record AiConfigInput(String type, String model, String apiKey) {
    }

    public record AiConfigResponse(Long id, String type, String model, String apiKeyMasked) {
        public static AiConfigResponse from(AiConfig c) {
            return new AiConfigResponse(c.getId(), c.getType(), c.getModel(), mask(c.getApiKey()));
        }

        private static String mask(String key) {
            if (key == null || key.length() <= 8) return "****";
            return key.substring(0, 4) + "****" + key.substring(key.length() - 4);
        }
    }
}
