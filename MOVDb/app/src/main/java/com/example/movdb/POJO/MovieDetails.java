package com.example.movdb.POJO;

public class MovieDetails {
    String movie_title,year,rating,overview;

    public MovieDetails() {
    }

    public MovieDetails(String movie_title, String year, String rating, String overview) {
        this.movie_title = movie_title;
        this.year = year;
        this.rating = rating;
        this.overview = overview;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
