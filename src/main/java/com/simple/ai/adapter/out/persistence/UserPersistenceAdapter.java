package com.simple.ai.adapter.out.persistence;

import com.simple.ai.application.port.out.UserPort;
import com.simple.ai.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPersistenceAdapter implements UserPort {

    private final UserRepository repository;

    @Override
    @Transactional
    public User save(User user) {
        UserEntity entity = user.getId() != null
                ? repository.findById(user.getId()).orElseThrow()
                : new UserEntity();
        entity.setUsername(user.getUsername());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setProvider(user.getProvider());
        entity.setProviderId(user.getProviderId());
        entity.setEmail(user.getEmail());
        return mapToDomain(repository.save(entity));
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(this::mapToDomain);
    }

    @Override
    public Optional<User> findByProvider(String provider, String providerId) {
        return repository.findByProviderAndProviderId(provider, providerId).map(this::mapToDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    private User mapToDomain(UserEntity e) {
        return User.builder()
                .id(e.getId())
                .username(e.getUsername())
                .passwordHash(e.getPasswordHash())
                .provider(e.getProvider())
                .providerId(e.getProviderId())
                .email(e.getEmail())
                .build();
    }
}
