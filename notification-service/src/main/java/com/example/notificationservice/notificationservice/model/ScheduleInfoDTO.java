package com.example.notificationservice.notificationservice.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ScheduleInfoDTO {



    private Calendar releaseDate;
    private Calendar endDate;
    private String region;

    public ScheduleInfoDTO(Calendar releaseDate, Calendar endDate, String region) {
        this.releaseDate = releaseDate;
        this.endDate = endDate;
        this.region = region;
    }

    public ScheduleInfoDTO() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleInfoDTO that = (ScheduleInfoDTO) o;
        return Objects.equals(releaseDate, that.releaseDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(releaseDate, endDate, region);
    }
}
