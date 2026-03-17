import { useEffect, useRef } from 'react';
import type { Message } from '../types';

interface Props {
  messages: Message[];
}

export function MessageList({ messages }: Props) {
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const el = ref.current;
    if (el) el.scrollTop = el.scrollHeight;
  }, [messages]);

  return (
    <div className="message-area" ref={ref}>
      {messages.map(msg => (
        <div
          key={msg.id}
          className={`message ${msg.role}${msg.streaming ? ' streaming' : ''}`}
        >
          {msg.content}
        </div>
      ))}
    </div>
  );
}
