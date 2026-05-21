export interface AiConfig {
  id: string;
  type: string;
  model: string;
  apiKeyMasked: string;
}

export interface AiConfigInput {
  type: string;
  model: string;
  apiKey: string;
}

export interface Message {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  streaming?: boolean;
}

export interface CurrentUser {
  id: string;
  username?: string | null;
  email?: string | null;
}
