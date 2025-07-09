#!/bin/bash

echo "🚀 Twothree 데이터베이스 환경을 시작합니다..."

# Docker Compose로 PostgreSQL, pgAdmin, Redis 시작
echo "📦 Docker 컨테이너들을 시작합니다..."
docker-compose up -d

# 컨테이너 상태 확인
echo "⏳ 컨테이너들이 시작될 때까지 잠시 기다립니다..."
sleep 10

# 컨테이너 상태 출력
echo "📊 컨테이너 상태:"
docker-compose ps

echo ""
echo "✅ 데이터베이스 환경이 시작되었습니다!"
echo ""
echo "📋 접속 정보:"
echo "   PostgreSQL: localhost:5432"
echo "   Database: twothree_db"
echo "   Username: twothree_user"
echo "   Password: twothree_password"
echo ""
echo "   pgAdmin: http://localhost:5050"
echo "   Email: admin@twothree.com"
echo "   Password: admin123"
echo ""
echo "   Redis: localhost:6379"
echo "   Redis Commander: http://localhost:8081"
echo "   Adminer: http://localhost:8082"
echo ""
echo "🔧 다음 명령어로 Spring Boot 애플리케이션을 시작하세요:"
echo "   cd backend && ./gradlew bootRun --args='--spring.profiles.active=prod'" 