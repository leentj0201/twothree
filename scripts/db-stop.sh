#!/bin/bash

echo "🛑 Twothree 데이터베이스 환경을 중지합니다..."

# Docker Compose 컨테이너들 중지
echo "📦 Docker 컨테이너들을 중지합니다..."
docker-compose down

echo "✅ 데이터베이스 환경이 중지되었습니다!"
echo ""
echo "💡 데이터를 완전히 삭제하려면 다음 명령어를 실행하세요:"
echo "   docker-compose down -v" 