package com.example.scheduleservice.scheduleservice.model;

import java.util.Objects;

public class MovieScheduleInfoWrapper {

    private ScheduleInfo scheduleInfo;
    private Movie movie;

    public MovieScheduleInfoWrapper(ScheduleInfo scheduleInfo, Movie movie) {
        this.scheduleInfo = scheduleInfo;
        this.movie = movie;
    }

    public MovieScheduleInfoWrapper() {
    }

    public ScheduleInfo getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(ScheduleInfo scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieScheduleInfoWrapper that = (MovieScheduleInfoWrapper) o;
        return Objects.equals(scheduleInfo, that.scheduleInfo) &&
                Objects.equals(movie, that.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleInfo, movie);
    }
}
