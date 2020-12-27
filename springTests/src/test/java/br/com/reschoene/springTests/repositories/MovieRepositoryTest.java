package br.com.reschoene.springTests.repositories;

import br.com.reschoene.springTests.entities.MovieEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;

    @Test
    void save_PersistMovie_WhenSuccessful(){
        var movie = createMovie();
        var savedMovie = this.movieRepository.save(movie);

        Assertions.assertThat(savedMovie).isNotNull();
        Assertions.assertThat(savedMovie.getId()).isNotNull();
        Assertions.assertThat(savedMovie.getTitle()).isEqualTo(movie.getTitle());
    }

    private MovieEntity createMovie(){
        var movieEntity = new MovieEntity();
        movieEntity.setTitle("Inception");
        return movieEntity;
    }
}