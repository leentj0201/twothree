import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css'; // Assuming a basic CSS file might exist or be created
import App from './App'; // Removed extension
import reportWebVitals from './reportWebVitals'; // Removed extension

const root = ReactDOM.createRoot(document.getElementById('root')!); // Added non-null assertion
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals(console.log); // Pass console.log as the callback
