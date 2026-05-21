import type { AiConfig, CurrentUser } from '../types';

interface Props {
  user: CurrentUser | null;
  configs: AiConfig[];
  selectedId: string | null;
  onSelect: (id: string | null) => void;
  onTogglePanel: () => void;
  onLogout: () => void;
}

export function ChatHeader({ user, configs, selectedId, onSelect, onTogglePanel, onLogout }: Props) {
  return (
    <header className="header">
      <h1>Simple Spring AI</h1>
      <div className="controls">
        <select
          value={selectedId ?? ''}
          onChange={e => onSelect(e.target.value || null)}
        >
          <option value="">-- Select AI config --</option>
          {configs.map(c => (
            <option key={c.id} value={c.id}>
              {c.type} / {c.model}
            </option>
          ))}
        </select>
        <button className="config-btn" onClick={onTogglePanel}>
          Settings
        </button>
        <span className="user-label">{user?.username ?? `#${user?.id ?? ''}`}</span>
        <button className="logout-btn" onClick={onLogout}>
          Logout
        </button>
      </div>
    </header>
  );
}
