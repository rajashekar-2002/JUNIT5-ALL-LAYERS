package com.junit_test.junit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.junit_test.junit.model.Movie;



public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	List<Movie> findByGenera(String genera);
}