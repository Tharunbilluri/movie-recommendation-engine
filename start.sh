#!/bin/bash

# Movie Recommendation System Startup Script

echo "🎬 Movie Recommendation System"
echo "================================"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 11 or higher."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1-2)
if [[ "$JAVA_VERSION" < "11" ]]; then
    echo "❌ Java 11 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java version: $JAVA_VERSION"
echo "✅ Maven found"

# Navigate to backend directory
cd backend

echo ""
echo "🔧 Building the application..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi

echo "✅ Build successful!"
echo ""
echo "🚀 Starting the server..."
echo "📍 Server will be available at: http://localhost:8080"
echo "🌐 Open your web browser and navigate to the URL above"
echo ""
echo "Press Ctrl+C to stop the server"
echo ""

# Start the server
mvn exec:java
