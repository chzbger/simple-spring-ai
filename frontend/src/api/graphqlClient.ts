import { GraphQLClient } from 'graphql-request';

const client = new GraphQLClient(`${window.location.origin}/graphql`, {
  fetch: async (input, init) => {
    await window.auth.getAccessToken();
    return window.auth.fetch(input as string, init as RequestInit);
  },
});

export async function request<T>(
  query: string,
  variables?: Record<string, unknown>,
): Promise<T> {
  return client.request<T>(query, variables);
}
