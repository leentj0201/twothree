import React from 'react';
import ContentForm from '../components/ContentForm'; // Removed extension

interface ContentData { // Defined interface for content data
  title: string;
  content: string;
}

const ContentRegistrationPage: React.FC = () => { // Added React.FC type
  const handleContentSubmit = (contentData: ContentData) => { // Added type for parameter
    console.log('콘텐츠 등록 데이터:', contentData); // Renamed console log
    // Here you would typically send this data to your backend API
    alert('콘텐츠가 성공적으로 등록되었습니다! (콘솔 확인)'); // Renamed alert message
  };

  return (
    <div style={{ padding: '20px' }}>
      <h1>콘텐츠 등록</h1> {/* Renamed heading */}
      <ContentForm onSubmit={handleContentSubmit} /> {/* Renamed component */}
    </div>
  );
};

export default ContentRegistrationPage; // Renamed export