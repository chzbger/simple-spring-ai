import { useCallback, useEffect, useRef, useState } from 'react';
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
  const unsubscribeRef = useRef<(() => void) | null>(null);

  useEffect(() => () => {
    unsubscribeRef.current?.();
  }, []);

  const send = useCallback((message: string, configId: string) => {
    unsubscribeRef.current?.();

    const userMsg: Message = { id: crypto.randomUUID(), role: 'user', content: message };
    const assistantId = crypto.randomUUID();
    const assistantMsg: Message = {
      id: assistantId,
      role: 'assistant',
      content: '',
      streaming: true,
    };

    setMessages(prev => [...prev, userMsg, assistantMsg]);
    setSending(true);
    setStatus('Waiting...');

    const finish = () => {
      setMessages(prev =>
        prev.map(m => (m.id === assistantId ? { ...m, streaming: false } : m)),
      );
      setSending(false);
      setStatus('Ready');
      unsubscribeRef.current = null;
    };

    unsubscribeRef.current = wsClient.subscribe<{ chat: string }>(
      { query: CHAT_SUBSCRIPTION, variables: { message, configId } },
      {
        next: (result) => {
          const chunk = result.data?.chat;
          if (!chunk) return;
          setStatus('Streaming...');
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
          unsubscribeRef.current = null;
        },
        complete: finish,
      },
    );
  }, []);

  const reset = useCallback(() => {
    unsubscribeRef.current?.();
    unsubscribeRef.current = null;
    setMessages([]);
    setSending(false);
    setStatus('Ready');
  }, []);

  return { messages, sending, status, send, reset };
}
