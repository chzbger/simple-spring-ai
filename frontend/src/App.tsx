import { useState } from 'react';
import './App.css';
import { ChatHeader } from './components/ChatHeader';
import { ConfigPanel } from './components/ConfigPanel';
import { MessageList } from './components/MessageList';
import { MessageInput } from './components/MessageInput';
import { useAiConfigs } from './hooks/useAiConfigs';
import { useChatStream } from './hooks/useChatStream';

function App() {
  const { configs, selectedId, setSelectedId, create, remove } = useAiConfigs();
  const { messages, sending, status, send } = useChatStream();
  const [showConfigPanel, setShowConfigPanel] = useState(false);

  const handleSend = (message: string) => {
    if (!selectedId) {
      alert('AI 설정을 먼저 추가해주세요.');
      return;
    }
    send(message, selectedId);
  };

  return (
    <div className="app">
      <ChatHeader
        configs={configs}
        selectedId={selectedId}
        onSelect={setSelectedId}
        onTogglePanel={() => setShowConfigPanel(prev => !prev)}
      />
      {showConfigPanel && (
        <ConfigPanel configs={configs} onCreate={create} onDelete={remove} />
      )}
      <MessageList messages={messages} />
      <div className="status">{status}</div>
      <MessageInput disabled={sending} onSend={handleSend} />
    </div>
  );
}

export default App;
