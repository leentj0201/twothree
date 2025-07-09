import React from 'react';
import { Link } from 'react-router-dom';

const Header: React.FC = () => { // Added React.FC type
  return (
    <header style={{ padding: '20px', background: '#f0f0f0', borderBottom: '1px solid #ccc' }}>
      <nav>
        <ul style={{ listStyle: 'none', padding: 0, margin: 0, display: 'flex', gap: '20px' }}>
          <li>
            <Link to="/admin/contents/register">콘텐츠 등록</Link> {/* Text and path renamed */}
          </li>
          <li>
            <Link to="/admin/contents">콘텐츠 목록</Link> {/* Text and path renamed */}
          </li>
          {/* Add other navigation links here */}
        </ul>
      </nav>
    </header>
  );
};

export default Header;