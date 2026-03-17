import type { AiConfig } from '../types';

interface Props {
  configs: AiConfig[];
  selectedId: string | null;
  onSelect: (id: string | null) => void;
  onTogglePanel: () => void;
}

export function ChatHeader({ configs, selectedId, onSelect, onTogglePanel }: Props) {
  return (
    <header className="header">
      <h1>Simple Spring AI</h1>
      <div className="controls">
        <select
          value={selectedId ?? ''}
          onChange={e => onSelect(e.target.value || null)}
        >
          <option value="">-- AI 설정 선택 --</option>
          {configs.map(c => (
            <option key={c.id} value={c.id}>
              {c.type} / {c.model}
            </option>
          ))}
        </select>
        <button className="config-btn" onClick={onTogglePanel}>
          설정 관리
        </button>
      </div>
    </header>
  );
}
