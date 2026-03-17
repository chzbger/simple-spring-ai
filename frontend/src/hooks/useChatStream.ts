import { useCallback, useState } from 'react';
import { wsClient } from '../api/wsClient';
import type { Message } from '../types';

const CHAT_SUBSCRIPTION = `
  subscription Chat($message: String!, $configId: ID!) {
    chat(message: $message, configId: $configId)
  }
`;

export function useChatStream() {
  const [messages, setMessages] = useState<Message[]>([]);
  const [sending, setSending] = useState(false);
  const [status, setStatus] = useState('Ready');

  const send = useCallback((message: string, configId: string) => {
    const userMsg: Message = {
      id: crypto.randomUUID(),
      role: 'user',
      content: message,
    };
    const assistantId = crypto.randomUUID();
    const assistantMsg: Message = {
      id: assistantId,
      role: 'assistant',
      content: '',
      streaming: true,
    };

    setMessages(prev => [...prev, userMsg, assistantMsg]);
    setSending(true);
    setStatus('AI 응답 대기 중...');

    const finish = () => {
      setMessages(prev =>
        prev.map(m => (m.id === assistantId ? { ...m, streaming: false } : m)),
      );
      setSending(false);
      setStatus('Ready');
    };

    wsClient.subscribe<{ chat: string }>(
      { query: CHAT_SUBSCRIPTION, variables: { message, configId } },
      {
        next: (result) => {
          const chunk = result.data?.chat;
          if (!chunk) return;
          setStatus('AI 응답 수신 중...');
          setMessages(prev =>
            prev.map(m =>
              m.id === assistantId ? { ...m, content: m.content + chunk } : m,
            ),
          );
        },
        error: (err) => {
          const errStr = err instanceof Error ? err.message : JSON.stringify(err);
          setMessages(prev =>
            prev.map(m =>
              m.id === assistantId
                ? { ...m, content: 'Error: ' + errStr, streaming: false }
                : m,
            ),
          );
          setSending(false);
          setStatus('Ready');
        },
        complete: finish,
      },
    );
  }, []);

  return { messages, sending, status, send };
}
