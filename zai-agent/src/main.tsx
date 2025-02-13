import ReactDOM from 'react-dom/client'

// import { I18nextProvider } from 'react-i18next'
// import i18next from '@/i18n'
import './i18n';
import App from './App.tsx'
import './index.css'
import 'primeicons/primeicons.css';
import "primereact/resources/themes/lara-light-cyan/theme.css";
// import VConsole from "vconsole";
// import { PrimeReactProvider } from 'primereact/api';

console.log(import.meta.env.MODE);
if (import.meta.env.MODE !== 'production') {
  // const _vConsole = new VConsole();
}

ReactDOM.createRoot(document.getElementById('root')!).render(
  // <React.StrictMode>
  <ThemeProvider defaultTheme="light" storageKey="theme">
    <App />
  </ThemeProvider>
  // </React.StrictMode>,
)
