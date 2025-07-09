import React, { useState, useEffect } from 'react';

interface Content { // Defined interface for Content
  id: number;
  title: string;
  content: string;
  createdAt: string;
}

const ContentListPage: React.FC = () => { // Added React.FC type
  const [contents, setContents] = useState<Content[]>([]); // Added type for useState
  const [loading, setLoading] = useState<boolean>(true); // Added type for useState
  const [error, setError] = useState<string | null>(null); // Added type for useState

  useEffect(() => {
    // In a real application, you would fetch data from your backend API here.
    // For now, we'll use dummy data.
    const fetchContents = () => { // Renamed function
      setLoading(true);
      setError(null);
      try {
        const dummyContents: Content[] = [ // Added type for dummy data
          { id: 1, title: '첫 번째 콘텐츠', content: '이것은 첫 번째 콘텐츠의 내용입니다.', createdAt: '2023-01-15' }, // Renamed dummy data
          { id: 2, title: '두 번째 콘텐츠', content: '이것은 두 번째 콘텐츠의 내용입니다.', createdAt: '2023-02-20' },
          { id: 3, title: '세 번째 콘텐츠', content: '이것은 세 번째 콘텐츠의 내용입니다.', createdAt: '2023-03-10' },
        ];
        setContents(dummyContents); // Renamed state setter
      } catch (err: any) { // Added type for error
        setError('콘텐츠를 불러오는 데 실패했습니다.'); // Renamed error message
        console.error(err);
      } finally { // Correctly placed finally block
        setLoading(false);
      }
    };

    fetchContents(); // Renamed function call
  }, []);

  if (loading) {
    return <div style={{ padding: '20px' }}>콘텐츠를 불러오는 중...</div>; // Renamed loading message
  }

  if (error) {
    return <div style={{ padding: '20px', color: 'red' }}>오류: {error}</div>; // Renamed error message
  }

  return (
    <div style={{ padding: '20px' }}>
      <h1>콘텐츠 목록</h1> {/* Renamed heading */}
      {contents.length === 0 ? ( // Renamed state variable
        <p>콘텐츠가 없습니다.</p> // Renamed message
      ) : (
        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
          <thead>
            <tr style={{ background: '#f2f2f2' }}>
              <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>ID</th>
              <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>제목</th>
              <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>작성일</th>
              <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>작업</th>
            </tr>
          </thead>
          <tbody>
            {contents.map((content) => ( // Renamed map variable
              <tr key={content.id}>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>{content.id}</td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>{content.title}</td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>{content.createdAt}</td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                  <button style={{ marginRight: '5px' }}>수정</button>
                  <button>삭제</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default ContentListPage; // Renamed export