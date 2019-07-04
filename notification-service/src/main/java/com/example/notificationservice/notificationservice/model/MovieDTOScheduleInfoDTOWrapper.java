package com.example.notificationservice.notificationservice.model;

import com.example.notificationservice.notificationservice.model.MovieDTO;
import com.example.notificationservice.notificationservice.model.ScheduleInfoDTO;

import java.util.Objects;

public class MovieDTOScheduleInfoDTOWrapper {

    private MovieDTO movieDTO;
    private ScheduleInfoDTO scheduleInfoDTO;

    public MovieDTOScheduleInfoDTOWrapper(MovieDTO movieDTO, ScheduleInfoDTO scheduleInfoDTO) {
        this.movieDTO = movieDTO;
        this.scheduleInfoDTO = scheduleInfoDTO;
    }

    public MovieDTOScheduleInfoDTOWrapper() {
    }

    public MovieDTO getMovieDTO() {
        return movieDTO;
    }

    public void setMovieDTO(MovieDTO movieDTO) {
        this.movieDTO = movieDTO;
    }

    public ScheduleInfoDTO getScheduleInfoDTO() {
        return scheduleInfoDTO;
    }

    public void setScheduleInfoDTO(ScheduleInfoDTO scheduleInfoDTO) {
        this.scheduleInfoDTO = scheduleInfoDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDTOScheduleInfoDTOWrapper that = (MovieDTOScheduleInfoDTOWrapper) o;
        return Objects.equals(movieDTO, that.movieDTO) &&
                Objects.equals(scheduleInfoDTO, that.scheduleInfoDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieDTO, scheduleInfoDTO);
    }
}
