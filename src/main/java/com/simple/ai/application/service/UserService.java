package com.simple.ai.application.service;

import com.simple.ai.application.port.in.UserUseCase;
import com.simple.ai.application.port.out.UserPort;
import com.simple.ai.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User signup(String username, String rawPassword, String email) {
        if (userPort.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken: " + username);
        }
        String hash = passwordEncoder.encode(rawPassword);
        return userPort.save(User.ofSignup(username, hash, email));
    }

    @Override
    public User resolveOAuthUser(String provider, String providerId) {
        return userPort.findByProvider(provider, providerId)
                .orElseGet(() -> userPort.save(User.ofOAuth(provider, providerId)));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userPort.findById(id);
    }
}
