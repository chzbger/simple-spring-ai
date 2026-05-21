package com.simple.ai.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AiConfigRepository extends JpaRepository<AiConfigEntity, Long> {

    Optional<AiConfigEntity> findByIdAndUserId(Long id, Long userId);

    List<AiConfigEntity> findAllByUserId(Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
}
