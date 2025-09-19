package com.recommendation;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RecommendationServer {
    private static final int PORT = 8080;
    private RecommendationEngine engine;
    private Gson gson;

    private ScheduledExecutorService scheduler;

    public RecommendationServer() {
        this.engine = new RecommendationEngine();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Add CORS headers to all responses
        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/movies", new MoviesHandler());
        server.createContext("/api/recommendations/popular", new PopularRecommendationsHandler());
        server.createContext("/api/recommendations/content", new ContentBasedHandler());
        server.createContext("/api/recommendations/collaborative", new CollaborativeHandler());
        server.createContext("/api/recommendations/trending", new TrendingHandler());
        server.createContext("/api/movies/industry", new IndustryMoviesHandler());
        server.createContext("/api/movies/recent", new RecentMoviesHandler());
        server.createContext("/api/movies/year", new MoviesByYearHandler());
        server.createContext("/api/industries", new IndustriesHandler());
        server.createContext("/api/live/update", new LiveUpdateHandler());
        server.createContext("/api/rate", new RatingHandler());
        
        server.setExecutor(null);
        server.start();
        
        // Start live updates - update trending scores every 30 seconds
        scheduler.scheduleAtFixedRate(() -> {
            engine.updateTrendingScores();
            System.out.println("🔄 Live update: Trending scores updated at " + new java.util.Date());
        }, 10, 30, TimeUnit.SECONDS);
        
        // Simulate box office updates every 2 minutes
        scheduler.scheduleAtFixedRate(() -> {
            engine.simulateBoxOfficeUpdate();
            System.out.println("📊 Box office update: Recent movies boosted at " + new java.util.Date());
        }, 60, 120, TimeUnit.SECONDS);
        
        System.out.println("Server started on port " + PORT);
        System.out.println("Access the application at: http://localhost:" + PORT);
        System.out.println("🔴 LIVE: Trending scores update every 30 seconds");
        System.out.println("📈 LIVE: Box office updates every 2 minutes");
    }

    private void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    private void sendJsonResponse(HttpExchange exchange, Object data, int statusCode) throws IOException {
        addCorsHeaders(exchange);
        String response = gson.toJson(data);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2) {
                    try {
                        result.put(URLDecoder.decode(pair[0], StandardCharsets.UTF_8.name()),
                                 URLDecoder.decode(pair[1], StandardCharsets.UTF_8.name()));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) {
                path = "/index.html";
            }

            File file = new File("." + path);
            if (file.exists() && file.isFile()) {
                String contentType = getContentType(path);
                exchange.getResponseHeaders().add("Content-Type", contentType);
                exchange.sendResponseHeaders(200, file.length());
                
                try (FileInputStream fis = new FileInputStream(file);
                     OutputStream os = exchange.getResponseBody()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                String response = "File not found";
                exchange.sendResponseHeaders(404, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
        }

        private String getContentType(String path) {
            if (path.endsWith(".html")) return "text/html";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".json")) return "application/json";
            return "text/plain";
        }
    }

    class MoviesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                List<Movie> movies = engine.getAllMovies();
                sendJsonResponse(exchange, movies, 200);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    class PopularRecommendationsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
                int limit = Integer.parseInt(params.getOrDefault("limit", "5"));
                
                List<Movie> recommendations = engine.getPopularMovies(limit);
                sendJsonResponse(exchange, recommendations, 200);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    class ContentBasedHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
                String genre = params.getOrDefault("genre", "Drama");
                int limit = Integer.parseInt(params.getOrDefault("limit", "5"));
                
                List<Movie> recommendations = engine.getContentBasedRecommendations(genre, limit);
                sendJsonResponse(exchange, recommendations, 200);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    class CollaborativeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
                int userId = Integer.parseInt(params.getOrDefault("userId", "1"));
                int limit = Integer.parseInt(params.getOrDefault("limit", "5"));
                
                List<Movie> recommendations = engine.getCollaborativeRecommendations(userId, limit);
                sendJsonResponse(exchange, recommendations, 200);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    class RatingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                
                Map<String, Object> requestData = gson.fromJson(sb.toString(), Map.class);
                int userId = ((Double) requestData.get("userId")).intValue();
                int movieId = ((Double) requestData.get("movieId")).intValue();
                double rating = (Double) requestData.get("rating");
                
                engine.rateMovie(userId, movieId, rating);
                
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Rating added successfully");
                sendJsonResponse(exchange, response, 200);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    class TrendingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
                String industry = params.get("industry");
                int limit = Integer.parseInt(params.getOrDefault("limit", "8"));
                
                List<Movie> recommendations;
                if (industry != null && !industry.isEmpty()) {
                    recommendations = engine.getTrendingMoviesByIndustry(industry, limit);
                } else {
                    recommendations = engine.getAllTrendingMovies(limit);
                }
                sendJsonResponse(exchange, recommendations, 200);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    class IndustryMoviesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
                String industry = params.getOrDefault("industry", "Hollywood");
                int limit = Integer.parseInt(params.getOrDefault("limit", "8"));
                
                List<Movie> movies = engine.getMoviesByIndustry(industry, limit);
                sendJsonResponse(exchange, movies, 200);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    class IndustriesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                List<String> industries = engine.getAvailableIndustries();
                sendJsonResponse(exchange, industries, 200);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    class RecentMoviesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
                int limit = Integer.parseInt(params.getOrDefault("limit", "10"));
                
                List<Movie> recentMovies = engine.getRecentMovies(limit);
                sendJsonResponse(exchange, recentMovies, 200);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    class MoviesByYearHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
                int year = Integer.parseInt(params.getOrDefault("year", "2023"));
                int limit = Integer.parseInt(params.getOrDefault("limit", "10"));
                
                List<Movie> movies = engine.getMoviesByYear(year, limit);
                sendJsonResponse(exchange, movies, 200);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    class LiveUpdateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, Object> updateInfo = new HashMap<>();
                updateInfo.put("lastUpdate", engine.getLastUpdateTime());
                updateInfo.put("status", "live");
                updateInfo.put("message", "Trending scores are updating every 30 seconds");
                
                sendJsonResponse(exchange, updateInfo, 200);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    public static void main(String[] args) {
        try {
            RecommendationServer server = new RecommendationServer();
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}
