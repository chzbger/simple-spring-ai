package com.simple.ai.application.port.out;

import com.simple.ai.domain.AiConfig;

import java.util.List;
import java.util.Optional;

public interface AiConfigPort {

    AiConfig save(Long userId, AiConfig config);

    Optional<AiConfig> findById(Long userId, Long id);

    List<AiConfig> findAll(Long userId);

    void deleteById(Long userId, Long id);
}
