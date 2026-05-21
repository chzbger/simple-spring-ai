import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'

const authUrl = `${window.location.origin}/sja/auth.js`
const mod = await import(/* @vite-ignore */ authUrl) as {
  SimpleJwtAuth: new (opts?: { baseUrl?: string }) => SimpleJwtAuthInstance
}
window.auth = new mod.SimpleJwtAuth()

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
