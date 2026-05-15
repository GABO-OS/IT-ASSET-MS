// main.jsx - Entry point ng React application
// Ito ang pinakauna na nag-e-execute - dito ini-initialize ang buong app

import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'   // I-import ang global styles
import App from './App.jsx'

// I-mount ang App component sa HTML element na may id="root"
// Yung id="root" ay nasa index.html
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
