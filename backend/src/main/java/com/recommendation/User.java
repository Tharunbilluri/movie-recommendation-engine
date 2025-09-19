package com.recommendation;

import java.util.HashMap;
import java.util.Map;

public class User {
    private int userId;
    private String username;
    private Map<Integer, Double> ratings; // movieId -> rating
    private String preferredGenre;

    public User(int userId, String username, String preferredGenre) {
        this.userId = userId;
        this.username = username;
        this.preferredGenre = preferredGenre;
        this.ratings = new HashMap<>();
    }

    public void rateMovie(int movieId, double rating) {
        ratings.put(movieId, rating);
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public Map<Integer, Double> getRatings() { return ratings; }
    public String getPreferredGenre() { return preferredGenre; }

    // Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setRatings(Map<Integer, Double> ratings) { this.ratings = ratings; }
    public void setPreferredGenre(String preferredGenre) { this.preferredGenre = preferredGenre; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", preferredGenre='" + preferredGenre + '\'' +
                ", ratingsCount=" + ratings.size() +
                '}';
    }
}
