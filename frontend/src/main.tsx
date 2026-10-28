import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from "react-router";

import './styles/tokens.css'
import './index.css'
import App from './App.tsx'
import { PassageView } from './components/PassageView.tsx'


createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/read/:bookCode/:chapterNumber" element={<PassageView />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>,
)
