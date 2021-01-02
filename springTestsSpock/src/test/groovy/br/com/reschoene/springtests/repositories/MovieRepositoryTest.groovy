package br.com.reschoene.springtests.repositories

import br.com.reschoene.springtests.entities.MovieEntity
import br.com.reschoene.springtests.util.MovieCreator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Specification

@DataJpaTest
class MovieRepositoryTest extends Specification{
    @Autowired
    private MovieRepository movieRepository

    def "Save creates movie when successful"(){
        given: "a movie to be saved"
            def movie = MovieCreator.createMovieToBeSaved()
        when: "repository's save method is called for this movie"
            def createdMovie = movieRepository.save(movie)
        then: "returns a valid created movie"
            createdMovie
            createdMovie.id
            createdMovie.title == movie.title
            createdMovie.releaseDate == movie.releaseDate
    }

    def "Save updates movie when successful"(){
        given: "database already contains a movie"
            def movieToBeSaved = MovieCreator.createMovieToBeSaved()
            def movieToBeUpdated = movieRepository.save(movieToBeSaved)
            movieToBeUpdated.title = "New Title"
            movieToBeUpdated.releaseDate = new Date()
        when: "repository's save method is called for this movie to be updated"
            def updatedMovie = movieRepository.save(movieToBeUpdated)
        then: "returns a valid updated movie equals to the movie to be updated"
            updatedMovie
            updatedMovie.id
            updatedMovie.title == movieToBeUpdated.title
            updatedMovie.releaseDate == movieToBeUpdated.releaseDate
    }

    def "Delete removes movie when successful"(){
        given: "database already contains a movie"
            def movieToBeSaved = MovieCreator.createMovieToBeSaved()
            def movieToBeRemoved = movieRepository.save(movieToBeSaved)
        when: "repository's delete method is called for the movie to be removed and then it is searched again"
            def deleteResponse = movieRepository.delete(movieToBeRemoved)
            def searchedMovie = movieRepository.findById(movieToBeRemoved.id)
        then: "delete method returns void and the movie is not found after its removal"
            deleteResponse == null
            searchedMovie == Optional.empty()
    }

    def "Find by Title returns list of movies when successful"(){
        given: "database already contains a movie"
            def movieToBeSaved = MovieCreator.createMovieToBeSaved()
            def createdMovie = movieRepository.save(movieToBeSaved)
        when: "is performed a search for movies whose its title matches the title of the created one"
            def foundedMovies = movieRepository.findByTitle(createdMovie.title)
        then:
            foundedMovies
            foundedMovies.size() == 1
            foundedMovies.contains(createdMovie)
    }

    def "Find by Title returns empty list when no movie was found"(){
        when: "is performed a search for movies using a non existent title as parameter"
            def foundedMovies = movieRepository.findByTitle("Non existence title")
        then: "returns an empty movie list"
            foundedMovies != null
            foundedMovies.isEmpty()
    }

    def "Save throw ConstraintViolationException when title is null"(){
        given: "a movie with null title"
            def movie = new MovieEntity()
        when: "repository's save method is called for this movie"
            movieRepository.save(movie)
        then: "An ConstraintViolationException exception is thrown"
            thrown(DataIntegrityViolationException)
    }
}
