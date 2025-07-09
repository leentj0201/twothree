#!/bin/bash

# API 테스트 스크립트
# 사용법: ./scripts/test-api.sh

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 기본 설정
BASE_URL="http://localhost:8080"
API_BASE="$BASE_URL/api"

# 로그 함수
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 서버 상태 확인
check_server() {
    log_info "서버 상태 확인 중..."
    if curl -s "$BASE_URL/actuator/health" > /dev/null; then
        log_success "서버가 정상적으로 실행 중입니다."
        return 0
    else
        log_error "서버에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요."
        return 1
    fi
}

# 교회 API 테스트
test_church_api() {
    log_info "교회 API 테스트 시작..."
    
    # 1. 교회 목록 조회
    log_info "1. 교회 목록 조회 테스트"
    response=$(curl -s -X POST "$API_BASE/churches/list" \
        -H "Content-Type: application/json" \
        -d '{}')
    echo "응답: $response"
    
    # 2. 교회 등록
    log_info "2. 교회 등록 테스트"
    response=$(curl -s -X POST "$API_BASE/churches/create" \
        -H "Content-Type: application/json" \
        -d '{
            "name": "테스트 교회",
            "description": "API 테스트용 교회",
            "address": "서울시 강남구 테스트로 123",
            "phone": "02-1234-5678",
            "email": "test@church.com",
            "pastorName": "테스트 목사",
            "pastorPhone": "010-1234-5678",
            "pastorEmail": "pastor@test.com",
            "status": "ACTIVE"
        }')
    echo "응답: $response"
    
    # 등록된 교회 ID 추출 (간단한 방법)
    church_id=$(echo $response | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    
    if [ -n "$church_id" ]; then
        log_success "교회가 성공적으로 등록되었습니다. ID: $church_id"
        
        # 3. 교회 상세 조회
        log_info "3. 교회 상세 조회 테스트"
        response=$(curl -s -X POST "$API_BASE/churches/get" \
            -H "Content-Type: application/json" \
            -d "{\"churchId\": $church_id}")
        echo "응답: $response"
        
        # 4. 교회 검색
        log_info "4. 교회 검색 테스트"
        response=$(curl -s -X POST "$API_BASE/churches/search" \
            -H "Content-Type: application/json" \
            -d '{"keyword": "테스트"}')
        echo "응답: $response"
        
        # 5. 교회 정보 수정
        log_info "5. 교회 정보 수정 테스트"
        response=$(curl -s -X POST "$API_BASE/churches/update" \
            -H "Content-Type: application/json" \
            -d "{
                \"churchId\": $church_id,
                \"churchDto\": {
                    \"name\": \"테스트 교회 (수정됨)\",
                    \"description\": \"API 테스트용 교회 - 수정됨\",
                    \"address\": \"서울시 강남구 테스트로 456\",
                    \"phone\": \"02-1234-5679\",
                    \"email\": \"test-updated@church.com\",
                    \"pastorName\": \"테스트 목사\",
                    \"pastorPhone\": \"010-1234-5679\",
                    \"pastorEmail\": \"pastor-updated@test.com\",
                    \"status\": \"ACTIVE\"
                }
            }")
        echo "응답: $response"
        
        # 6. 교회 삭제
        log_info "6. 교회 삭제 테스트"
        response=$(curl -s -X POST "$API_BASE/churches/delete" \
            -H "Content-Type: application/json" \
            -d "{\"churchId\": $church_id}")
        echo "응답: $response"
        
    else
        log_warning "교회 등록 실패 또는 ID 추출 실패"
    fi
    
    # 7. 중복 확인 테스트
    log_info "7. 교회명 중복 확인 테스트"
    response=$(curl -s -X POST "$API_BASE/churches/check-name" \
        -H "Content-Type: application/json" \
        -d '{"name": "감리교회"}')
    echo "응답: $response"
}

# 멤버 API 테스트
test_member_api() {
    log_info "멤버 API 테스트 시작..."
    
    # 먼저 교회를 등록
    log_info "테스트용 교회 등록 중..."
    church_response=$(curl -s -X POST "$API_BASE/churches/create" \
        -H "Content-Type: application/json" \
        -d '{
            "name": "멤버 테스트 교회",
            "description": "멤버 API 테스트용 교회",
            "address": "서울시 강남구 멤버테스트로 123",
            "phone": "02-1234-5678",
            "email": "member-test@church.com",
            "pastorName": "멤버 테스트 목사",
            "pastorPhone": "010-1234-5678",
            "pastorEmail": "pastor@member-test.com",
            "status": "ACTIVE"
        }')
    
    church_id=$(echo $church_response | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    
    if [ -n "$church_id" ]; then
        log_success "테스트용 교회 등록 완료. ID: $church_id"
        
        # 1. 멤버 등록
        log_info "1. 멤버 등록 테스트"
        response=$(curl -s -X POST "$API_BASE/members/create" \
            -H "Content-Type: application/json" \
            -d "{
                \"name\": \"테스트 멤버\",
                \"email\": \"member@test.com\",
                \"phone\": \"010-1234-5678\",
                \"churchId\": $church_id,
                \"role\": \"MEMBER\",
                \"status\": \"ACTIVE\"
            }")
        echo "응답: $response"
        
        # 등록된 멤버 ID 추출
        member_id=$(echo $response | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
        
        if [ -n "$member_id" ]; then
            log_success "멤버가 성공적으로 등록되었습니다. ID: $member_id"
            
            # 2. 멤버 상세 조회
            log_info "2. 멤버 상세 조회 테스트"
            response=$(curl -s -X POST "$API_BASE/members/get" \
                -H "Content-Type: application/json" \
                -d "{\"memberId\": $member_id}")
            echo "응답: $response"
            
            # 3. 교회별 멤버 목록
            log_info "3. 교회별 멤버 목록 테스트"
            response=$(curl -s -X POST "$API_BASE/members/list-by-church" \
                -H "Content-Type: application/json" \
                -d "{\"churchId\": $church_id}")
            echo "응답: $response"
            
            # 4. 멤버 검색
            log_info "4. 멤버 검색 테스트"
            response=$(curl -s -X POST "$API_BASE/members/search-by-church" \
                -H "Content-Type: application/json" \
                -d "{
                    \"churchId\": $church_id,
                    \"keyword\": \"테스트\"
                }")
            echo "응답: $response"
            
            # 5. 멤버 삭제
            log_info "5. 멤버 삭제 테스트"
            response=$(curl -s -X POST "$API_BASE/members/delete" \
                -H "Content-Type: application/json" \
                -d "{\"memberId\": $member_id}")
            echo "응답: $response"
        fi
        
        # 테스트용 교회도 삭제
        log_info "테스트용 교회 삭제 중..."
        curl -s -X POST "$API_BASE/churches/delete" \
            -H "Content-Type: application/json" \
            -d "{\"churchId\": $church_id}" > /dev/null
    fi
}

# 부서 API 테스트
test_department_api() {
    log_info "부서 API 테스트 시작..."
    
    # 먼저 교회를 등록
    log_info "테스트용 교회 등록 중..."
    church_response=$(curl -s -X POST "$API_BASE/churches/create" \
        -H "Content-Type: application/json" \
        -d '{
            "name": "부서 테스트 교회",
            "description": "부서 API 테스트용 교회",
            "address": "서울시 강남구 부서테스트로 123",
            "phone": "02-1234-5678",
            "email": "dept-test@church.com",
            "pastorName": "부서 테스트 목사",
            "pastorPhone": "010-1234-5678",
            "pastorEmail": "pastor@dept-test.com",
            "status": "ACTIVE"
        }')
    
    church_id=$(echo $church_response | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    
    if [ -n "$church_id" ]; then
        log_success "테스트용 교회 등록 완료. ID: $church_id"
        
        # 1. 부서 등록
        log_info "1. 부서 등록 테스트"
        response=$(curl -s -X POST "$API_BASE/departments/create" \
            -H "Content-Type: application/json" \
            -d "{
                \"name\": \"테스트 부서\",
                \"description\": \"API 테스트용 부서\",
                \"churchId\": $church_id,
                \"category\": \"MINISTRY\",
                \"status\": \"ACTIVE\"
            }")
        echo "응답: $response"
        
        # 등록된 부서 ID 추출
        department_id=$(echo $response | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
        
        if [ -n "$department_id" ]; then
            log_success "부서가 성공적으로 등록되었습니다. ID: $department_id"
            
            # 2. 부서 상세 조회
            log_info "2. 부서 상세 조회 테스트"
            response=$(curl -s -X POST "$API_BASE/departments/get" \
                -H "Content-Type: application/json" \
                -d "{\"departmentId\": $department_id}")
            echo "응답: $response"
            
            # 3. 교회별 부서 목록
            log_info "3. 교회별 부서 목록 테스트"
            response=$(curl -s -X POST "$API_BASE/departments/list-by-church" \
                -H "Content-Type: application/json" \
                -d "{\"churchId\": $church_id}")
            echo "응답: $response"
            
            # 4. 부서 삭제
            log_info "4. 부서 삭제 테스트"
            response=$(curl -s -X POST "$API_BASE/departments/delete" \
                -H "Content-Type: application/json" \
                -d "{\"departmentId\": $department_id}")
            echo "응답: $response"
        fi
        
        # 테스트용 교회도 삭제
        log_info "테스트용 교회 삭제 중..."
        curl -s -X POST "$API_BASE/churches/delete" \
            -H "Content-Type: application/json" \
            -d "{\"churchId\": $church_id}" > /dev/null
    fi
}

# 메인 실행 함수
main() {
    log_info "API 테스트 시작"
    log_info "기본 URL: $BASE_URL"
    
    # 서버 상태 확인
    if ! check_server; then
        exit 1
    fi
    
    # 각 API 테스트 실행
    test_church_api
    echo ""
    test_member_api
    echo ""
    test_department_api
    
    log_success "모든 API 테스트가 완료되었습니다."
}

# 스크립트 실행
main "$@" 