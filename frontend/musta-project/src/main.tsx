import './index.css';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import { createRoot, hydrateRoot } from 'react-dom/client';

const rootElement = document.getElementById('root');
const root = createRoot(rootElement as Element);

root.render(
  <BrowserRouter>
    <App />
  </BrowserRouter>
);
