import React from 'react';
import { useAppSelector } from '../store/hooks';
import { selectChurchStatistics } from '../store/slices/churchSlice';

const HomePage: React.FC = () => {
  const churchStats = useAppSelector(selectChurchStatistics);

  return (
    <div className="home-page">
      <div className="container">
        <h1>교회 관리 시스템</h1>
        <p>교회, 멤버, 부서를 효율적으로 관리하세요.</p>
        
        <div className="stats-grid">
          <div className="stat-card">
            <h3>전체 교회</h3>
            <p className="stat-number">{churchStats.total}</p>
          </div>
          <div className="stat-card">
            <h3>활성 교회</h3>
            <p className="stat-number">{churchStats.active}</p>
          </div>
          <div className="stat-card">
            <h3>비활성 교회</h3>
            <p className="stat-number">{churchStats.inactive}</p>
          </div>
          <div className="stat-card">
            <h3>대기 교회</h3>
            <p className="stat-number">{churchStats.pending}</p>
          </div>
        </div>

        <div className="quick-actions">
          <h2>빠른 작업</h2>
          <div className="action-buttons">
            <button className="btn btn-primary">새 교회 등록</button>
            <button className="btn btn-secondary">멤버 관리</button>
            <button className="btn btn-secondary">부서 관리</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage; 