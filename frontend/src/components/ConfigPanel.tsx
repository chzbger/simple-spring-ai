import { useState } from 'react';
import type { AiConfig, AiConfigInput } from '../types';

interface Props {
  configs: AiConfig[];
  onCreate: (input: AiConfigInput) => Promise<void>;
  onDelete: (id: string) => Promise<void>;
}

const initialForm: AiConfigInput = {
  type: 'GEMINI',
  model: 'gemini-3.1-flash-lite',
  apiKey: '',
};

export function ConfigPanel({ configs, onCreate, onDelete }: Props) {
  const [form, setForm] = useState<AiConfigInput>(initialForm);
  const [error, setError] = useState<string | null>(null);

  const handleCreate = async () => {
    setError(null);
    if (!form.apiKey.trim()) {
      setError('API key is required.');
      return;
    }
    try {
      await onCreate(form);
      setForm(initialForm);
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to create');
    }
  };

  return (
    <div className="config-panel">
      <h2>AI Settings</h2>
      <div className="config-form">
        <select
          value={form.type}
          onChange={e => setForm({ ...form, type: e.target.value })}
        >
          <option value="GEMINI">Gemini</option>
        </select>
        <input
          placeholder="Model"
          value={form.model}
          onChange={e => setForm({ ...form, model: e.target.value })}
        />
        <input
          type="password"
          placeholder="API key"
          value={form.apiKey}
          onChange={e => setForm({ ...form, apiKey: e.target.value })}
        />
        <button onClick={handleCreate}>Add</button>
      </div>
      {error && <p className="config-error">{error}</p>}
      <div className="config-list">
        {configs.length === 0 && <p className="config-empty">No configs yet.</p>}
        {configs.map(c => (
          <div key={c.id} className="config-item">
            <span>{c.type} / {c.model} / {c.apiKeyMasked}</span>
            <button className="delete-btn" onClick={() => onDelete(c.id)}>Delete</button>
          </div>
        ))}
      </div>
    </div>
  );
}
