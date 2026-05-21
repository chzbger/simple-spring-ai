import { useEffect, useState } from 'react';
import './App.css';
import { AuthScreen } from './components/AuthScreen';
import { ChatHeader } from './components/ChatHeader';
import { ConfigPanel } from './components/ConfigPanel';
import { MessageList } from './components/MessageList';
import { MessageInput } from './components/MessageInput';
import { useAuth } from './hooks/useAuth';
import { useAiConfigs } from './hooks/useAiConfigs';
import { useChatStream } from './hooks/useChatStream';

function App() {
  const auth = useAuth();
  const isAuthed = auth.status === 'authenticated';
  const { configs, selectedId, setSelectedId, create, remove } = useAiConfigs(isAuthed);
  const { messages, sending, status, send, reset } = useChatStream();
  const [showConfigPanel, setShowConfigPanel] = useState(false);

  useEffect(() => {
    if (!isAuthed) reset();
  }, [isAuthed, reset]);

  if (auth.status === 'loading') {
    return <div className="app loading">Loading...</div>;
  }

  if (auth.status === 'anonymous') {
    return <AuthScreen onLogin={auth.login} onSignup={auth.signup} />;
  }

  const handleSend = (message: string) => {
    if (!selectedId) {
      alert('Add an AI config first.');
      return;
    }
    send(message, selectedId);
  };

  return (
    <div className="app">
      <ChatHeader
        user={auth.user}
        configs={configs}
        selectedId={selectedId}
        onSelect={setSelectedId}
        onTogglePanel={() => setShowConfigPanel(prev => !prev)}
        onLogout={auth.logout}
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
