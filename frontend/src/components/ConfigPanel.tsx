import { useState } from 'react';
import type { AiConfig, AiConfigInput } from '../types';

interface Props {
  configs: AiConfig[];
  onCreate: (input: AiConfigInput) => Promise<void>;
  onDelete: (id: string) => Promise<void>;
}

const initialForm: AiConfigInput = {
  type: 'GEMINI',
  model: 'gemini-3.1-flash-lite-preview',
  apiKey: '',
};

export function ConfigPanel({ configs, onCreate, onDelete }: Props) {
  const [form, setForm] = useState<AiConfigInput>(initialForm);

  const handleCreate = async () => {
    if (!form.apiKey.trim()) {
      alert('API Key를 입력해주세요.');
      return;
    }
    await onCreate(form);
    setForm(initialForm);
  };

  return (
    <div className="config-panel">
      <h2>AI 설정 관리</h2>
      <div className="config-form">
        <select
          value={form.type}
          onChange={e => setForm({ ...form, type: e.target.value })}
        >
          <option value="GEMINI">Gemini</option>
        </select>
        <input
          placeholder="모델명"
          value={form.model}
          onChange={e => setForm({ ...form, model: e.target.value })}
        />
        <input
          type="password"
          placeholder="API Key"
          value={form.apiKey}
          onChange={e => setForm({ ...form, apiKey: e.target.value })}
        />
        <button onClick={handleCreate}>추가</button>
      </div>
      <div className="config-list">
        {configs.length === 0 && <p className="config-empty">등록된 설정이 없습니다.</p>}
        {configs.map(c => (
          <div key={c.id} className="config-item">
            <span>{c.type} / {c.model} / {c.apiKeyMasked}</span>
            <button className="delete-btn" onClick={() => onDelete(c.id)}>삭제</button>
          </div>
        ))}
      </div>
    </div>
  );
}
