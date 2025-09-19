#!/bin/bash

# Manual compilation script (when Maven is not available)

echo "🎬 Movie Recommendation System - Manual Compilation"
echo "=================================================="

# Check if Java is installed
if ! command -v javac &> /dev/null; then
    echo "❌ Java compiler (javac) is not installed."
    echo "Please install Java Development Kit (JDK) 11 or higher."
    exit 1
fi

echo "✅ Java compiler found"

# Create output directory
mkdir -p backend/target/classes

# Download Gson dependency (if not available)
GSON_JAR="backend/lib/gson-2.10.1.jar"
if [ ! -f "$GSON_JAR" ]; then
    echo "📦 Downloading Gson dependency..."
    mkdir -p backend/lib
    curl -L "https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar" -o "$GSON_JAR"
fi

# Compile Java files
echo "🔧 Compiling Java files..."
javac -cp "$GSON_JAR" -d backend/target/classes backend/src/main/java/com/recommendation/*.java

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed."
    exit 1
fi

echo "✅ Compilation successful!"
echo ""
echo "🚀 To run the server:"
echo "java -cp \"backend/target/classes:$GSON_JAR\" com.recommendation.RecommendationServer"
echo ""
echo "📍 Then open http://localhost:8080 in your browser"
