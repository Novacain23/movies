package com.example.notificationservice.notificationservice.model;

import org.springframework.lang.NonNull;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class Movie {

    private int id;
    private String movieName;
    private Set<Genre> genres;
    private int duration;
    private int year;
    private String director;

    public Movie(int id, String movieName, Set<Genre> genres, int duration,int year, String director) {
        this.id = id;
        this.movieName = movieName;
        this.genres = genres;
        this.duration = duration;
        this.year = year;
        this.director = director;
    }

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id &&
                duration == movie.duration &&
                year == movie.year &&
                Objects.equals(movieName, movie.movieName) &&
                Objects.equals(genres, movie.genres) &&
                Objects.equals(director, movie.director);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movieName, genres, duration, year, director);
    }
}


