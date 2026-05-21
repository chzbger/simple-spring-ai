package com.simple.ai.adapter.in.web.security;

import com.simplejwtauth.common.security.AuthContext;
import org.springframework.stereotype.Component;

@Component
public class AuthContextCurrentUserResolver implements CurrentUserResolver {

    @Override
    public Long getUserId() {
        return Long.valueOf(AuthContext.getUserId());
    }
}
