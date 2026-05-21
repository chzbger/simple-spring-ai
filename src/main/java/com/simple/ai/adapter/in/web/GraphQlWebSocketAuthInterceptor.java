package com.simple.ai.adapter.in.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.graphql.server.WebSocketGraphQlInterceptor;
import org.springframework.graphql.server.WebSocketSessionInfo;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GraphQlWebSocketAuthInterceptor implements WebSocketGraphQlInterceptor {

    public static final String USER_ID_KEY = "userId";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtDecoder jwtDecoder;

    @Override
    public Mono<Object> handleConnectionInitialization(
            WebSocketSessionInfo sessionInfo,
            Map<String, Object> connectionInitPayload) {
        Object header = connectionInitPayload.get(AUTHORIZATION);
        if (!(header instanceof String authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            return Mono.error(new IllegalArgumentException("Missing Authorization in connection_init"));
        }
        try {
            Jwt jwt = jwtDecoder.decode(authHeader.substring(BEARER_PREFIX.length()));
            sessionInfo.getAttributes().put(USER_ID_KEY, jwt.getSubject());
            return Mono.empty();
        } catch (JwtException e) {
            log.debug("Subscription auth failed: {}", e.getMessage());
            return Mono.error(new IllegalArgumentException("Invalid token"));
        }
    }

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, WebGraphQlInterceptor.Chain chain) {
        Object userId = request.getAttributes().get(USER_ID_KEY);
        if (userId != null) {
            request.configureExecutionInput((input, builder) ->
                    builder.graphQLContext(Map.of(USER_ID_KEY, userId)).build());
        }
        return chain.next(request);
    }
}
