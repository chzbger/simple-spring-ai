package com.simple.ai.application.port.out;

import com.simple.ai.domain.AiConfig;

import java.util.List;
import java.util.Optional;

public interface AiConfigPort {

    AiConfig save(AiConfig config);

    Optional<AiConfig> findById(Long id);

    List<AiConfig> findAll();

    void deleteById(Long id);
}
