package com.simple.ai.adapter.out.security;

import com.simple.ai.application.port.in.UserUseCase;
import com.simplejwtauth.auth.application.port.out.OAuthUserResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthUserResolverImpl implements OAuthUserResolver {

    private final UserUseCase userUseCase;

    @Override
    public String resolve(String registrationId, String providerId) {
        return String.valueOf(
                userUseCase.resolveOAuthUser(registrationId, providerId).getId());
    }
}
