package com.simple.ai.application.port.in;

import com.simple.ai.domain.User;

import java.util.Optional;

public interface UserUseCase {

    User signup(String username, String rawPassword, String email);

    User resolveOAuthUser(String provider, String providerId);

    Optional<User> findById(Long id);
}
