package com.simple.ai.adapter.in.web;

import com.simple.ai.application.port.in.UserUseCase;
import com.simple.ai.domain.User;
import com.simplejwtauth.common.security.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserUseCase userUseCase;

    @MutationMapping
    public UserResponse signup(@Argument("input") SignupInput input) {
        return UserResponse.from(
                userUseCase.signup(input.username(), input.password(), input.email()));
    }

    @QueryMapping
    public UserResponse currentUser() {
        String userId = AuthContext.findUserId();
        if (userId == null) return null;
        return userUseCase.findById(Long.valueOf(userId))
                .map(UserResponse::from)
                .orElse(null);
    }

    public record SignupInput(String username, String password, String email) {
    }

    public record UserResponse(Long id, String username, String email) {
        public static UserResponse from(User u) {
            return new UserResponse(u.getId(), u.getUsername(), u.getEmail());
        }
    }
}
