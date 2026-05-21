package com.simple.ai.application.port.in;

import com.simple.ai.domain.AiConfig;

import java.util.List;
import java.util.Optional;

public interface AiConfigUseCase {

    AiConfig create(Long userId, String type, String model, String apiKey);

    AiConfig update(Long userId, Long id, String type, String model, String apiKey);

    Optional<AiConfig> findById(Long userId, Long id);

    List<AiConfig> findAll(Long userId);

    void delete(Long userId, Long id);
}
