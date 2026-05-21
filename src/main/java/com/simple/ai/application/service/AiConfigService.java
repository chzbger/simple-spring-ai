package com.simple.ai.application.service;

import com.simple.ai.application.port.in.AiConfigUseCase;
import com.simple.ai.application.port.out.AiConfigPort;
import com.simple.ai.domain.AiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiConfigService implements AiConfigUseCase {

    private final AiConfigPort aiConfigPort;

    @Override
    public AiConfig create(Long userId, String type, String model, String apiKey) {
        return aiConfigPort.save(userId, AiConfig.ofCreate(type, model, apiKey));
    }

    @Override
    public AiConfig update(Long userId, Long id, String type, String model, String apiKey) {
        AiConfig existing = aiConfigPort.findById(userId, id)
                .orElseThrow(() -> new IllegalArgumentException("AI Config not found: " + id));
        AiConfig updated = existing.toBuilder()
                .type(type)
                .model(model)
                .apiKey(apiKey)
                .build();
        return aiConfigPort.save(userId, updated);
    }

    @Override
    public Optional<AiConfig> findById(Long userId, Long id) {
        return aiConfigPort.findById(userId, id);
    }

    @Override
    public List<AiConfig> findAll(Long userId) {
        return aiConfigPort.findAll(userId);
    }

    @Override
    public void delete(Long userId, Long id) {
        aiConfigPort.deleteById(userId, id);
    }
}
