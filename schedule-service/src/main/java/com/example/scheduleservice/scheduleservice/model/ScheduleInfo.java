package com.example.scheduleservice.scheduleservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
public class ScheduleInfo {

    @Id
    private int movieId;
    @Column(name = "releaseDate")
    private Calendar releaseDate;
    @Column(name = "endDate")
    private Calendar endDate;
    @Column(name = "region")
    private String region;
    @Column(name = "isSent")
    private boolean isSent;

    public ScheduleInfo(int movieId, Calendar releaseDate, Calendar endDate, String region) {
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
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
