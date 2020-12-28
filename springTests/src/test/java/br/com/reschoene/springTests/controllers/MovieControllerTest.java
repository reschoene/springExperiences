package br.com.reschoene.springTests.controllers;

import br.com.reschoene.springTests.entities.MovieEntity;
import br.com.reschoene.springTests.services.MovieService;
import br.com.reschoene.springTests.util.MovieCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        var moviePage = new PageImpl<MovieEntity>(oneMovieList);
        BDDMockito.when(movieService.findAll(ArgumentMatchers.any()))
                .thenReturn(moviePage);

        BDDMockito.when(movieService.findAllNonPageable())
                .thenReturn(oneMovieList);

        BDDMockito.when(movieService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(MovieCreator.createValidMovie()));

        BDDMockito.when(movieService.findByTitle(ArgumentMatchers.anyString()))
                .thenReturn(oneMovieList);

        BDDMockito.when(movieService.create(ArgumentMatchers.any(MovieEntity.class)))
                .thenReturn(MovieCreator.createMovieToBeSaved());
    }

    @Test
    @DisplayName("findAll returns list of movies inside page object when successful")
    void findAll_ReturnsListOfMoviesInsidePageObject_WhenSuccessful(){
        String expectedTitle = MovieCreator.createMovieToBeSaved().getTitle();
        var moviePage = movieController.findAll(null).getBody();

        Assertions.assertThat(moviePage).isNotEmpty();

        Assertions.assertThat(moviePage.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(moviePage.toList().get(0).getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("findAllNonPageable returns list of all movies when successful")
    void findAllNonPageable_ReturnsListOfMoviesInsidePageObject_WhenSuccessful(){
        String expectedTitle = MovieCreator.createMovieToBeSaved().getTitle();
        var movieList = movieController.findAllNonPageable().getBody();

        Assertions.assertThat(movieList).isNotEmpty();

        Assertions.assertThat(movieList).isNotEmpty().hasSize(1);

        Assertions.assertThat(movieList.get(0).getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("findById returns one movie when successful")
    void findById_ReturnsOptionalMovie_WhenSuccessful(){
        long expectedId = MovieCreator.createValidMovie().getId();
        var movie = movieController.findById(expectedId).getBody();

        Assertions.assertThat(movie).isNotNull();

        Assertions.assertThat(movie.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByTitle returns list of movies that matched a given title when successful")
    void findByTitle_ReturnsListOfMoviesMatchedTitle_WhenSuccessful(){
        String expectedTitle = MovieCreator.createMovieToBeSaved().getTitle();
        var movieList = movieController.findByTitle(expectedTitle).getBody();

        Assertions.assertThat(movieList).isNotNull()
                  .isNotEmpty()
                  .hasSize(1);

        Assertions.assertThat(movieList.get(0).getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("findByTitle returns an empty list of movies when title was not found")
    void findByTitle_ReturnsEmptyList_WhenMovieWasNotFound(){
        BDDMockito.when(movieService.findByTitle(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        var movieList = movieController.findByTitle("").getBody();

        Assertions.assertThat(movieList).isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("create returns one movie when successful")
    void create_ReturnsOptionalMovie_WhenSuccessful(){
        var movie = MovieCreator.createMovieToBeSaved();
        var savedMovie = movieController.create(movie).getBody();

        Assertions.assertThat(savedMovie).isNotNull();
        Assertions.assertThat(savedMovie.getTitle()).isEqualTo(movie.getTitle());
    }
}
