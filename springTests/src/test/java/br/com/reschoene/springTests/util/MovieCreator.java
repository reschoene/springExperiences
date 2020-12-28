package br.com.reschoene.springTests.util;

import br.com.reschoene.springTests.entities.MovieEntity;

import java.util.Date;

public class MovieCreator {
    public static MovieEntity createMovieToBeSaved(){
        var movie = new MovieEntity();
        movie.setTitle("Incetion");
        movie.setReleaseDate(new Date());
        return movie;
    }

    public static MovieEntity createValidMovie(){
        var movie = new MovieEntity();
        movie.setId(1L);
        return movie;
    }

    public static MovieEntity createValidUpdatedMovie(){
        var movie = new MovieEntity();
        movie.setId(1L);
        movie.setTitle("Incetion - Updated");
        return movie;
    }
}
