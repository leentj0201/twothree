#!/bin/bash

echo "🚀 Deploying Twothree Backend to INT environment..."

# Stop existing containers
echo "📦 Stopping existing containers..."
docker-compose --profile int down

# Build and start containers
echo "🔨 Building and starting containers..."
docker-compose --profile int up --build -d

# Wait for application to start
echo "⏳ Waiting for application to start..."
sleep 30

# Check if application is running
echo "🔍 Checking application status..."
if curl -f http://localhost:8081/api/test/health > /dev/null 2>&1; then
    echo "✅ Application is running successfully on http://localhost:8081"
    echo "🔗 API Documentation: http://localhost:8081/swagger-ui.html"
    echo "📊 Database: PostgreSQL running on localhost:5433"
else
    echo "❌ Application failed to start properly"
    echo "📋 Checking logs..."
    docker-compose --profile int logs twothree-backend-int
    exit 1
fi

echo "🎉 INT environment deployment completed!" 