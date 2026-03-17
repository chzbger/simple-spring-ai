import { useCallback, useEffect, useState } from 'react';
import { graphqlClient } from '../api/graphqlClient';
import type { AiConfig, AiConfigInput } from '../types';

const LIST_QUERY = `
  query AiConfigs {
    aiConfigs { id type model apiKeyMasked }
  }
`;

const CREATE_MUTATION = `
  mutation CreateAiConfig($input: AiConfigInput!) {
    createAiConfig(input: $input) { id }
  }
`;

const DELETE_MUTATION = `
  mutation DeleteAiConfig($id: ID!) {
    deleteAiConfig(id: $id)
  }
`;

export function useAiConfigs() {
  const [configs, setConfigs] = useState<AiConfig[]>([]);
  const [selectedId, setSelectedId] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    const data = await graphqlClient.request<{ aiConfigs: AiConfig[] }>(LIST_QUERY);
    setConfigs(data.aiConfigs);
    setSelectedId(prev => prev ?? data.aiConfigs[0]?.id ?? null);
  }, []);

  useEffect(() => {
    void refresh();
  }, [refresh]);

  const create = useCallback(async (input: AiConfigInput) => {
    await graphqlClient.request(CREATE_MUTATION, { input });
    await refresh();
  }, [refresh]);

  const remove = useCallback(async (id: string) => {
    await graphqlClient.request(DELETE_MUTATION, { id });
    setSelectedId(prev => (prev === id ? null : prev));
    await refresh();
  }, [refresh]);

  return { configs, selectedId, setSelectedId, create, remove };
}
