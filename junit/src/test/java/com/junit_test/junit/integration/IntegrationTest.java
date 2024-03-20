package com.junit_test.junit.integration;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import com.junit_test.junit.model.Movie;
import com.junit_test.junit.repository.MovieRepository;

//set randomport
//load entire context
//provides web environment
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    
    @LocalServerPort
    private int port;

    private String baseUrl="http://localhost";

    //used to test endpoint
    private static RestTemplate restTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeAll
    public static void init(){
        //initialize resttemplate
        restTemplate=new RestTemplate();
    }

    @BeforeEach
    public void beforeSetup(){
        baseUrl=baseUrl  + ':' +port + "/movies";
    }

    @AfterEach
    public void AfterSetup(){
        movieRepository.deleteAll();
    }

    @Test
    void cerateMovieTest(){
        Movie avatar=Movie.builder()
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        Movie movie=restTemplate.postForObject(baseUrl, avatar, Movie.class);
        assertNotNull(movie);
        assertThat(movie.getId()).isNotNull();
    }

    @Test
    void fetchMoviesTest(){
        Movie avatar=Movie.builder()
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        Movie titanic=Movie.builder()
        .genera("action")
        .name("titanic")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        //save to database
        restTemplate.postForObject(baseUrl, avatar, Movie.class);
        restTemplate.postForObject(baseUrl, titanic, Movie.class);

        List<Movie> list=restTemplate.getForObject(baseUrl,List.class);
        assertNotNull(list);
        assertThat(list.size()).isEqualTo(2);
    }

    @SuppressWarnings("null")
    @Test
    void getMovieById(){
        Movie avatar=Movie.builder()
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        Movie titanic=Movie.builder()
        .genera("action")
        .name("titanic")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        //save to database
        avatar=restTemplate.postForObject( baseUrl, avatar, Movie.class);
        titanic=restTemplate.postForObject(baseUrl, titanic, Movie.class);

        Movie object=restTemplate.getForObject(baseUrl + "/" + avatar.getId(), Movie.class);
        // assertThat(object.getId()).isEqualTo(1);
        assertThat(object.getName()).isEqualTo("avatar");
    
    }

    @Test
    @SuppressWarnings("null")
    void deleteMovieTest(){
        Movie avatar=Movie.builder()
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        Movie titanic=Movie.builder()
        .genera("action")
        .name("titanic")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        //save to database
        avatar=restTemplate.postForObject(baseUrl, avatar, Movie.class);
        titanic=restTemplate.postForObject(baseUrl, titanic, Movie.class);

       restTemplate.delete(baseUrl + "/" + avatar.getId());

       int count=movieRepository.findAll().size();
       assertThat(count).isEqualTo(1);



    }
}
