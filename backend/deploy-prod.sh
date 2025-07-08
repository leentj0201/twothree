#!/bin/bash

echo "🚀 Deploying Twothree Backend to PROD environment..."

# Stop existing containers
echo "📦 Stopping existing containers..."
docker-compose --profile prod down

# Build and start containers
echo "🔨 Building and starting containers..."
docker-compose --profile prod up --build -d

# Wait for database to be ready
echo "⏳ Waiting for database to be ready..."
sleep 15

# Wait for application to start
echo "⏳ Waiting for application to start..."
sleep 30

# Check if application is running
echo "🔍 Checking application status..."
if curl -f http://localhost:8080/api/test/health > /dev/null 2>&1; then
    echo "✅ Application is running successfully on http://localhost:8080"
    echo "🔗 API Documentation: http://localhost:8080/swagger-ui.html"
    echo "📊 Database: PostgreSQL running on localhost:5432"
else
    echo "❌ Application failed to start properly"
    echo "📋 Checking logs..."
    docker-compose --profile prod logs twothree-backend-prod
    exit 1
fi

echo "🎉 PROD environment deployment completed!" 