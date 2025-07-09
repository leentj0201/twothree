import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../store/hooks';
import {
  fetchChurches,
  searchChurches,
  deleteChurch,
  selectChurches,
  selectChurchLoading,
  selectChurchError,
  selectSearchKeyword,
  selectSelectedStatus,
  selectFilteredChurches,
  setSearchKeyword,
  setSelectedStatus,
  clearError,
} from '../store/slices/churchSlice';
import { addNotification } from '../store/slices/uiSlice';
import { Church, ChurchStatus } from '../types';

const ChurchListPage: React.FC = () => {
  const dispatch = useAppDispatch();
  const churches = useAppSelector(selectFilteredChurches);
  const loading = useAppSelector(selectChurchLoading);
  const error = useAppSelector(selectChurchError);
  const searchKeyword = useAppSelector(selectSearchKeyword);
  const selectedStatus = useAppSelector(selectSelectedStatus);

  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [churchToDelete, setChurchToDelete] = useState<Church | null>(null);

  useEffect(() => {
    dispatch(fetchChurches());
  }, [dispatch]);

  useEffect(() => {
    if (error) {
      dispatch(addNotification({
        type: 'error',
        message: error,
      }));
      dispatch(clearError());
    }
  }, [error, dispatch]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchKeyword.trim()) {
      dispatch(searchChurches(searchKeyword.trim()));
    } else {
      dispatch(fetchChurches());
    }
  };

  const handleStatusFilter = (status: string) => {
    dispatch(setSelectedStatus(status));
  };

  const handleDeleteClick = (church: Church) => {
    setChurchToDelete(church);
    setShowDeleteModal(true);
  };

  const handleDeleteConfirm = async () => {
    if (churchToDelete) {
      try {
        await dispatch(deleteChurch(churchToDelete.id)).unwrap();
        dispatch(addNotification({
          type: 'success',
          message: '교회가 성공적으로 삭제되었습니다.',
        }));
      } catch (error) {
        dispatch(addNotification({
          type: 'error',
          message: '교회 삭제에 실패했습니다.',
        }));
      }
    }
    setShowDeleteModal(false);
    setChurchToDelete(null);
  };

  const getStatusBadge = (status: ChurchStatus) => {
    const statusConfig = {
      [ChurchStatus.ACTIVE]: { label: '활성', className: 'badge-success' },
      [ChurchStatus.INACTIVE]: { label: '비활성', className: 'badge-danger' },
      [ChurchStatus.PENDING]: { label: '대기', className: 'badge-warning' },
    };

    const config = statusConfig[status];
    return <span className={`badge ${config.className}`}>{config.label}</span>;
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner"></div>
        <p>교회 목록을 불러오는 중...</p>
      </div>
    );
  }

  return (
    <div className="church-list-page">
      <div className="container">
        <div className="page-header">
          <h1>교회 목록</h1>
          <button className="btn btn-primary">새 교회 등록</button>
        </div>

        {/* 검색 및 필터 */}
        <div className="search-filter-section">
          <form onSubmit={handleSearch} className="search-form">
            <input
              type="text"
              placeholder="교회명으로 검색..."
              value={searchKeyword}
              onChange={(e) => dispatch(setSearchKeyword(e.target.value))}
              className="search-input"
            />
            <button type="submit" className="btn btn-secondary">검색</button>
          </form>

          <div className="filter-buttons">
            <button
              className={`btn ${selectedStatus === 'ALL' ? 'btn-primary' : 'btn-outline'}`}
              onClick={() => handleStatusFilter('ALL')}
            >
              전체
            </button>
            <button
              className={`btn ${selectedStatus === ChurchStatus.ACTIVE ? 'btn-primary' : 'btn-outline'}`}
              onClick={() => handleStatusFilter(ChurchStatus.ACTIVE)}
            >
              활성
            </button>
            <button
              className={`btn ${selectedStatus === ChurchStatus.INACTIVE ? 'btn-primary' : 'btn-outline'}`}
              onClick={() => handleStatusFilter(ChurchStatus.INACTIVE)}
            >
              비활성
            </button>
            <button
              className={`btn ${selectedStatus === ChurchStatus.PENDING ? 'btn-primary' : 'btn-outline'}`}
              onClick={() => handleStatusFilter(ChurchStatus.PENDING)}
            >
              대기
            </button>
          </div>
        </div>

        {/* 교회 목록 */}
        <div className="church-grid">
          {churches.map((church) => (
            <div key={church.id} className="church-card">
              <div className="church-header">
                <h3>{church.name}</h3>
                {getStatusBadge(church.status)}
              </div>
              <div className="church-info">
                <p><strong>주소:</strong> {church.address}</p>
                <p><strong>전화:</strong> {church.phone}</p>
                <p><strong>이메일:</strong> {church.email}</p>
                <p><strong>담임목사:</strong> {church.pastorName}</p>
              </div>
              <div className="church-actions">
                <button className="btn btn-sm btn-primary">수정</button>
                <button 
                  className="btn btn-sm btn-danger"
                  onClick={() => handleDeleteClick(church)}
                >
                  삭제
                </button>
              </div>
            </div>
          ))}
        </div>

        {churches.length === 0 && (
          <div className="empty-state">
            <p>등록된 교회가 없습니다.</p>
          </div>
        )}
      </div>

      {/* 삭제 확인 모달 */}
      {showDeleteModal && churchToDelete && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>교회 삭제 확인</h3>
            <p>"{churchToDelete.name}" 교회를 삭제하시겠습니까?</p>
            <p>이 작업은 되돌릴 수 없습니다.</p>
            <div className="modal-actions">
              <button 
                className="btn btn-danger"
                onClick={handleDeleteConfirm}
              >
                삭제
              </button>
              <button 
                className="btn btn-secondary"
                onClick={() => setShowDeleteModal(false)}
              >
                취소
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ChurchListPage; 