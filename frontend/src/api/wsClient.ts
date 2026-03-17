import { createClient } from 'graphql-ws';

const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';

export const wsClient = createClient({
  url: `${protocol}//${window.location.host}/graphql`,
  retryAttempts: 5,
  shouldRetry: () => true,
  lazy: true,
});
