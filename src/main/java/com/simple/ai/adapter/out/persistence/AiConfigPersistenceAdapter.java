package com.simple.ai.adapter.out.persistence;

import com.simple.ai.application.port.out.AiConfigPort;
import com.simple.ai.domain.AiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiConfigPersistenceAdapter implements AiConfigPort {

    private final AiConfigRepository repository;
    private final TextEncryptor textEncryptor;

    @Override
    @Transactional
    public AiConfig save(AiConfig config) {
        var entity = mapToEntity(config);
        var saved = repository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<AiConfig> findById(Long id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<AiConfig> findAll() {
        return repository.findAll().stream().map(this::mapToDomain).toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private AiConfigEntity mapToEntity(AiConfig c) {
        if (c == null) return null;
        AiConfigEntity entity = new AiConfigEntity();
        if (c.getId() != null) {
            entity = repository.findById(c.getId()).orElseThrow();
        }
        entity.setType(c.getType());
        entity.setModel(c.getModel());
        entity.setApiKey(textEncryptor.encrypt(c.getApiKey()));
        return entity;
    }

    private AiConfig mapToDomain(AiConfigEntity e) {
        return AiConfig.builder()
                .id(e.getId())
                .type(e.getType())
                .model(e.getModel())
                .apiKey(textEncryptor.decrypt(e.getApiKey()))
                .build();
    }
}
