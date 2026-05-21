package com.simple.ai.application.port.out;

import com.simple.ai.domain.User;

import java.util.Optional;

public interface UserPort {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByProvider(String provider, String providerId);

    boolean existsByUsername(String username);
}
