package com.example.scheduleservice.scheduleservice.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class ScheduleInfo {

    @Id
    private int movieId;
    @Column(name = "movieName")
    private String movieName;
    @Column(name = "releaseDate")
    private Calendar releaseDate;
    @Column(name = "endDate")
    private Calendar endDate;

    @ElementCollection(targetClass = Region.class)
    @CollectionTable(name = "regions", joinColumns = @JoinColumn(name = "movieId"))
    @Column(name = "regions", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Region> region;
    @Column(name = "isSent")
    private boolean isSent;

    public ScheduleInfo(int movieId,String movieName, Calendar releaseDate, Calendar endDate, Set<Region> region) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.endDate = endDate;
        this.region = region;
        this.isSent = false;
    }

    public ScheduleInfo() {
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
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
                Objects.equals(movieName, that.movieName) &&
                Objects.equals(releaseDate, that.releaseDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, movieName, releaseDate, endDate, region, isSent);
    }
}
