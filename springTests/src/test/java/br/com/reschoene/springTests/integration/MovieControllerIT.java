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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

//Testes de integracao sao geralmente mais lentos que os unitarios e por isso sao executados em pipelines separados em producao

//Usa SpringBootTest para fazer uma inicializacao completa do spring, necessaria para testes de integracao
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase //para utilizar base de dados em memoria nos testes
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) //Para criar a base de dados toda antes dde qualquer teste
public class MovieControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MovieRepository movieRepository;

//    @LocalServerPort
//    private int port;

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

    @Test
    @DisplayName("findAllNonPageable returns list of all movies when successful")
    void findAllNonPageable_ReturnsListOfMoviesInsidePageObject_WhenSuccessful(){
        var movie = MovieCreator.createMovieToBeSaved();

        movieRepository.save(movie);

        var movieList = testRestTemplate.exchange("/movies/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MovieEntity>>() {
                }).getBody();

        Assertions.assertThat(movieList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(movieList.get(0).getTitle()).isEqualTo(movie.getTitle());
    }

    @Test
    @DisplayName("findById returns one movie when successful")
    void findById_ReturnsMovie_WhenSuccessful(){
        var movie = MovieCreator.createMovieToBeSaved();

        movieRepository.save(movie);

        long expectedId = movie.getId();

        var movieRes = testRestTemplate.getForObject("/movies/{id}", MovieEntity.class, expectedId);

        Assertions.assertThat(movieRes).isNotNull();

        Assertions.assertThat(movieRes.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById returns bad request When Id Was Not Found")
    void findById_ReturnsBadRequest_WhenIdNotFound(){
        var movieRes = testRestTemplate.getForEntity("/movies/{id}", MovieEntity.class, -1);

        Assertions.assertThat(movieRes).isNotNull();

        Assertions.assertThat(movieRes.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("findByTitle returns list of movies that matched a given title when successful")
    void findByTitle_ReturnsListOfMoviesMatchedTitle_WhenSuccessful(){
        var movie = MovieCreator.createMovieToBeSaved();
        String expectedTitle = movie.getTitle();

        movieRepository.save(movie);

        String url = String.format("/movies/find?title=%s", expectedTitle);

        var movieList = testRestTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MovieEntity>>() {
                }).getBody();

        Assertions.assertThat(movieList).isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(movieList.get(0).getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("findByTitle returns an empty list of movies when title was not found")
    void findByTitle_ReturnsEmptyList_WhenMovieWasNotFound(){
        var movieList = testRestTemplate.exchange("/movies/find?title=test",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MovieEntity>>() {
                }).getBody();

        Assertions.assertThat(movieList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("create returns one movie when successful")
    void create_ReturnsOptionalMovie_WhenSuccessful(){
        var movie = MovieCreator.createMovieToBeSaved();

        var responseEntity = testRestTemplate.postForEntity("/movies", movie, MovieEntity.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("update updates movie when successful")
    void update_UpdatesMovie_WhenSuccessful(){
        var movie = movieRepository.save(MovieCreator.createMovieToBeSaved());

        final String expectedTitle = "new title";
        movie.setTitle(expectedTitle);

        var responseEntity = testRestTemplate.exchange(
                "/movies",
                HttpMethod.PUT,
                new HttpEntity<>(movie),
                MovieEntity.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("delete removes movie when successful")
    void delete_RemovesMovie_WhenSuccessful(){
        var movie = movieRepository.save(MovieCreator.createMovieToBeSaved());

        final String expectedTitle = "new title";
        movie.setTitle(expectedTitle);

        var responseEntity = testRestTemplate.exchange(
                "/movies/{id}",
                HttpMethod.DELETE,
                null,
                MovieEntity.class,
                movie.getId());

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }
}
