package br.com.reschoene.springTests.controllers

import br.com.reschoene.springTests.services.MovieService
import br.com.reschoene.springTests.util.MovieCreator
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpStatus
import spock.lang.Specification

class MovieControllerTest extends Specification {
    def movieService = Mock(MovieService)
    def movieController = new MovieController(movieService)

    def "findAll returns list of movies inside page object when successful"() {
        given: "findAll is mocked to return a page with one movie"
            def oneMovieList = [MovieCreator.createMovieToBeSaved()]
            def moviePage = new PageImpl<>(oneMovieList)

            movieService.findAll(_) >> moviePage
        when: "controller receives a findAll request"
            def response = movieController.findAll(null)

        then: "returns a valid response (200) with a page containing only one movie"
            response
            response.statusCode == HttpStatus.OK
            response.getBody()
            response.getBody().size() == 1
            response.getBody().toList()[0].title == oneMovieList[0].title
    }

    def "findAllNonPageable returns list of all movies given successful"() {
        given: "findAllNonPageable is mocked to return a list with one movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieService.findAllNonPageable() >> [movie]
        when: "controller receives a findAllNonPageable request"
            def response = movieController.findAllNonPageable()
        then: "returns a valid response (200) with a list containing only one movie"
            response
            response.statusCode == HttpStatus.OK
            response.getBody()
            response.getBody().size() == 1
            response.getBody()[0].title == movie.title
    }

    def "findById returns one movie given successful"() {
        given: "findById is mocked to return one movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieService.findById(_) >> Optional.of(movie)
        when: "controller receives a findById request"
            def response = movieController.findById(1)
        then: "returns a valid response (200) with one movie"
            response
            response.statusCode == HttpStatus.OK
            response.getBody()
            response.getBody().title == movie.title
    }

}
