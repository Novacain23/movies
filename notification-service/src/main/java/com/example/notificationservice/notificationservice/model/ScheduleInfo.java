package com.example.notificationservice.notificationservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class ScheduleInfo {

    private int movieId;
    private Calendar releaseDate;
    private Calendar endDate;
    private Set<Region> region;
    private boolean isSent;

    public ScheduleInfo(int movieId, Calendar releaseDate, Calendar endDate, Set<Region> region) {
        this.movieId = movieId;
        this.releaseDate = releaseDate;
        this.endDate = endDate;
        this.region = region;
        this.isSent = false;
    }

    public ScheduleInfo() {
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Set<Region> getRegion() {
        return region;
    }

    public void setRegion(Set<Region> region) {
        this.region = region;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleInfo that = (ScheduleInfo) o;
        return movieId == that.movieId &&
                isSent == that.isSent &&
                Objects.equals(releaseDate, that.releaseDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, releaseDate, endDate, region, isSent);
    }
}
