package br.com.reschoene.springTests.services;

import br.com.reschoene.springTests.entities.MovieEntity;
import br.com.reschoene.springTests.repositories.MovieRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class MovieServiceTest {
    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp(){
        var oneMovieList = List.of(MovieCreator.createMovieToBeSaved());
        var moviePage = new PageImpl<>(oneMovieList);
        BDDMockito.when(movieRepository.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(moviePage);

        BDDMockito.when(movieRepository.findAll())
                .thenReturn(oneMovieList);

        BDDMockito.when(movieRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(MovieCreator.createValidMovie()));

        BDDMockito.when(movieRepository.findByTitle(ArgumentMatchers.anyString()))
                .thenReturn(oneMovieList);

        BDDMockito.when(movieRepository.save(ArgumentMatchers.any(MovieEntity.class)))
                .thenReturn(MovieCreator.createMovieToBeSaved());

        BDDMockito.doNothing().when(movieRepository).delete(ArgumentMatchers.any(MovieEntity.class));
    }

    @Test
    @DisplayName("findAll returns list of movies inside page object when successful")
    void findAll_ReturnsListOfMoviesInsidePageObject_WhenSuccessful(){
        String expectedTitle = MovieCreator.createMovieToBeSaved().getTitle();
        var moviePage = movieService.findAll(PageRequest.of(1,1));

        Assertions.assertThat(moviePage)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(moviePage.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(moviePage.toList().get(0).getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("findAllNonPageable returns list of all movies when successful")
    void findAllNonPageable_ReturnsListOfMoviesInsidePageObject_WhenSuccessful(){
        String expectedTitle = MovieCreator.createMovieToBeSaved().getTitle();
        var movieList = movieService.findAllNonPageable();

        Assertions.assertThat(movieList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(movieList.get(0).getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("findById returns one movie when successful")
    void findById_ReturnsMovie_WhenSuccessful(){
        long expectedId = MovieCreator.createValidMovie().getId();
        var movie = movieService.findById(expectedId);

        Assertions.assertThat(movie).isNotNull();

        Assertions.assertThat(movie.get().getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByTitle returns list of movies that matched a given title when successful")
    void findByTitle_ReturnsListOfMoviesMatchedTitle_WhenSuccessful(){
        String expectedTitle = MovieCreator.createMovieToBeSaved().getTitle();
        var movieList = movieService.findByTitle(expectedTitle);

        Assertions.assertThat(movieList).isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(movieList.get(0).getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("findByTitle returns an empty list of movies when title was not found")
    void findByTitle_ReturnsEmptyList_WhenMovieWasNotFound(){
        BDDMockito.when(movieRepository.findByTitle(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        var movieList = movieService.findByTitle("");

        Assertions.assertThat(movieList).isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("create returns one movie when successful")
    void create_ReturnsOptionalMovie_WhenSuccessful(){
        var movie = MovieCreator.createMovieToBeSaved();
        var savedMovie = movieService.create(movie);

        Assertions.assertThat(savedMovie).isNotNull();
        Assertions.assertThat(savedMovie.getTitle())
                .isNotEmpty()
                .isEqualTo(movie.getTitle());
    }

    @Test
    @DisplayName("update updates movie when successful")
    void update_UpdatesMovie_WhenSuccessful(){
        BDDMockito.when(movieRepository.save(ArgumentMatchers.any(MovieEntity.class)))
                .thenReturn(MovieCreator.createValidUpdatedMovie());

        var movie = MovieCreator.createValidUpdatedMovie();
        var updatedMovie = movieService.update(movie);

        Assertions.assertThat(updatedMovie).isNotNull();
        Assertions.assertThat(updatedMovie.getId()).isGreaterThan(0);
        Assertions.assertThat(updatedMovie.getTitle()).isEqualTo(movie.getTitle());
    }

    @Test
    @DisplayName("delete removes movie when successful")
    void delete_RemovesMovie_WhenSuccessful(){
        Assertions.assertThatCode(() -> movieService.delete(1)).doesNotThrowAnyException();
    }
}