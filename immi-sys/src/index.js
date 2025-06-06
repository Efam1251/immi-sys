import React from 'react';
import ReactDOM from 'react-dom/client';  // Change the import to 'react-dom/client'
import App from './App';
import * as serviceWorker from './serviceWorker';
import { BrowserRouter } from 'react-router-dom';
import './css/index.css';

// Create a root element using createRoot
const root = ReactDOM.createRoot(document.getElementById('root'));

// Render the app inside the root
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </React.StrictMode>
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
