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
    public AiConfig save(Long userId, AiConfig config) {
        AiConfigEntity entity = mapToEntity(userId, config);
        return mapToDomain(repository.save(entity));
    }

    @Override
    public Optional<AiConfig> findById(Long userId, Long id) {
        return repository.findByIdAndUserId(id, userId).map(this::mapToDomain);
    }

    @Override
    public List<AiConfig> findAll(Long userId) {
        return repository.findAllByUserId(userId).stream().map(this::mapToDomain).toList();
    }

    @Override
    @Transactional
    public void deleteById(Long userId, Long id) {
        repository.deleteByIdAndUserId(id, userId);
    }

    private AiConfigEntity mapToEntity(Long userId, AiConfig c) {
        AiConfigEntity entity = (c.getId() != null)
                ? repository.findByIdAndUserId(c.getId(), userId).orElseThrow()
                : new AiConfigEntity();
        entity.setUserId(userId);
        entity.setType(c.getType());
        entity.setModel(c.getModel());
        entity.setApiKey(textEncryptor.encrypt(c.getApiKey()));
        return entity;
    }

    private AiConfig mapToDomain(AiConfigEntity e) {
        return AiConfig.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .type(e.getType())
                .model(e.getModel())
                .apiKey(textEncryptor.decrypt(e.getApiKey()))
                .build();
    }
}
