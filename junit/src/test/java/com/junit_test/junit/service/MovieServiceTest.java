package com.junit_test.junit.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.junit_test.junit.model.Movie;
import com.junit_test.junit.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    //FAKE IMPLEMENTATION
    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;
    //get error if not used @ExtendWith(MockitoExtension.class) for mockito

    @Test
    @Order(1)
    @DisplayName("save movie object")
    void save(){
         Movie avatar=Movie.builder()
        .id(1l)
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        //USE MOKITO THAT WHENEVER THIS METHOD IS CALLED OBJECT IS RETURNED
        when(movieRepository.save(any(Movie.class))).thenReturn(avatar);

        Movie object=movieService.save(avatar);
        assertEquals(avatar, object);
        assertThat(object.getName()).isEqualTo("avatar");
    }


    @Test
    @Order(2)
    @DisplayName("list all movies")
    void getAllMovies(){
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

        when(movieRepository.findAll()).thenReturn(list);
        movieService.getAllMovies();
        assertEquals(2, list.size());
        assertThat(list).contains(titanic,avatar);

    }

    @Test
    @Order(3)
    @DisplayName("get movie by id")
    void getMovieById(){
        Movie avatar=Movie.builder()
        .id(1l)
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();


        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatar));
        Movie object=movieService.getMovieById(1l);
        assertEquals(1l, avatar.getId());
        assertThat(object.getName()).isEqualTo("avatar");


    }

    @Test
    @Order(4)
    @DisplayName("handle find by id exception")
    void getMovieIdException(){
        Movie avatar=Movie.builder()
        .id(1l)
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();
        
        when(movieRepository.findById(1l)).thenReturn(Optional.of(avatar));

        assertThrows(RuntimeException.class, ()->{
            movieService.getMovieById(2l);
        });

    }


    @Test
    @Order(5)
    @DisplayName("update movie")
    void updateMovie(){
        Movie avatar=Movie.builder()
        .id(1l)
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();
        
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatar));
        when(movieRepository.save(any(Movie.class))).thenReturn(avatar);

        avatar.setGenera("thrill");
        Movie object=movieService.updateMovie(avatar, 1l);

        assertEquals(avatar.getId(), object.getId());
        assertThat(object.getGenera()).isEqualTo("thrill");

    }

    //THIS METHID DOES NOT RETURN ANYTHING
    @Test
    @Order(6)
    @DisplayName("delete movie by id")
    void deleteMovie(){
        Movie avatar=Movie.builder()
        .id(1l)
        .genera("action")
        .name("avatar")
        .releaseDate(LocalDate.of(2000,1,22))
        .build();

        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatar));

        //do not return anything using donothing
        doNothing().when(movieRepository).delete(avatar);
        movieService.deleteMovie(1l);

        //check method called or not
        verify(movieRepository,times(1)).delete(avatar);
        verify(movieRepository,times(1)).findById(anyLong());
        // assertNull(avatar);


        // avatar object is just a local variable, and deleting it from the database doesn't affect the reference to this local variable. Hence, the avatar object won't become null after deletion in this context.
    }
    



}
