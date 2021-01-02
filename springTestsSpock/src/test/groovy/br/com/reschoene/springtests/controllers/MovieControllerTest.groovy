package br.com.reschoene.springtests.controllers

import br.com.reschoene.springtests.dto.MovieDTO
import br.com.reschoene.springtests.entities.MovieEntity
import br.com.reschoene.springtests.services.MovieService
import br.com.reschoene.springtests.util.MovieCreator
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification

class MovieControllerTest extends Specification {
    def movieService = Mock(MovieService)
    def movieController = new MovieController(movieService)

    def "Find all movies returns a page containing a list of movies"() {
        given: "service's findAll method returns a page with one movie"
            def oneMovieList = [MovieCreator.createMovieToBeSaved()]
            def moviePage = new PageImpl<>(oneMovieList)

            Pageable nullPage = null
            movieService.findAll(nullPage) >> moviePage
        when: "controller receives a findAll request"
            def response = movieController.findAll(null)

        then: "returns a valid response (200) with a page containing only one movie"
            response
            response.statusCode == HttpStatus.OK
            response.hasBody()
            response.getBody().size() == 1
            response.getBody().toList()[0].title == oneMovieList[0].title
    }

    def "Find all movies returns a list of all movies"() {
        given: "service's findAllNonPageable method returns a list with one movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieService.findAllNonPageable() >> [movie]
        when: "controller receives a findAllNonPageable request"
            def response = movieController.findAllNonPageable()
        then: "returns a valid response (200) with a list containing only one movie"
            response
            response.statusCode == HttpStatus.OK
            response.hasBody()
            response.getBody().size() == 1
            response.getBody()[0].title == movie.title
    }

    def "Find by id returns one movie"() {
        given: "service's findById method returns one movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieService.findById(1) >> Optional.of(movie)
        when: "controller receives a findById request"
            def response = movieController.findById(1)
        then: "returns a valid response (200) with one movie"
            response
            response.statusCode == HttpStatus.OK
            response.hasBody()
            response.getBody().title == movie.title
    }

    def "Find by id throws ResponseStatusException when the given Id parameter does not exist"(){
        given: "service's findById method returns empty"
            movieService.findById(1) >> Optional.empty()
        when: "controller receives a findById request"
            movieController.findById(1)
        then: "returns a valid response (200) with one movie"
            def e = thrown(ResponseStatusException)
            e.status == HttpStatus.BAD_REQUEST
            e.reason == "Movie not found"
    }

    def "Find by title returns a list of movies that matched a given title"(){
        given: "service's findByTile method returns a list with one movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieService.findByTitle(movie.title) >> [movie]
        when: "controller receives a findByTitle request"
            def response = movieController.findByTitle(movie.title)
        then: "returns a valid response (200) with a list containing the searched movie"
            response
            response.statusCode == HttpStatus.OK
            response.hasBody()
            response.body.size == 1
            response.body.toList()[0].title == movie.title
    }

    def "Create inserts a new movie and return it"(){
        given: "service's create method returns a movie"
            def movieEntity = MovieCreator.createMovieToBeSaved()
            movieService.create(_ as MovieEntity) >> movieEntity
        when: "controller receives a create request"
            def movieDTO = MovieDTO.fromEntity(movieEntity)
            def response = movieController.create(movieDTO)
        then: "returns a valid response (200) with the movie that was inserted"
            response
            response.statusCode == HttpStatus.CREATED
            response.hasBody()
            response.body.title == movieDTO.title
    }

    def "Update updates a given movie and return it"(){
        given: "service's create method returns a movie"
            def movieEntity = MovieCreator.createValidUpdatedMovie()
            movieService.update(_ as MovieEntity) >> movieEntity
        when: "controller receives a update request"
            def movieDTO = MovieDTO.fromEntity(movieEntity)
            def response = movieController.update(movieDTO)
        then: "returns a valid response (200) with the movie that was updated"
            response
            response.statusCode == HttpStatus.OK
            response.hasBody()
            response.body.title == movieDTO.title
    }

    def "delete does not throw any exception"(){
        when: "controller receives a delete request for a given id"
            def response = movieController.delete(1)
        then: "returns 204 - No Content and not throw any exception"
            noExceptionThrown()
            response
            response.statusCode == HttpStatus.NO_CONTENT
    }
}
