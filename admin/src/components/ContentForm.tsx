import React, { useState } from 'react';

interface ContentData {
  title: string;
  content: string;
}

interface ContentFormProps {
  onSubmit: (data: ContentData) => void;
}

const ContentForm: React.FC<ContentFormProps> = ({ onSubmit }) => { // Added React.FC with props type
  const [title, setTitle] = useState<string>(''); // Added type for useState
  const [content, setContent] = useState<string>(''); // Added type for useState

  const handleSubmit = (e: React.FormEvent) => { // Added type for event
    e.preventDefault();
    onSubmit({ title, content });
  };

  return (
    <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px', maxWidth: '500px', margin: '20px 0' }}>
      <div>
        <label htmlFor="title" style={{ display: 'block', marginBottom: '5px' }}>제목:</label>
        <input
          type="text"
          id="title"
          value={title}
          onChange={(e: React.ChangeEvent<HTMLInputElement>) => setTitle(e.target.value)} // Added type for event
          style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }}
          required
        />
      </div>
      <div>
        <label htmlFor="content" style={{ display: 'block', marginBottom: '5px' }}>내용:</label>
        <textarea
          id="content"
          value={content}
          onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) => setContent(e.target.value)} // Added type for event
          style={{ width: '100%', padding: '8px', boxSizing: 'border-box', minHeight: '150px' }}
          required
        ></textarea>
      </div>
      <button type="submit" style={{ padding: '10px 15px', background: '#007bff', color: 'white', border: 'none', cursor: 'pointer' }}>콘텐츠 등록</button> {/* Renamed button text */}
    </form>
  );
};

export default ContentForm; // Renamed export