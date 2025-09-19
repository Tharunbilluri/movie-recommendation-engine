package com.recommendation;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private double rating;
    private int year;
    private String description;
    private String imageUrl;
    private String industry; // Bollywood, Tollywood, Hollywood, etc.
    private int trendingScore; // Higher score = more trending
    private String language;

    public Movie(int id, String title, String genre, double rating, int year, String description, String imageUrl) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.description = description;
        this.imageUrl = imageUrl;
        this.industry = "Hollywood"; // Default
        this.trendingScore = 0;
        this.language = "English";
    }

    public Movie(int id, String title, String genre, double rating, int year, String description, String imageUrl, String industry, int trendingScore, String language) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.description = description;
        this.imageUrl = imageUrl;
        this.industry = industry;
        this.trendingScore = trendingScore;
        this.language = language;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public double getRating() { return rating; }
    public int getYear() { return year; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getIndustry() { return industry; }
    public int getTrendingScore() { return trendingScore; }
    public String getLanguage() { return language; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setRating(double rating) { this.rating = rating; }
    public void setYear(int year) { this.year = year; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setIndustry(String industry) { this.industry = industry; }
    public void setTrendingScore(int trendingScore) { this.trendingScore = trendingScore; }
    public void setLanguage(String language) { this.language = language; }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", rating=" + rating +
                ", year=" + year +
                '}';
    }
}
