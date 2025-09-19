@echo off
title Movie Recommendation System

echo 🎬 Movie Recommendation System
echo ================================

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed. Please install Java 11 or higher.
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven is not installed. Please install Maven 3.6 or higher.
    pause
    exit /b 1
)

echo ✅ Java and Maven found

REM Navigate to backend directory
cd backend

echo.
echo 🔧 Building the application...
mvn clean compile

if %errorlevel% neq 0 (
    echo ❌ Build failed. Please check the error messages above.
    pause
    exit /b 1
)

echo ✅ Build successful!
echo.
echo 🚀 Starting the server...
echo 📍 Server will be available at: http://localhost:8080
echo 🌐 Open your web browser and navigate to the URL above
echo.
echo Press Ctrl+C to stop the server
echo.

REM Start the server
mvn exec:java

pause
