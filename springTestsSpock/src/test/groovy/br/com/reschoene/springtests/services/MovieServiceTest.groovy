package br.com.reschoene.springtests.services

import br.com.reschoene.springtests.repositories.MovieRepository
import br.com.reschoene.springtests.util.MovieCreator
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import spock.lang.Specification

class MovieServiceTest extends Specification{
    def movieRepository = Mock(MovieRepository)
    def movieService = new MovieService(movieRepository)

    def "Find all movies returns a page containing a list of movies"() {
        given: "repository's findAll method returns a page with one movie"
            def oneMovieList = [MovieCreator.createMovieToBeSaved()]
            def moviePage = new PageImpl<>(oneMovieList)

            Pageable nullPage = null
            movieRepository.findAll(nullPage) >> moviePage
        when: "service receives a findAll request"
            def response = movieService.findAll(null)

        then: "returns a page containing only one movie"
            response
            response.totalElements == 1
            response.toList()[0].title == oneMovieList[0].title
    }

    def "Find all movies returns a list of all movies"() {
        given: "repository's findAll method returns a list with one movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieRepository.findAll() >> [movie]
        when: "service receives a findAll call"
            def response = movieService.findAllNonPageable()
        then: "returns a list containing only one movie"
            response
            response.size() == 1
            response[0].title == movie.title
    }

    def "Find by id returns one movie"() {
        given: "repository's findById method returns one movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieRepository.findById(1) >> Optional.of(movie)
        when: "service receives a findById call"
            def response = movieService.findById(1)
        then: "returns a list containing one movie"
            response
            response.isPresent()
            response.get().title == movie.title
    }

    def "Find by title returns a list of movies that matched a given title"(){
        given: "repository's findByTile method returns a list with one movie"
           def movie = MovieCreator.createMovieToBeSaved()
            movieRepository.findByTitle(movie.title) >> [movie]
        when: "service receives a findByTitle call"
            def response = movieService.findByTitle(movie.title)
        then: "returns a list containing the searched movie"
            response
            response.size == 1
            response[0].title == movie.title
    }

    def "Create inserts a new movie and return it"(){
        given: "repository's create method returns a movie"
            def movieEntity = MovieCreator.createMovieToBeSaved()
            movieRepository.save(movieEntity) >> movieEntity
        when: "service receives a create call"
            def response = movieService.create(movieEntity)
        then: "returns the movie that was inserted"
            response
            response.title == movieEntity.title

    }

    def "Update updates a given movie and return it"(){
        given: "repository's create method returns a movie"
            def movieEntity = MovieCreator.createValidUpdatedMovie()
            movieRepository.save(movieEntity) >> movieEntity
        when: "service receives a update call"
            def response = movieService.update(movieEntity)
        then: "returns the movie that was updated"
            response
            response.title == movieEntity.title
    }

    def "Delete does not throw any exception"(){
        given: "repository's findById method returns a movieEntity"
            def movieEntity = MovieCreator.createValidMovie()
            movieRepository.findById(1) >> Optional.of(movieEntity)
        when: "service receives a delete call for a given id"
            movieService.delete(1)
        then: "no exception was thrown and repository's delete method was called a once"
            noExceptionThrown()
            1 * movieRepository.delete(_)
    }
}
