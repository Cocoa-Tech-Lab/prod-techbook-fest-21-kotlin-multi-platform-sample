import { createRoot } from 'react-dom/client'
import './index.css'
import { App } from './modules/App'

const root = createRoot(document.getElementById('root')!)
// Note: Avoid React.StrictMode in dev to prevent intentional double-invocation of effects
// that caused duplicate API calls during development.
root.render(<App />)
