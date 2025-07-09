#!/bin/bash

echo "🔄 Twothree 데이터베이스를 초기화합니다..."

# 컨테이너와 볼륨 모두 삭제
echo "🗑️ 기존 컨테이너와 데이터를 삭제합니다..."
docker-compose down -v

# 컨테이너 재시작
echo "🚀 새로운 컨테이너를 시작합니다..."
docker-compose up -d

# 초기화 완료 대기
echo "⏳ 데이터베이스 초기화를 기다립니다..."
sleep 15

echo "✅ 데이터베이스가 초기화되었습니다!"
echo ""
echo "📋 접속 정보:"
echo "   PostgreSQL: localhost:5432"
echo "   pgAdmin: http://localhost:5050"
echo ""
echo "🔧 Spring Boot 애플리케이션을 시작하세요:"
echo "   cd backend && ./gradlew bootRun --args='--spring.profiles.active=prod'" 