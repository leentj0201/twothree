import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import { store } from './store';
import Header from './components/Header';
import NotificationContainer from './components/NotificationContainer';
import HomePage from './pages/HomePage';
import ChurchListPage from './pages/ChurchListPage';
import './App.css';

function App() {
  return (
    <Provider store={store}>
      <Router>
        <div className="App">
          <Header />
          <NotificationContainer />
          <main className="main-content">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/churches" element={<ChurchListPage />} />
            </Routes>
          </main>
        </div>
      </Router>
    </Provider>
  );
}

export default App;