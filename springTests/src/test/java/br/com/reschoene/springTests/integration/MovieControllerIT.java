package br.com.reschoene.springTests.integration;

import br.com.reschoene.springTests.entities.MovieEntity;
import br.com.reschoene.springTests.repositories.MovieRepository;
import br.com.reschoene.springTests.util.MovieCreator;
import br.com.reschoene.springTests.wrappers.RestResponsePage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

//Usa SpringBootTest para fazer uma inicializacao completa do spring, necessaria para testes de integracao
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase //para utilizar base de dados em memoria nos testes
public class MovieControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("findAll returns list of movies inside page object when successful")
    void findAll_ReturnsListOfMoviesInsidePageObject_WhenSuccessful(){
        var movie = MovieCreator.createMovieToBeSaved();

        movieRepository.save(movie);

        var moviePage = testRestTemplate.exchange("/movies",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestResponsePage<MovieEntity>>() {
                }).getBody();

        Assertions.assertThat(moviePage)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(moviePage.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(moviePage.toList().get(0).getTitle()).isEqualTo(movie.getTitle());
    }
}
