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
    public AiConfig create(String type, String model, String apiKey) {
        return aiConfigPort.save(AiConfig.ofCreate(type, model, apiKey));
    }

    @Override
    public AiConfig update(Long id, String type, String model, String apiKey) {
        var existing = aiConfigPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AI Config not found: " + id));
        var updated = existing.toBuilder()
                .type(type)
                .model(model)
                .apiKey(apiKey)
                .build();
        return aiConfigPort.save(updated);
    }

    @Override
    public Optional<AiConfig> findById(Long id) {
        return aiConfigPort.findById(id);
    }

    @Override
    public List<AiConfig> findAll() {
        return aiConfigPort.findAll();
    }

    @Override
    public void delete(Long id) {
        aiConfigPort.deleteById(id);
    }
}
