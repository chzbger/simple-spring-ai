import { createClient } from 'graphql-ws';

const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';

export const wsClient = createClient({
  url: `${protocol}//${window.location.host}/graphql`,
  retryAttempts: 5,
  shouldRetry: () => true,
  lazy: true,
  // 매 연결 시도마다 호출되어 만료/없으면 auth 가 자동 refresh.
  connectionParams: async () => {
    const token = await window.auth.getAccessToken();
    return token ? { Authorization: `Bearer ${token}` } : {};
  },
});
