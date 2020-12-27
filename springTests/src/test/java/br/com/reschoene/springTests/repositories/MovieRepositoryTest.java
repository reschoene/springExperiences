package br.com.reschoene.springTests.repositories;

import br.com.reschoene.springTests.entities.MovieEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

@DataJpaTest
class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("Save creates movie when successful")
    void save_CreateMovie_WhenSuccessful(){
        var movie = createMovie();
        var createdMovie = this.movieRepository.save(movie);

        Assertions.assertThat(createdMovie).isNotNull();
        Assertions.assertThat(createdMovie.getId()).isNotNull();
        Assertions.assertThat(createdMovie.getTitle()).isEqualTo(movie.getTitle());
        Assertions.assertThat(createdMovie.getReleaseDate()).isEqualTo(movie.getReleaseDate());
    }

    @Test
    @DisplayName("Save updates movie when successful")
    void save_UpdatesMovie_WhenSuccessful(){
        var movie = createMovie();
        var createdMovie = this.movieRepository.save(movie);

        Date dt = new Date();

        createdMovie.setTitle("New Title");
        createdMovie.setReleaseDate(dt);
        var updatedMovie = this.movieRepository.save(createdMovie);

        Assertions.assertThat(updatedMovie).isNotNull();
        Assertions.assertThat(updatedMovie.getId()).isNotNull();
        Assertions.assertThat(updatedMovie.getTitle()).isEqualTo(createdMovie.getTitle());
        Assertions.assertThat(updatedMovie.getReleaseDate()).isEqualTo(dt);
    }

    @Test
    @DisplayName("Delete removes movie when successful")
    void delete_RemovesMovie_WhenSuccessful(){
        var movie = createMovie();
        var createdMovie = this.movieRepository.save(movie);

        this.movieRepository.delete(createdMovie);

        var movieOptional = this.movieRepository.findById(createdMovie.getId());
        Assertions.assertThat(movieOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by Title returns list of movies when successful")
    void findByTitle_ReturnsListOfMovies_WhenSuccessful(){
        var movie = createMovie();
        var createdMovie = this.movieRepository.save(movie);

        var movies = this.movieRepository.findByTitle(createdMovie.getTitle());
        Assertions.assertThat(movies).isNotEmpty().contains(createdMovie);
    }

    @Test
    @DisplayName("Find by Title returns empty list when no movie was found")
    void findByTitle_ReturnsEmptyList_WhenNoMovieWasFound(){
        var movies = this.movieRepository.findByTitle("Untitled");
        Assertions.assertThat(movies).isEmpty();
    }

    private MovieEntity createMovie(){
        var movieEntity = new MovieEntity();
        movieEntity.setTitle("Inception");
        movieEntity.setReleaseDate(new Date());
        return movieEntity;
    }
}