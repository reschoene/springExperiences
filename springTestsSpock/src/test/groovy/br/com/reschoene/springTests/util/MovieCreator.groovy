package br.com.reschoene.springTests.util

import br.com.reschoene.springTests.entities.MovieEntity

class MovieCreator {
    static MovieEntity createMovieToBeSaved(){
        def movie = new MovieEntity()
        movie.title = "Incetion"
        movie.releaseDate = new Date()
        return movie
    }

    static MovieEntity createValidMovie(){
        def movie = new MovieEntity()
        movie.id = 1L
        return movie
    }

    static MovieEntity createValidUpdatedMovie(){
        def movie = new MovieEntity()
        movie.id = 1L
        movie.title = "Incetion - Updated"
        return movie
    }
}
