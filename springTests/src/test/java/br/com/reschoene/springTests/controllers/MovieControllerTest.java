package br.com.reschoene.springTests.controllers;

import br.com.reschoene.springTests.entities.MovieEntity;
import br.com.reschoene.springTests.services.MovieService;
import br.com.reschoene.springTests.util.MovieCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class MovieControllerTest {
    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieService;

    @BeforeEach
    void setUp(){
        var oneMovieList = List.of(MovieCreator.createMovieToBeSaved());
        var moviePage = new PageImpl<>(oneMovieList);
        BDDMockito.given(movieService.findAll(ArgumentMatchers.any()))
                .willReturn(moviePage);

        BDDMockito.given(movieService.findAllNonPageable())
                .willReturn(oneMovieList);

        BDDMockito.given(movieService.findById(ArgumentMatchers.anyLong()))
                .willReturn(Optional.of(MovieCreator.createValidMovie()));

        BDDMockito.given(movieService.findByTitle(ArgumentMatchers.anyString()))
                .willReturn(oneMovieList);

        BDDMockito.given(movieService.create(ArgumentMatchers.any(MovieEntity.class)))
                .willReturn(MovieCreator.createMovieToBeSaved());

        BDDMockito.given(movieService.update(ArgumentMatchers.any(MovieEntity.class)))
                .willReturn(MovieCreator.createValidUpdatedMovie());

        BDDMockito.doNothing().when(movieService).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("findAll returns list of movies inside page object given successful")
    void findAll_ReturnsListOfMoviesInsidePageObject_givenSuccessful(){
        String expectedTitle = MovieCreator.createMovieToBeSaved().getTitle();
        var moviePage = movieController.findAll(null).getBody();

        Assertions.assertThat(moviePage)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(moviePage.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(moviePage.toList().get(0).getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("findAllNonPageable returns list of all movies given successful")
    void findAllNonPageable_ReturnsListOfMoviesInsidePageObject_givenSuccessful(){
        String expectedTitle = MovieCreator.createMovieToBeSaved().getTitle();
        var movieList = movieController.findAllNonPageable().getBody();

        Assertions.assertThat(movieList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(movieList.get(0).getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("findById returns one movie given successful")
    void findById_ReturnsMovie_givenSuccessful(){
        long expectedId = MovieCreator.createValidMovie().getId();
        var movie = movieController.findById(expectedId).getBody();

        Assertions.assertThat(movie).isNotNull();

        Assertions.assertThat(movie.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById Throws ResponseStatusException given Id Was Not Found")
    void findById_ThrowsResponseStatusException_givenIdNotFound(){
        BDDMockito.given(movieService.findById(ArgumentMatchers.anyLong()))
                .willReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> this.movieController.findById(-1))
                .withMessageContaining("Movie not found");
    }

    @Test
    @DisplayName("findByTitle returns list of movies that matched a given title given successful")
    void findByTitle_ReturnsListOfMoviesMatchedTitle_givenSuccessful(){
        String expectedTitle = MovieCreator.createMovieToBeSaved().getTitle();
        var movieList = movieController.findByTitle(expectedTitle).getBody();

        Assertions.assertThat(movieList).isNotNull()
                  .isNotEmpty()
                  .hasSize(1);

        Assertions.assertThat(movieList.get(0).getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("findByTitle returns an empty list of movies given title was not found")
    void findByTitle_ReturnsEmptyList_givenMovieWasNotFound(){
        BDDMockito.given(movieService.findByTitle(ArgumentMatchers.anyString()))
                .willReturn(Collections.emptyList());

        var movieList = movieController.findByTitle("").getBody();

        Assertions.assertThat(movieList).isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("create returns one movie given successful")
    void create_ReturnsOptionalMovie_givenSuccessful(){
        var movie = MovieCreator.createMovieToBeSaved();
        var savedMovie = movieController.create(movie).getBody();

        Assertions.assertThat(savedMovie).isNotNull();
        Assertions.assertThat(savedMovie.getTitle())
                .isNotEmpty()
                .isEqualTo(movie.getTitle());
    }

    @Test
    @DisplayName("update updates movie given successful")
    void update_UpdatesMovie_givenSuccessful(){
        var movie = MovieCreator.createValidUpdatedMovie();
        var updatedMovie = movieController.update(movie).getBody();

        Assertions.assertThat(updatedMovie).isNotNull();
        Assertions.assertThat(updatedMovie.getId()).isGreaterThan(0);
        Assertions.assertThat(updatedMovie.getTitle()).isEqualTo(movie.getTitle());
    }

    @Test
    @DisplayName("delete removes movie given successful")
    void delete_RemovesMovie_givenSuccessful(){
        Assertions.assertThatCode(() -> movieController.delete(1)).doesNotThrowAnyException();

        ResponseEntity<Void> response = movieController.delete(1);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }
}
