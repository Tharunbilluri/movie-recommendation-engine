package com.recommendation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadLocalRandom;

public class RecommendationEngine {
    private List<Movie> movies;
    private List<User> users;

    public RecommendationEngine() {
        this.movies = new ArrayList<>();
        this.users = new ArrayList<>();
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Recent Hollywood Movies (2023-2024)
        movies.add(new Movie(1, "Oppenheimer", "Biography", 8.3, 2023, 
            "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb.",
            "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg", "Hollywood", 95, "English"));
        movies.add(new Movie(2, "Barbie", "Comedy", 6.9, 2023,
            "Barbie and Ken are having the time of their lives in the colorful and seemingly perfect world of Barbie Land.",
            "https://image.tmdb.org/t/p/w500/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg", "Hollywood", 92, "English"));
        movies.add(new Movie(3, "Spider-Man: Across the Spider-Verse", "Animation", 8.7, 2023,
            "Miles Morales catapults across the Multiverse, where he encounters a team of Spider-People charged with protecting its very existence.",
            "https://image.tmdb.org/t/p/w500/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg", "Hollywood", 88, "English"));
        movies.add(new Movie(4, "Dune: Part Two", "Sci-Fi", 8.5, 2024,
            "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family.",
            "https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg", "Hollywood", 90, "English"));

        // Recent Bollywood Movies (2023-2024)
        movies.add(new Movie(5, "Jawan", "Action", 7.0, 2023,
            "A high-octane action thriller which outlines the emotional journey of a man who is set to rectify the wrongs in the society.",
            "https://image.tmdb.org/t/p/w500/5ScPNT6fHtfYJeWBajZciPV3hEL.jpg", "Bollywood", 98, "Hindi"));
        movies.add(new Movie(6, "Pathaan", "Action", 6.0, 2023,
            "A soldier caught by enemies and presumed dead comes back to complete his mission, accompanied by old companions and foes.",
            "https://image.tmdb.org/t/p/w500/1n7cVZCr7GgHxAhkfYfKzpNMXjH.jpg", "Bollywood", 94, "Hindi"));
        movies.add(new Movie(7, "Animal", "Action", 6.1, 2023,
            "The hardened son of a powerful industrialist returns home after years abroad and vows to take bloody revenge on those threatening his father's life.",
            "https://image.tmdb.org/t/p/w500/2eUMlbKp2BSdSWcmOaZWXk8ajuL.jpg", "Bollywood", 89, "Hindi"));
        movies.add(new Movie(8, "12th Fail", "Drama", 8.9, 2023,
            "Based on the true story of IPS officer Manoj Kumar Sharma and IRS officer Shraddha Joshi.",
            "https://image.tmdb.org/t/p/w500/5LoRWZKi4RaWe4jQRzirUvbSVhz.jpg", "Bollywood", 85, "Hindi"));

        // Recent Tollywood Movies (2023-2024)
        movies.add(new Movie(9, "RRR", "Action", 7.9, 2022,
            "A fearless revolutionary and an officer in the British force, who once shared a deep bond, decide to join forces and chart out an inspirational path of freedom against the despotic rule.",
            "https://image.tmdb.org/t/p/w500/w6n1pu9thpCVHILejsuhKf3tNCV.jpg", "Tollywood", 96, "Telugu"));
        movies.add(new Movie(10, "Salaar: Part 1", "Action", 6.4, 2023,
            "A gang leader tries to keep a promise made to his dying friend and takes on the other criminal gangs.",
            "https://image.tmdb.org/t/p/w500/j6G24dqI4WgUtChhWjfnI4lnmiK.jpg", "Tollywood", 91, "Telugu"));
        movies.add(new Movie(11, "Gunjan Saxena", "Biography", 6.0, 2020,
            "Inspired by the life of a fearless young officer who made history by becoming the first Indian female Air Force officer to fly in a combat zone.",
            "https://image.tmdb.org/t/p/w500/y7ksqcLhQCGSNtaA912Q4ckNcmr.jpg", "Tollywood", 78, "Telugu"));

        // Recent Kollywood Movies (2023-2024)
        movies.add(new Movie(12, "Leo", "Action", 7.2, 2023,
            "Parthiban lives a simple life in Shimla along with wife Sathya and two children and runs a small cafe. However, things take a turn when he gets involved in a road accident.",
            "https://image.tmdb.org/t/p/w500/pCUdYAaarKqY2AAUtV6xXYO8UGY.jpg", "Kollywood", 93, "Tamil"));
        movies.add(new Movie(13, "Jailer", "Action", 7.1, 2023,
            "A retired jailer goes on a manhunt to find his son's killers. But the road leads him to a familiar, albeit a bit darker place.",
            "https://image.tmdb.org/t/p/w500/xvk8vjdvx8yw4k2O7lhORx3c0C0.jpg", "Kollywood", 87, "Tamil"));
        movies.add(new Movie(14, "Ponniyin Selvan: Part II", "Drama", 7.2, 2023,
            "Arulmozhi Varman continues on his journey to become Rajaraja I, the greatest ruler of the historic Chola empire of South India.",
            "https://image.tmdb.org/t/p/w500/lP7oJUmM4kTEwiEYvDktaYdDpzu.jpg", "Kollywood", 82, "Tamil"));

        // Recent Sandalwood Movies (2023-2024)
        movies.add(new Movie(15, "Kantara", "Action", 8.2, 2022,
            "It involves culture of Kambla and Bhootha Kola. A human and nature conflict where Shiva is the rebellion who works against nature.",
            "https://image.tmdb.org/t/p/w500/8UMHeR5URdTkYhzGBglkv7Rk0Uj.jpg", "Sandalwood", 84, "Kannada"));
        movies.add(new Movie(16, "777 Charlie", "Drama", 8.7, 2022,
            "Dharma is stuck in a rut with his negative and lonely lifestyle and spends each day in the comfort of his loneliness. A pup named Charlie enters his life and gives him a new perspective.",
            "https://image.tmdb.org/t/p/w500/76jYlzUdHBhAhHb7jWLn5LgLOVE.jpg", "Sandalwood", 79, "Kannada"));

        // Recent Mollywood Movies (2023-2024)
        movies.add(new Movie(17, "2018", "Drama", 8.2, 2023,
            "A disaster film set during the 2018 Kerala floods, showcasing the rescue operations and human stories of survival.",
            "https://image.tmdb.org/t/p/w500/tOKpOEOmDXlLaVkOg1XCCbHrjS7.jpg", "Mollywood", 88, "Malayalam"));
        movies.add(new Movie(18, "The Goat Life", "Drama", 7.8, 2024,
            "The real-life incident of an Indian migrant worker, Najeeb Muhammad, who goes to Saudi Arabia to earn money.",
            "https://image.tmdb.org/t/p/w500/s5znBQmprDJJ553IMQfwEVlfroH.jpg", "Mollywood", 86, "Malayalam"));

        // Add some international trending movies
        movies.add(new Movie(19, "Parasite", "Thriller", 8.5, 2019,
            "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.",
            "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg", "Korean", 83, "Korean"));
        movies.add(new Movie(20, "Your Name", "Animation", 8.2, 2016,
            "Two teenagers share a profound, magical connection upon discovering they are swapping bodies.",
            "https://image.tmdb.org/t/p/w500/q719jXXEzOoYaps6babgKnONONX.jpg", "Japanese", 76, "Japanese"));

        // Sample users with ratings
        User user1 = new User(1, "Alice", "Drama");
        user1.rateMovie(1, 5.0);
        user1.rateMovie(2, 4.5);
        user1.rateMovie(5, 4.8);
        users.add(user1);

        User user2 = new User(2, "Bob", "Action");
        user2.rateMovie(3, 5.0);
        user2.rateMovie(6, 4.7);
        user2.rateMovie(7, 4.5);
        users.add(user2);

        User user3 = new User(3, "Charlie", "Crime");
        user3.rateMovie(2, 5.0);
        user3.rateMovie(4, 4.8);
        user3.rateMovie(8, 4.6);
        users.add(user3);
    }

    // Content-based recommendation
    public List<Movie> getContentBasedRecommendations(String preferredGenre, int limit) {
        return movies.stream()
                .filter(movie -> movie.getGenre().equalsIgnoreCase(preferredGenre))
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Collaborative filtering recommendation
    public List<Movie> getCollaborativeRecommendations(int userId, int limit) {
        User targetUser = users.stream()
                .filter(user -> user.getUserId() == userId)
                .findFirst()
                .orElse(null);

        if (targetUser == null) {
            return getPopularMovies(limit);
        }

        // Find similar users based on rating similarity
        Map<User, Double> userSimilarities = new HashMap<>();
        for (User otherUser : users) {
            if (otherUser.getUserId() != userId) {
                double similarity = calculateUserSimilarity(targetUser, otherUser);
                if (similarity > 0) {
                    userSimilarities.put(otherUser, similarity);
                }
            }
        }

        // Get recommendations from similar users
        Map<Integer, Double> movieScores = new HashMap<>();
        for (Map.Entry<User, Double> entry : userSimilarities.entrySet()) {
            User similarUser = entry.getKey();
            double similarity = entry.getValue();

            for (Map.Entry<Integer, Double> ratingEntry : similarUser.getRatings().entrySet()) {
                int movieId = ratingEntry.getKey();
                double rating = ratingEntry.getValue();

                // Only recommend movies the target user hasn't rated
                if (!targetUser.getRatings().containsKey(movieId)) {
                    movieScores.put(movieId, movieScores.getOrDefault(movieId, 0.0) + similarity * rating);
                }
            }
        }

        // Sort and return top recommendations
        return movieScores.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(limit)
                .map(entry -> getMovieById(entry.getKey()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private double calculateUserSimilarity(User user1, User user2) {
        Set<Integer> commonMovies = new HashSet<>(user1.getRatings().keySet());
        commonMovies.retainAll(user2.getRatings().keySet());

        if (commonMovies.isEmpty()) {
            return 0.0;
        }

        double sum1 = 0, sum2 = 0, sum1Sq = 0, sum2Sq = 0, pSum = 0;
        for (int movieId : commonMovies) {
            double rating1 = user1.getRatings().get(movieId);
            double rating2 = user2.getRatings().get(movieId);

            sum1 += rating1;
            sum2 += rating2;
            sum1Sq += rating1 * rating1;
            sum2Sq += rating2 * rating2;
            pSum += rating1 * rating2;
        }

        double num = pSum - (sum1 * sum2 / commonMovies.size());
        double den = Math.sqrt((sum1Sq - sum1 * sum1 / commonMovies.size()) *
                              (sum2Sq - sum2 * sum2 / commonMovies.size()));

        return den == 0 ? 0 : num / den;
    }

    public List<Movie> getPopularMovies(int limit) {
        return movies.stream()
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get trending movies by industry
    public List<Movie> getTrendingMoviesByIndustry(String industry, int limit) {
        return movies.stream()
                .filter(movie -> industry == null || industry.isEmpty() || movie.getIndustry().equalsIgnoreCase(industry))
                .sorted((m1, m2) -> Integer.compare(m2.getTrendingScore(), m1.getTrendingScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get all trending movies (across all industries)
    public List<Movie> getAllTrendingMovies(int limit) {
        return movies.stream()
                .sorted((m1, m2) -> Integer.compare(m2.getTrendingScore(), m1.getTrendingScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get movies by industry
    public List<Movie> getMoviesByIndustry(String industry, int limit) {
        return movies.stream()
                .filter(movie -> movie.getIndustry().equalsIgnoreCase(industry))
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get all available industries
    public List<String> getAvailableIndustries() {
        return movies.stream()
                .map(Movie::getIndustry)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public Movie getMovieById(int id) {
        return movies.stream()
                .filter(movie -> movie.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    public User getUserById(int userId) {
        return users.stream()
                .filter(user -> user.getUserId() == userId)
                .findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void rateMovie(int userId, int movieId, double rating) {
        User user = getUserById(userId);
        if (user != null) {
            user.rateMovie(movieId, rating);
        }
    }

    // Live update methods for realistic trending simulation
    public void updateTrendingScores() {
        for (Movie movie : movies) {
            // Simulate realistic trending fluctuations
            int currentScore = movie.getTrendingScore();
            int change = ThreadLocalRandom.current().nextInt(-5, 6); // Random change between -5 and +5
            
            // Keep scores within realistic bounds (0-100)
            int newScore = Math.max(0, Math.min(100, currentScore + change));
            movie.setTrendingScore(newScore);
        }
    }

    public long getLastUpdateTime() {
        return System.currentTimeMillis();
    }

    // Get recently released movies (2023-2024)
    public List<Movie> getRecentMovies(int limit) {
        return movies.stream()
                .filter(movie -> movie.getYear() >= 2022)
                .sorted((m1, m2) -> Integer.compare(m2.getYear(), m1.getYear()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get movies by release year
    public List<Movie> getMoviesByYear(int year, int limit) {
        return movies.stream()
                .filter(movie -> movie.getYear() == year)
                .sorted((m1, m2) -> Integer.compare(m2.getTrendingScore(), m1.getTrendingScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Simulate box office performance updates
    public void simulateBoxOfficeUpdate() {
        // Boost trending scores for recent movies
        movies.stream()
                .filter(movie -> movie.getYear() >= 2023)
                .forEach(movie -> {
                    int boost = ThreadLocalRandom.current().nextInt(1, 4);
                    int newScore = Math.min(100, movie.getTrendingScore() + boost);
                    movie.setTrendingScore(newScore);
                });
    }
}
