import React from 'react';
import { Link } from 'react-router-dom';

const Header: React.FC = () => {
  return (
    <header style={{ 
      padding: '20px', 
      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', 
      borderBottom: '1px solid #ccc',
      color: 'white'
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div style={{ fontSize: '24px', fontWeight: 'bold' }}>
          교회 관리 시스템
        </div>
        <nav>
          <ul style={{ listStyle: 'none', padding: 0, margin: 0, display: 'flex', gap: '20px' }}>
            <li>
              <Link to="/admin/churches" style={{ color: 'white', textDecoration: 'none', fontWeight: 'bold' }}>
                교회 관리
              </Link>
            </li>
            <li>
              <Link to="/admin/contents" style={{ color: 'white', textDecoration: 'none', fontWeight: 'bold' }}>
                콘텐츠 관리
              </Link>
            </li>
            <li>
              <Link to="/admin/contents/register" style={{ color: 'white', textDecoration: 'none', fontWeight: 'bold' }}>
                콘텐츠 등록
              </Link>
            </li>
          </ul>
        </nav>
      </div>
    </header>
  );
};

export default Header;