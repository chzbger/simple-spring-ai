# Simple Spring AI

Spring AI + GraphQL 로 AI 호출하여 실시간 스트리밍 응답을 받는 심플한 소스

## 기술 스택

### Backend
- Java 21, Spring Boot 3.5.0
- Spring AI 1.1.2 (Google Gemini)
- Spring GraphQL (Subscription + WebSocket)
- Spring Data JPA + H2
- Gradle 9.4 (Kotlin DSL)

### Frontend
- React 19 + TypeScript
- Vite 8
- graphql-request (Query / Mutation)
- graphql-ws (Subscription)

## 프로젝트 구조

```
com.simple.ai
├── config/                          # 인프라 설정 (CryptoConfig)
├── domain/                          # 도메인 모델 (AiConfig)
├── application/
│   ├── port/in/                     # 인바운드 포트 (ChatUseCase, AiConfigUseCase)
│   ├── port/out/                    # 아웃바운드 포트 (AiPort, AiConfigPort)
│   └── service/                     # 유스케이스 구현 (ChatService, AiConfigService)
└── adapter/
    ├── in/web/                      # GraphQL 컨트롤러
    └── out/
        ├── ai/                      # AI 라우팅 (RoutingAiAdapter, AiClient, GeminiClient)
        └── persistence/             # JPA 영속성 어댑터
```

### 실행

```bash
# Backend
./gradlew bootRun

# Frontend
cd frontend
npm install
npm run dev
```

- Backend: http://localhost:8080
- Frontend: http://localhost:3000

## Core Workflow

* AI용 API키를 화면에서 등록하고 호출할때 사용
* 화면에서 AI에게 메시지 전송 및 스트리밍으로 응답 수신(GraphQL + WebSocket)


## GraphQL API

엔드포인트: `/graphql` (WebSocket)

```graphql
type Query {
    health: String!
    aiConfigs: [AiConfig!]!
    aiConfig(id: ID!): AiConfig
}

type Mutation {
    createAiConfig(input: AiConfigInput!): AiConfig!
    updateAiConfig(id: ID!, input: AiConfigInput!): AiConfig!
    deleteAiConfig(id: ID!): Boolean!
}

type Subscription {
    chat(message: String!, configId: ID!): String!
}
```

- GraphiQL: http://localhost:8080/graphiql
- H2 Console: http://localhost:8080/h2-console
