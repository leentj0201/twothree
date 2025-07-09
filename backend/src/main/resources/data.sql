-- Church 데이터
INSERT INTO church (name, address, phone, email, status, created_at, updated_at) 
VALUES ('새생명교회', '서울시 강남구 테헤란로 123', '02-1234-5678', 'church@example.com', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO church (name, address, phone, email, status, created_at, updated_at) 
VALUES ('은혜교회', '서울시 서초구 서초대로 456', '02-2345-6789', 'grace@example.com', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Department 데이터
INSERT INTO department (church_id, name, category, status, created_at, updated_at) 
VALUES (1, '예배부', 'WORSHIP', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO department (church_id, name, category, status, created_at, updated_at) 
VALUES (1, '청년부', 'YOUTH', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO department (church_id, name, category, status, created_at, updated_at) 
VALUES (2, '주일학교', 'SUNDAY_SCHOOL', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- User 데이터
INSERT INTO users (id, username, email, password, full_name, role, created_at, updated_at) 
VALUES (1, 'admin', 'admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '관리자', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (id, username, email, password, full_name, role, created_at, updated_at) 
VALUES (2, 'user', 'user@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '일반 사용자', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Member 데이터
INSERT INTO member (id, church_id, name, email, phone, address, birth_date, gender, status, role, profile_image_url, baptism_date, membership_date, notes, department_id, created_at, updated_at) 
VALUES (1, 1, '김목사', 'pastor@example.com', '010-1234-5678', '서울시 강남구', '1970-01-01', 'MALE', 'ACTIVE', 'PASTOR', NULL, NULL, NULL, '교회 설립자', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO member (id, church_id, name, email, phone, address, birth_date, gender, status, role, profile_image_url, baptism_date, membership_date, notes, department_id, created_at, updated_at) 
VALUES (2, 1, '이집사', 'elder@example.com', '010-2345-6789', '서울시 서초구', '1980-05-15', 'FEMALE', 'ACTIVE', 'ELDER', NULL, NULL, NULL, '재정 담당', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO member (id, church_id, name, email, phone, address, birth_date, gender, status, role, profile_image_url, baptism_date, membership_date, notes, department_id, created_at, updated_at) 
VALUES (3, 2, '박청년', 'youth@example.com', '010-3456-7890', '경기도 성남시', '1995-11-20', 'MALE', 'ACTIVE', 'MEMBER', NULL, NULL, NULL, '청년부 소속', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- MemberDepartment 데이터
INSERT INTO member_department (member_id, department_id, created_at, updated_at) 
VALUES (1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO member_department (member_id, department_id, created_at, updated_at) 
VALUES (2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO member_department (member_id, department_id, created_at, updated_at) 
VALUES (3, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);