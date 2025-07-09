import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header'; // Removed extension
import ContentRegistrationPage from './pages/ContentRegistrationPage'; // Removed extension
import ContentListPage from './pages/ContentListPage'; // Removed extension

const App: React.FC = () => { // Added React.FC type
  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/admin/contents/register" element={<ContentRegistrationPage />} /> {/* Path and element renamed */}
        <Route path="/admin/contents" element={<ContentListPage />} /> {/* Path and element renamed */}
        {/* Add other routes here */}
      </Routes>
    </Router>
  );
};

export default App;