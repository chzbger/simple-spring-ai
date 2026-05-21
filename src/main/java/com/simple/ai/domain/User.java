package com.simple.ai.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class User {

    private Long id;
    private String username;
    private String passwordHash;
    private String provider;
    private String providerId;
    private String email;

    public static User ofSignup(String username, String passwordHash, String email) {
        return User.builder()
                .username(username)
                .passwordHash(passwordHash)
                .email(email)
                .build();
    }

    public static User ofOAuth(String provider, String providerId) {
        return User.builder()
                .provider(provider)
                .providerId(providerId)
                .build();
    }
}
