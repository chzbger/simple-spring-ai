import { useState } from 'react';
import type { KeyboardEvent } from 'react';

interface Props {
  disabled: boolean;
  onSend: (message: string) => void;
}

export function MessageInput({ disabled, onSend }: Props) {
  const [input, setInput] = useState('');

  const handleSend = () => {
    const trimmed = input.trim();
    if (!trimmed) return;
    onSend(trimmed);
    setInput('');
  };

  const handleKeyDown = (e: KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <div className="input-area">
      <textarea
        placeholder="Type a message... (Shift+Enter: newline, Enter: send)"
        value={input}
        onChange={e => setInput(e.target.value)}
        onKeyDown={handleKeyDown}
      />
      <button disabled={disabled} onClick={handleSend}>
        Send
      </button>
    </div>
  );
}
