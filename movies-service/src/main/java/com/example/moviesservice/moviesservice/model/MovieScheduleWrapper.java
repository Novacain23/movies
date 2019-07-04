package com.example.moviesservice.moviesservice.model;

import java.util.Objects;

public class MovieScheduleWrapper {

    private Movie movie;
    private ScheduleInfo scheduleInfo;

    public MovieScheduleWrapper(Movie movie, ScheduleInfo scheduleInfo) {
        this.movie = movie;
        this.scheduleInfo = scheduleInfo;
    }

    public MovieScheduleWrapper() {
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public ScheduleInfo getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(ScheduleInfo scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieScheduleWrapper that = (MovieScheduleWrapper) o;
        return Objects.equals(movie, that.movie) &&
                Objects.equals(scheduleInfo, that.scheduleInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, scheduleInfo);
    }
}
