-- PostgreSQL 초기화 스크립트
-- 이 스크립트는 PostgreSQL 컨테이너가 처음 시작될 때 실행됩니다.

-- 데이터베이스 생성 (이미 docker-compose에서 생성됨)
-- CREATE DATABASE twothree_db;

-- 사용자 권한 설정
GRANT ALL PRIVILEGES ON DATABASE twothree_db TO twothree_user;

-- 확장 기능 활성화 (필요한 경우)
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 테이블 생성은 JPA가 자동으로 처리하므로 여기서는 생략
-- 샘플 데이터는 data.sql에서 처리됨 