package com.example.moviesservice.moviesservice.repository;

import com.example.moviesservice.moviesservice.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    public Optional<Movie> findByMovieName(String name);

}
