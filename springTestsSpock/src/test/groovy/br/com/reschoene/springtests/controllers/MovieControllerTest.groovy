package br.com.reschoene.springtests.controllers

import br.com.reschoene.springtests.dto.MovieDTO
import br.com.reschoene.springtests.entities.MovieEntity
import br.com.reschoene.springtests.services.MovieService
import br.com.reschoene.springtests.util.MovieCreator
import br.com.reschoene.springtests.wrappers.RestResponsePage
import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static br.com.reschoene.springtests.util.MockMvcDeserializer.getObjectContent
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MovieController.class)
class MovieControllerTest extends Specification {
    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    MovieService movieService = Mock()

    def "Find all movies returns a page containing a list of movies"() {
        given: "service's findAll method returns a page with one movie"
            def oneMovieList = [MovieCreator.createMovieToBeSaved()]
            def moviePage = new PageImpl<>(oneMovieList)
            movieService.findAll(_ as Pageable) >> moviePage
        when: "controller receives GET request for /movies endpoint"
            def response = mockMvc.perform(get("/movies"))
            def page = getObjectContent(response, RestResponsePage.class, objectMapper)
        then: "returns a page containing a list with one movie"
            response.andExpect(status().isOk())
            page
            page.totalElements == 1
            page.toList()[0].title == oneMovieList[0].title
    }

    def "Find all movies returns a list of all movies"() {
        given: "service's findAllNonPageable method returns a list with one movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieService.findAllNonPageable() >> [movie]
        when: "controller receives GET request for /movies/all endpoint"
            def response = mockMvc.perform(get("/movies/all"))
            def list = getObjectContent(response, List.class, objectMapper)
        then: "returns a valid response (200) with a list containing only one movie"
            response.andExpect(status().isOk())
            list
            list.size == 1
            list[0].title == movie.title
    }

    def "Find by id returns one movie"() {
        given: "service's findById method returns one movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieService.findById(1) >> Optional.of(movie)
        when: "controller receives GET request for /movies/{id} endpoint using an existing id"
            def response = mockMvc.perform(get("/movies/{id}", 1))
            def returnedMovie = getObjectContent(response, MovieDTO.class, objectMapper)
        then: "returns a valid response (200) with one movie"
            response.andExpect(status().isOk())
            returnedMovie.title == movie.title
    }

    def "Find by id returns status 400 when the given Id parameter does not exist"(){
        given: "service's findById method returns empty"
            movieService.findById(-1) >> Optional.empty()
        when: "controller receives GET request for /movies/{id} endpoint using a not existing id"
            def response = mockMvc.perform(get("/movies/{id}", -1))
        then: "returns status 400 - BAD REQUEST"
            response.andExpect(status().isBadRequest())
    }

    def "Find by title returns a list of movies that matched a given title"(){
        given: "service's findByTile method returns a list with one movie"
            def movie = MovieCreator.createMovieToBeSaved()
            movieService.findByTitle(movie.title) >> [movie]
        when: "controller receives GET request for /movies/all endpoint"
            def ulr = String.format("/movies/find?title=%s", movie.title)
            def response = mockMvc.perform(get(ulr))
            def list = getObjectContent(response, List.class, objectMapper)
        then: "returns a valid response (200) with a list containing only one movie"
            response.andExpect(status().isOk())
            list
            list.size == 1
            list[0].title == movie.title
    }

    def "Create inserts a new movie and return it"(){
        given: "service's create method returns a movie"
            def movieEntity = MovieCreator.createMovieToBeSaved()
            movieService.create(_ as MovieEntity) >> movieEntity
            def movieDto = MovieDTO.fromEntity(movieEntity)
        when: "controller receives a POST request to /movies endpoint"
            def response = mockMvc.perform(post("/movies")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .content(objectMapper.writeValueAsString(movieDto)))
            def insertedMovie = getObjectContent(response, MovieDTO.class, objectMapper)
        then: "returns a created response (201) with the movie that was inserted"
            response.andExpect(status().isCreated())
            insertedMovie.title == movieDto.title
    }

    def "Create can't insert a movie with no title"(){
        given: "service's create method returns a movie"
            def movieEntity = MovieCreator.createMovieToBeSaved()
            movieService.create(_ as MovieEntity) >> movieEntity
            def movieDto = MovieDTO.fromEntity(movieEntity)
            movieDto.setTitle("")
        when: "controller receives a POST request to /movies endpoint with a movie without a title"
            def response = mockMvc.perform(post("/movies")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .content(objectMapper.writeValueAsString(movieDto)))
        then: "returns a bad request response (400)"
            response.andExpect(status().isBadRequest())
    }

    def "Update updates a given movie and return it"(){
        given: "service's create method returns a movie"
            def movieEntity = MovieCreator.createValidUpdatedMovie()
            def movieDto = MovieDTO.fromEntity(movieEntity)
            movieService.update(_ as MovieEntity) >> movieEntity

        when: "controller receives a PUT request to /movies endpoint"
            def response = mockMvc.perform(put("/movies")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .content(objectMapper.writeValueAsString(movieDto)))
            def updatedMovie = getObjectContent(response, MovieDTO.class, objectMapper)
        then: "returns a ok response (200) with the movie that was updated"
            response.andExpect(status().isOk())
            updatedMovie.title == movieDto.title
    }

    def "delete does not throw any exception and return status 204"(){
        when: "controller receives a DELETE request to /movies/{id} endpoint"
            def response = mockMvc.perform(delete("/movies/{id}", 1))
        then: "returns 204 - No Content and not throw any exception"
            noExceptionThrown()
            response.andExpect(status().isNoContent())
    }
}
