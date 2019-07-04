package com.example.moviesservice.moviesservice.model;

import java.util.Objects;
import java.util.Set;

public class MovieDTO {

    private String movieName;
    private Set<Genre> genres;
    private int duration;
    private String director;

    public MovieDTO(String movieName, Set<Genre> genres, int duration, String director) {
        this.movieName = movieName;
        this.genres = genres;
        this.duration = duration;
        this.director = director;
    }

    public MovieDTO() {
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
        MovieDTO movieDTO = (MovieDTO) o;
        return duration == movieDTO.duration &&
                Objects.equals(movieName, movieDTO.movieName) &&
                Objects.equals(genres, movieDTO.genres) &&
                Objects.equals(director, movieDTO.director);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieName, genres, duration, director);
    }
}
