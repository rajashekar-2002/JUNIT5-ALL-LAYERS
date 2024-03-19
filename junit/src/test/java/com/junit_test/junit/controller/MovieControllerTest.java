package com.junit_test.junit.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
//any
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//when
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit_test.junit.model.Movie;
import com.junit_test.junit.service.MovieService;

@WebMvcTest
class MovieControllerTest {

    @MockBean
    private MovieService movieService;
    //Service and repository beans are not present in mvc test
    //we can also make use of @mock as used in service
    //if creating test cases DOES NOT depend on application context or spring container use mock 
    //else mockbean [cerating a mock object already present in container or app context]

    @Autowired
    private MockMvc mockmvc;
    //we can make use of controller layer methods using this mockmvc

    @Autowired
    private ObjectMapper objectMapper;
    //convert movie object to string


    @Test
    void testCreate() throws JsonProcessingException, Exception {
        Movie avatar=Movie.builder()
        .id(1l)
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        when(movieService.save(any(Movie.class))).thenReturn(avatar);

		this.mockmvc.perform(post("/movies")
		.contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(avatar)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.name", is(avatar.getName())))
		.andExpect(jsonPath("$.genera", is(avatar.getGenera())))
		.andExpect(jsonPath("$.releaseDate", is(avatar.getReleaseDate().toString())));
    }

    @Test
    void testListAllMovies() throws Exception {
        Movie avatar=Movie.builder()
        .id(1l)
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        Movie titanic=Movie.builder()
        .id(2l)
        .genera("action")
        .name("titanic")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        List<Movie> list=new ArrayList<>();
        list.add(titanic);
        list.add(avatar);

        when(movieService.getAllMovies()).thenReturn(list);

        this.mockmvc.perform(get("/movies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()",is(list.size())));

    }

    @Test
    void testMovieById() throws Exception {
        Movie avatar=Movie.builder()
        .id(1l)
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        when(movieService.getMovieById(anyLong())).thenReturn(avatar);
        this.mockmvc.perform(get("/movies/{id}",1l))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(avatar.getName())))
        .andExpect(jsonPath("$.genera",is(avatar.getGenera())));
    }

    @Test
    void testDeleteMovie() throws Exception {
        Movie avatar=Movie.builder()
        .id(1l)
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        doNothing().when(movieService).deleteMovie(anyLong());
        this.mockmvc.perform(delete("/movies/{id}",1l))
        .andExpect(status().isNoContent());
        

    }

    @Test
    void testUpdateMovie() throws JsonProcessingException, Exception {
        Movie avatar=Movie.builder()
        .id(1l)
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        when(movieService.updateMovie(any(Movie.class),anyLong())).thenReturn(avatar);

        this.mockmvc.perform(put("/movies/{id}",1l)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(avatar)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(avatar.getName())))
        .andExpect(jsonPath("$.genera",is(avatar.getGenera())));

    }
}
