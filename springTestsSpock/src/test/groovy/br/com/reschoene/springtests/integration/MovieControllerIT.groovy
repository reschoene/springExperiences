package br.com.reschoene.springtests.integration

import br.com.reschoene.springtests.entities.MovieEntity
import br.com.reschoene.springtests.repositories.MovieRepository
import br.com.reschoene.springtests.util.MovieCreator
import br.com.reschoene.springtests.wrappers.RestResponsePage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Profile
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import spock.lang.Specification

//Integration tests are slower than unit tests. In production environment they are executed in different pipelines

//@SpringBootTest do a complete initialization on Spring Context, this is necessary for the integration tests
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase //Auto configure in memory database for the tests
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) //Before each tests, recreates the entire database
class MovieControllerIT extends Specification{
    @Autowired
    TestRestTemplate testRestTemplate

    @Autowired
    MovieRepository movieRepository

//    @LocalServerPort
//    private int port

    def "GET movies returns a page containing a list of movies" (){
        given: "database already contains a movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieRepository.save(movie)

        when: "is performed a GET request /movies endpoint"
            def response = testRestTemplate.exchange("/movies",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<RestResponsePage<MovieEntity>>() {
                    })
        then: "is returned status code 200 (OK) with a page containing one movie"
            response.statusCode == HttpStatus.OK
            response.hasBody()
            with(response.getBody().toList()){
                it.size() == 1
                it[0].title == movie.title
            }
    }

    def "GET all movies returns list of all movies" (){
        given: "database already contains a movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieRepository.save(movie)

        when: "is performed a GET request /movies/all endpoint"
            def response = testRestTemplate.exchange("/movies/all",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MovieEntity>>() {
                    })

        then: "is returned status code 200 (OK) with a list containing one movie"
            response.statusCode == HttpStatus.OK
            response.hasBody()
            with(response.getBody()){
                it.size() == 1
                it[0].title == movie.title
            }
    }

    def "GET movies by id returns one movie when successful" (){
        given: "database already contains a movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieRepository.save(movie)

        when: "is performed a GET request to /movies/{id} endpoint"
            def movieRes = testRestTemplate.getForObject("/movies/{id}", MovieEntity.class, movie.id)

        then: "return a movie with the same id used in the search"
            movieRes
            movieRes.id
            movieRes.id == movie.id
    }

    def "GET movies by id  returns bad request When Id Was Not Found" (){
        when: "is performed a GET request to /movies/{id} endpoint using a non existent id"
            def movieRes = testRestTemplate.getForEntity("/movies/{id}", MovieEntity.class, -1)
        then: "returns a bad request status (400)"
            movieRes.statusCode == HttpStatus.BAD_REQUEST
    }

    def "GET movies by title returns list of movies that matched a given title" (){
        given: "database already contains a movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieRepository.save(movie)

        when: "is performed a GET request to /movies/find?title=%s endpoint using a existent title"
            String url = String.format("/movies/find?title=%s", movie.getTitle())
            def response = testRestTemplate.exchange(url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MovieEntity>>() {
                    })
        then: "is returned status code 200 (OK) with a list containing one movie"
            response.statusCode == HttpStatus.OK
            response.hasBody()
            with(response.getBody()){
                it.size() == 1
                it[0].title == movie.title
            }
    }

    def "GET movies by title returns an empty list of movies when title was not found" (){
        when: "is performed a GET request to /movies/find?title=%s endpoint using a non existent title"
            def response = testRestTemplate.exchange("/movies/find?title=test",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MovieEntity>>() {
                    })
        then: "is returned status code 200 (OK) with a empty list"
            response.statusCode == HttpStatus.OK
            response.hasBody()
            response.getBody().isEmpty()
    }

    def "POST movies creates one movie on the database and return it" () {
        given: "a in-memory movie to be saved"
            def movieToBeSaved = MovieCreator.createMovieToBeSaved()
        when: "is performed a POST request to /movies endpoint for a movie"
            def response = testRestTemplate.postForEntity("/movies", movieToBeSaved, MovieEntity.class)
        then: "is returned status code 201 (CREATED) with the created movie and it is present on the database"
            response.statusCode == HttpStatus.CREATED
            response.hasBody()
        with(response.getBody()) {
            it.id
            it.title == movieToBeSaved.title
            it.releaseDate == movieToBeSaved.releaseDate
            movieRepository.findById(it.id).isPresent()
        }
    }

    def "UPDATE movies updates one movie on the database and return it" (){
        given: "database already contains a movie"
            def movie = MovieCreator.createMovieToBeSaved()
            def existingMovie = movieRepository.save(movie)
            existingMovie.title = "new title"
            existingMovie.releaseDate = new Date()
        when: "is performed a PUT request to /movies endpoint for a modified movie to be saved"
            def response = testRestTemplate.exchange(
                    "/movies",
                    HttpMethod.PUT,
                    new HttpEntity<>(existingMovie),
                    MovieEntity.class)
        then: "is returned status code 200 (OK) with the updated movie and it was successful saved on the database"
            response.statusCode == HttpStatus.OK
            response.hasBody()
            with(response.getBody()){
                it.id == existingMovie.id
                it.title == existingMovie.title
                it.releaseDate == existingMovie.releaseDate
            }

            with(movieRepository.findById(existingMovie.id)){
                it.isPresent()
                with(it.get()) {
                    it.id == existingMovie.id
                    it.title == existingMovie.title
                    it.releaseDate == existingMovie.releaseDate
                }
            }
    }

    def "DELETE movies removes a movie from the database" (){
        given: "database already contains a movie"
            def movie = MovieCreator.createMovieToBeSaved()
            def existingMovie = movieRepository.save(movie)

        when: "is performed a DELETE request to /movies endpoint for a modified movie to be deleted"
            def response = testRestTemplate.exchange(
                    "/movies/{id}",
                    HttpMethod.DELETE,
                    null,
                    MovieEntity.class,
                    existingMovie.getId())
        then: "is returned status code NO_CONTENT (204) and the movie is not more present on the database"
            response.statusCode == HttpStatus.NO_CONTENT
            movieRepository.findById(existingMovie.id).isEmpty()
    }
}
