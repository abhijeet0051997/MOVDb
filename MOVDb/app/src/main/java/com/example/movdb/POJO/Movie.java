package com.example.movdb.POJO;

public class Movie {
    String movie_title, movie_poster,movie_year;
    int movie_id;

    public Movie(String movie_title, String movie_poster, String movie_year, int movie_id) {
        this.movie_title = movie_title;
        this.movie_poster = movie_poster;
        this.movie_year = movie_year;
        this.movie_id = movie_id;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public String getMovie_poster() {
        return movie_poster;
    }

    public void setMovie_poster(String movie_poster) {
        this.movie_poster = movie_poster;
    }

    public String getMovie_year() {
        return movie_year;
    }

    public void setMovie_year(String movie_year) {
        this.movie_year = movie_year;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }
}
