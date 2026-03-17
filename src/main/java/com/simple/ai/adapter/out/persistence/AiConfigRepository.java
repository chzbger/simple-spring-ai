package com.simple.ai.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AiConfigRepository extends JpaRepository<AiConfigEntity, Long> {
}
