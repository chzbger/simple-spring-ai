# Simple Spring AI

Spring AI 와 GraphQL Subscription 으로 LLM 토큰 단위 스트리밍을 다뤄본 사이드 프로젝트.
사용자별 API 키를 안전하게 관리하면서 WebSocket 으로 실시간 응답을 흘린다.

## 기술 스택

### Backend
- Java 21, Spring Boot 3.5
- Spring AI 1.1 (Google Gemini)
- Spring GraphQL (HTTP + WebSocket Subscription)
- Spring Data JPA, H2 (file)
- Spring Security + [simple-jwt-auth](https://github.com/chzbger/simple-jwt-auth) (JWT 인증)
- Gradle 9.4 (Kotlin DSL)

### Frontend
- React 19, TypeScript, Vite 8
- graphql-request (Query / Mutation)
- graphql-ws (Subscription)

## 핵심 동작

1. 로컬 회원가입 / 로그인 (BCrypt 해시, refresh token 쿠키 + access token JWT) 또는 **Google OAuth 로그인**
2. 사용자별 AI Config 등록 (API 키는 DB에 암호화되어 저장, 응답에서는 마스킹된 값만 노출)
3. WebSocket 구독으로 LLM 응답을 토큰 단위 스트리밍 (`Flux<String>` 종단 간 전달)

## 인증 구조

- HTTP `POST /api/auth/login` / `/refresh` / `/logout`, `GET /api/auth/me` 는 라이브러리 제공
- `GET /api/auth/oauth/google` -> Google OAuth 시작, `/api/auth/oauth/callback/google` 에서 콜백 처리. `OAuthUserResolverImpl` 이 provider+providerId 로 사용자 조회/생성
- GraphQL HTTP 호출은 `Authorization: Bearer {accessToken}` 헤더로 인증
- GraphQL WebSocket 구독은 `connection_init` payload 에 `Authorization` 으로 인증 (`GraphQlWebSocketAuthInterceptor`)
- 모든 AI Config / Chat 자원은 토큰의 `sub` claim 에 묶여 사용자 격리

### Google OAuth 사용 시 사전 설정

Google Cloud Console -> Credentials -> OAuth 2.0 Client -> 승인된 리디렉션 URI:
```
http://localhost:3000/api/auth/oauth/callback/google
```
Vite proxy 가 `/api` 를 백엔드로 forward 하므로 브라우저 origin 은 3000 으로 일관 유지.

## 프로젝트 구조

```
com.simple.ai
├── config/                          # 인프라 설정 (CryptoConfig)
├── domain/                          # 도메인 모델 (User, AiConfig)
├── application/
│   ├── port/in/                     # UserUseCase, AiConfigUseCase, ChatUseCase
│   ├── port/out/                    # UserPort, AiConfigPort, AiPort
│   └── service/                     # 유스케이스 구현
└── adapter/
    ├── in/web/                      # GraphQL 컨트롤러 + WebSocket 인증 인터셉터
    └── out/
        ├── ai/                      # AI 클라이언트 (RoutingAiAdapter, GeminiClient)
        ├── persistence/             # JPA 엔티티/어댑터
        └── security/                # UserDetailsService 구현
```

## 실행

```bash
# Backend (port 8080)
./gradlew bootRun

# Frontend (port 3000, /graphql + /api 를 8080 으로 프록시)
cd frontend
npm install
npm run dev
```

- App: http://localhost:3000
- GraphiQL: http://localhost:8080/graphiql
- H2 Console: http://localhost:8080/h2-console

## 테스트

```bash
./gradlew test                                   # 전체
cd frontend && npx tsc -p tsconfig.app.json --noEmit   # 프론트 타입체크
```

## GraphQL 스키마

```graphql
type Query {
    aiConfigs: [AiConfig!]!
    aiConfig(id: ID!): AiConfig
    currentUser: UserResponse
}

type Mutation {
    signup(input: SignupInput!): UserResponse!
    createAiConfig(input: AiConfigInput!): AiConfig!
    updateAiConfig(id: ID!, input: AiConfigInput!): AiConfig!
    deleteAiConfig(id: ID!): Boolean!
}

type Subscription {
    chat(message: String!, configId: ID!): String!
}
```

## 보안 / 운영 설정

API 키는 Spring Security `Encryptors.delux` 로 암호화되어 H2 에 저장되고, 응답에서는 `apiKeyMasked` 만 노출된다.
운영 환경에서는 다음 환경변수를 반드시 주입할 것 (기본값은 dev 전용).

```
APP_CRYPTO_PASSWORD=<강한 비밀번호>
APP_CRYPTO_SALT=<16자리 hex>
AUTH_JWT_SECRET=<32바이트 이상 UTF-8 문자열>
AUTH_COOKIE_SECURE=true                # HTTPS 환경에서
GOOGLE_CLIENT_ID=<운영 Google OAuth client id>
GOOGLE_CLIENT_SECRET=<운영 Google OAuth client secret>
```

DB 스키마 변경 시 `data/` 디렉토리(H2 file DB)를 삭제하고 재시작.
