#!/bin/bash

echo "🦫 DBeaver 설정 스크립트"
echo "=========================="

# 운영체제 확인
OS="$(uname -s)"
case "${OS}" in
    Linux*)     MACHINE=Linux;;
    Darwin*)    MACHINE=Mac;;
    CYGWIN*)    MACHINE=Cygwin;;
    MINGW*)     MACHINE=MinGw;;
    *)          MACHINE="UNKNOWN:${OS}"
esac

echo "📋 운영체제: $MACHINE"

# DBeaver 설치 확인
if command -v dbeaver &> /dev/null; then
    echo "✅ DBeaver가 이미 설치되어 있습니다."
else
    echo "⚠️  DBeaver가 설치되어 있지 않습니다."
    echo ""
    echo "📥 DBeaver 설치 방법:"
    echo "   1. https://dbeaver.io/ 에서 다운로드"
    echo "   2. 운영체제에 맞는 버전 선택:"
    
    case $MACHINE in
        "Mac")
            echo "      - macOS: .dmg 파일 다운로드"
            echo "      - 또는: brew install --cask dbeaver-community"
            ;;
        "Linux")
            echo "      - Linux: .tar.gz 파일 다운로드"
            echo "      - 또는: sudo snap install dbeaver-ce"
            echo "      - 또는: sudo apt install dbeaver-ce"
            ;;
        "MinGw"|"Cygwin")
            echo "      - Windows: .exe 파일 다운로드"
            ;;
    esac
    echo ""
fi

echo ""
echo "🔧 연결 설정 정보:"
echo "=================="

echo ""
echo "1️⃣ H2 데이터베이스 (개발 환경)"
echo "   연결 이름: Twothree H2 (Dev)"
echo "   URL: jdbc:h2:mem:testdb"
echo "   사용자 이름: sa"
echo "   비밀번호: (비어있음)"
echo "   드라이버: H2"

echo ""
echo "2️⃣ PostgreSQL 데이터베이스 (프로덕션 환경)"
echo "   연결 이름: Twothree PostgreSQL (Prod)"
echo "   호스트: localhost"
echo "   포트: 5432"
echo "   데이터베이스: twothree_db"
echo "   사용자 이름: twothree_user"
echo "   비밀번호: twothree_password"
echo "   드라이버: PostgreSQL"

echo ""
echo "🚀 환경 시작 방법:"
echo "=================="

echo ""
echo "개발 환경 (H2):"
echo "  1. cd backend"
echo "  2. ./gradlew bootRun"
echo "  3. DBeaver에서 H2 연결 테스트"

echo ""
echo "프로덕션 환경 (PostgreSQL):"
echo "  1. ./scripts/db-start.sh"
echo "  2. DBeaver에서 PostgreSQL 연결 테스트"

echo ""
echo "📁 유용한 파일들:"
echo "=================="
echo "  - DBeaver_Setup_Guide.md: 상세 설정 가이드"
echo "  - database-connections/sample-queries.sql: 샘플 쿼리"
echo "  - database-connections/dbeaver-connections.xml: 연결 설정"

echo ""
echo "💡 팁:"
echo "======"
echo "  - Ctrl + Enter: 쿼리 실행"
echo "  - Ctrl + Shift + Enter: 현재 쿼리만 실행"
echo "  - Ctrl + /: 주석 처리"
echo "  - Ctrl + Shift + F: SQL 포맷팅"

echo ""
echo "✅ 설정 완료! DBeaver를 실행하여 연결을 설정하세요." 