-- =====================================================
-- Twothree 프로젝트 샘플 쿼리 모음
-- DBeaver에서 사용하기 위한 SQL 스크립트
-- =====================================================

-- 1. 기본 데이터 확인
-- =====================================================

-- 모든 교회 조회
SELECT * FROM church;

-- 모든 부서 조회
SELECT * FROM department;

-- 모든 멤버 조회
SELECT * FROM member;

-- 모든 사용자 조회
SELECT * FROM "user";

-- 2. 교회별 통계
-- =====================================================

-- 교회별 부서 수
SELECT 
    c.name as church_name,
    c.address,
    COUNT(d.id) as department_count
FROM church c
LEFT JOIN department d ON c.id = d.church_id
GROUP BY c.id, c.name, c.address
ORDER BY department_count DESC;

-- 교회별 멤버 수
SELECT 
    c.name as church_name,
    COUNT(m.id) as member_count
FROM church c
LEFT JOIN member m ON c.id = m.church_id
GROUP BY c.id, c.name
ORDER BY member_count DESC;

-- 교회별 멤버 역할별 통계
SELECT 
    c.name as church_name,
    m.role as member_role,
    COUNT(*) as count
FROM church c
JOIN member m ON c.id = m.church_id
GROUP BY c.id, c.name, m.role
ORDER BY c.name, m.role;

-- 3. 부서별 분석
-- =====================================================

-- 부서별 멤버 조회
SELECT 
    c.name as church_name,
    d.name as department_name,
    d.category as department_category,
    m.name as member_name,
    m.role as member_role,
    m.phone as member_phone
FROM department d
JOIN church c ON d.church_id = c.id
JOIN member_department md ON d.id = md.department_id
JOIN member m ON md.member_id = m.id
ORDER BY c.name, d.name, m.name;

-- 부서별 멤버 수
SELECT 
    c.name as church_name,
    d.name as department_name,
    d.category as department_category,
    COUNT(md.member_id) as member_count
FROM department d
JOIN church c ON d.church_id = c.id
LEFT JOIN member_department md ON d.id = md.department_id
GROUP BY c.id, c.name, d.id, d.name, d.category
ORDER BY c.name, member_count DESC;

-- 4. 멤버 분석
-- =====================================================

-- 멤버별 소속 부서
SELECT 
    m.name as member_name,
    m.role as member_role,
    c.name as church_name,
    STRING_AGG(d.name, ', ') as departments
FROM member m
JOIN church c ON m.church_id = c.id
LEFT JOIN member_department md ON m.id = md.member_id
LEFT JOIN department d ON md.department_id = d.id
GROUP BY m.id, m.name, m.role, c.name
ORDER BY c.name, m.name;

-- 역할별 멤버 수
SELECT 
    m.role as member_role,
    COUNT(*) as count
FROM member m
GROUP BY m.role
ORDER BY count DESC;

-- 5. 사용자 분석
-- =====================================================

-- 사용자별 교회 정보
SELECT 
    u.email,
    u.role as user_role,
    c.name as church_name,
    m.name as member_name
FROM "user" u
LEFT JOIN member m ON u.id = m.user_id
LEFT JOIN church c ON m.church_id = c.id
ORDER BY u.email;

-- 6. 고급 분석 쿼리
-- =====================================================

-- 교회별 종합 통계
SELECT 
    c.name as church_name,
    c.address,
    c.phone,
    COUNT(DISTINCT d.id) as department_count,
    COUNT(DISTINCT m.id) as member_count,
    COUNT(DISTINCT u.id) as user_count
FROM church c
LEFT JOIN department d ON c.id = d.church_id
LEFT JOIN member m ON c.id = m.church_id
LEFT JOIN "user" u ON m.user_id = u.id
GROUP BY c.id, c.name, c.address, c.phone
ORDER BY member_count DESC;

-- 부서 카테고리별 통계
SELECT 
    d.category as department_category,
    COUNT(d.id) as department_count,
    COUNT(md.member_id) as total_members
FROM department d
LEFT JOIN member_department md ON d.id = md.department_id
GROUP BY d.category
ORDER BY total_members DESC;

-- 7. 데이터 검증 쿼리
-- =====================================================

-- 고아 데이터 확인 (부서에 멤버가 없는 경우)
SELECT 
    c.name as church_name,
    d.name as department_name
FROM department d
JOIN church c ON d.church_id = c.id
LEFT JOIN member_department md ON d.id = md.department_id
WHERE md.member_id IS NULL;

-- 사용자 계정이 없는 멤버
SELECT 
    m.name as member_name,
    c.name as church_name
FROM member m
JOIN church c ON m.church_id = c.id
WHERE m.user_id IS NULL;

-- 8. 유틸리티 쿼리
-- =====================================================

-- 테이블 크기 확인
SELECT 
    schemaname,
    tablename,
    attname,
    n_distinct,
    correlation
FROM pg_stats 
WHERE schemaname = 'public'
ORDER BY tablename, attname;

-- 최근 생성된 데이터
SELECT 
    'church' as table_name,
    name,
    created_at
FROM church
UNION ALL
SELECT 
    'department' as table_name,
    name,
    created_at
FROM department
UNION ALL
SELECT 
    'member' as table_name,
    name,
    created_at
FROM member
ORDER BY created_at DESC
LIMIT 10;

-- =====================================================
-- 쿼리 실행 팁:
-- 1. Ctrl + Enter: 전체 쿼리 실행
-- 2. Ctrl + Shift + Enter: 현재 커서 위치의 쿼리만 실행
-- 3. Ctrl + /: 주석 처리/해제
-- 4. Ctrl + Shift + F: SQL 포맷팅
-- ===================================================== 