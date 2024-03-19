package com.junit_test.junit.repository;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.junit_test.junit.model.Movie;

//SEE SQL LOGS AND DETAILS IN DEBUG CONSOLE
//AND TICKS IN TESTS RESULTS
// @Transactional
// @SpringBootTest
@DataJpaTest
@DisplayName("save object to database")
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private EntityManager entityManager;

    private Movie avatar;
    private Movie titanic;

    @BeforeEach
    void setUp(){

        avatar=Movie.builder()
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        titanic=Movie.builder()
        .genera("drama")
        .name("titanic")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        // avatar = movieRepository.save(avatar);
        // titanic = movieRepository.save(titanic);

        entityManager.persist(avatar);
        entityManager.persist(titanic);
        // Persist movies using EntityManager
    }


    @Test
    void CheckObjectSavedInDatabase(){
        assertNotNull(avatar);
        assertThat(avatar.getId()).isNotNull();

        assertNotNull(titanic);
        assertThat(titanic.getId()).isNotNull();

    }

    @Test
    @DisplayName("movie list with expected 2 in list")
    void getAllMovies(){

            List<Movie>list=movieRepository.findAll();
            assertNotNull(list);
            assertThat(list).isNotNull();
            assertEquals(2, list.size());

    }

    @Test
    @DisplayName("find movie by id")
    void findMovieById(){

        Movie object=movieRepository.findById(1l).get();
        //get() thorws exception if object not found
        assertNotNull(object);
        assertEquals(1l,object.getId());
        assertEquals("action",object.getGenera());
        assertThat(object.getReleaseDate()).isBefore(LocalDate.of(2000,2,10));
    }


    @Test
    @DisplayName("update movie")
    void updateMovie(){

        Movie object=movieRepository.findById(1l).get();

        object.setGenera("romance");
        object.setName("Titanic");

        assertEquals("romance",object.getGenera());
        assertEquals("Titanic",object.getName());

    }

    @Test
    @DisplayName("delete movie by id")
    void deleteMovieById(){

        movieRepository.delete(avatar);
        List<Movie> list=movieRepository.findAll();
        assertEquals(1l, list.size());

        Optional<Movie> checkObject=movieRepository.findById(1l);
        assertThat(checkObject).isEmpty();
        
    }

    @Test
    @DisplayName("list all movies by genera")
    void getMovieByGenera(){

        List<Movie> list=movieRepository.findByGenera("action");
        assertEquals(1, list.size());
        assertThat(list.size()).isEqualTo(1);

    }
}
