package com.simple.ai.application.port.in;

import com.simple.ai.domain.AiConfig;

import java.util.List;
import java.util.Optional;

public interface AiConfigUseCase {

    AiConfig create(String type, String model, String apiKey);

    AiConfig update(Long id, String type, String model, String apiKey);

    Optional<AiConfig> findById(Long id);

    List<AiConfig> findAll();

    void delete(Long id);
}
