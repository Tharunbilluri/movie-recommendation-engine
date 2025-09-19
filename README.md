# Movie Recommendation System

A comprehensive movie recommendation system built with Java backend and modern web frontend using HTML, CSS, and JavaScript.

## Features

- **Multiple Recommendation Algorithms**:
  - Content-based filtering (by genre)
  - Collaborative filtering (user-based)
  - Popular movies recommendation

- **Interactive Web Interface**:
  - Modern, responsive design
  - User selection and preferences
  - Movie rating system
  - Real-time recommendations

- **Backend API**:
  - RESTful API endpoints
  - In-memory data storage
  - CORS support for web frontend

## Technology Stack

- **Backend**: Java 11+ with built-in HTTP server
- **Frontend**: HTML5, CSS3, JavaScript (ES6+)
- **Data Format**: JSON
- **Build Tool**: Maven
- **Dependencies**: Gson for JSON processing

## Project Structure

```
recommendation-system/
├── backend/
│   ├── src/main/java/com/recommendation/
│   │   ├── Movie.java                 # Movie data model
│   │   ├── User.java                  # User data model
│   │   ├── RecommendationEngine.java  # Core recommendation logic
│   │   └── RecommendationServer.java  # HTTP server and API endpoints
│   └── pom.xml                        # Maven configuration
├── frontend/
│   ├── index.html                     # Main web interface
│   ├── css/
│   │   └── styles.css                 # Modern CSS styling
│   └── js/
│       └── app.js                     # Frontend JavaScript logic
└── README.md                          # This file
```

## Quick Start

### Prerequisites

- **Java Development Kit (JDK) 11 or higher**
  - Download from: https://adoptium.net/ or https://www.oracle.com/java/
- **Maven 3.6 or higher** (recommended)
  - Download from: https://maven.apache.org/download.cgi
- **Modern web browser** (Chrome, Firefox, Safari, Edge)

### Installation Instructions

#### Option 1: Install Java and Maven (Recommended)

**On macOS:**
```bash
# Using Homebrew
brew install openjdk@11 maven

# Or using SDKMAN
curl -s "https://get.sdkman.io" | bash
sdk install java 11.0.19-tem
sdk install maven
```

**On Windows:**
1. Download and install JDK 11+ from https://adoptium.net/
2. Download Maven from https://maven.apache.org/download.cgi
3. Add both to your PATH environment variable

**On Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-11-jdk maven
```

#### Option 2: Manual Setup (Java only)

If you only have Java installed without Maven, use the manual compilation script.

### Running the Application

#### Method 1: Using Maven (Recommended)

1. **Navigate to the project directory**:
   ```bash
   cd recommendation-system
   ```

2. **Run the startup script**:
   ```bash
   # On macOS/Linux
   ./start.sh
   
   # On Windows
   start.bat
   ```

   Or manually:
   ```bash
   cd backend
   mvn clean compile exec:java
   ```

#### Method 2: Manual Compilation

If Maven is not available:

```bash
cd recommendation-system
./compile-manual.sh
```

Then run:
```bash
java -cp "backend/target/classes:backend/lib/gson-2.10.1.jar" com.recommendation.RecommendationServer
```

#### Method 3: Build JAR file

```bash
cd recommendation-system/backend
mvn clean package
java -jar target/movie-recommendation-system-1.0.0.jar
```

### Accessing the Application

1. **Start the server** using any of the methods above
2. **Open your web browser**
3. **Navigate to**: `http://localhost:8080`
4. **Enjoy the movie recommendations!**

The server will automatically serve both the API and the web interface.

## API Endpoints

- `GET /api/movies` - Get all movies
- `GET /api/recommendations/popular?limit=N` - Get popular movies
- `GET /api/recommendations/content?genre=GENRE&limit=N` - Get content-based recommendations
- `GET /api/recommendations/collaborative?userId=ID&limit=N` - Get collaborative recommendations
- `POST /api/rate` - Submit movie rating

## How It Works

### Recommendation Algorithms

1. **Popular Movies**: Returns movies sorted by rating
2. **Content-Based**: Filters movies by genre and sorts by rating
3. **Collaborative Filtering**: Uses user similarity based on rating patterns to recommend movies

### Sample Data

The system includes sample data with:
- 8 movies across different genres (Drama, Action, Crime, Sci-Fi)
- 3 sample users with different preferences
- Pre-populated ratings for demonstration

### User Interface

- **Navigation**: Switch between different recommendation types
- **User Selection**: Choose from sample users to see personalized recommendations
- **Genre Filter**: Filter content-based recommendations by genre
- **Movie Details**: Click on movies to see details and rate them
- **Rating System**: 5-star rating system with real-time updates

## Customization

### Adding New Movies

Edit `RecommendationEngine.java` in the `initializeSampleData()` method:

```java
movies.add(new Movie(id, "Title", "Genre", rating, year, "Description", "ImageURL"));
```

### Adding New Users

```java
User newUser = new User(id, "Username", "PreferredGenre");
newUser.rateMovie(movieId, rating);
users.add(newUser);
```

### Modifying Algorithms

The recommendation logic is in `RecommendationEngine.java`:
- `getContentBasedRecommendations()` - Content-based filtering
- `getCollaborativeRecommendations()` - Collaborative filtering
- `calculateUserSimilarity()` - User similarity calculation

## Browser Compatibility

- Chrome 60+
- Firefox 55+
- Safari 12+
- Edge 79+

## Development

### Backend Development

The Java backend uses the built-in `com.sun.net.httpserver.HttpServer` for simplicity. For production use, consider:
- Spring Boot for more robust web framework
- Database integration (PostgreSQL, MySQL)
- Authentication and authorization
- Caching layer (Redis)

### Frontend Development

The frontend uses vanilla JavaScript for maximum compatibility. Consider adding:
- Modern framework (React, Vue.js)
- State management (Redux, Vuex)
- Build tools (Webpack, Vite)
- TypeScript for type safety

## License

This project is open source and available under the MIT License.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## Support

For questions or issues, please create an issue in the project repository.
