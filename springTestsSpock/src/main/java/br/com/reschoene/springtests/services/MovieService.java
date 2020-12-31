package br.com.reschoene.springtests.services;

import br.com.reschoene.springtests.entities.MovieEntity;
import br.com.reschoene.springtests.repositories.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public Page<MovieEntity> findAll(Pageable page) {
        return movieRepository.findAll(page);
    }

    public List<MovieEntity> findAllNonPageable(){
        return movieRepository.findAll();
    }

    public Optional<MovieEntity> findById(long id){
        return movieRepository.findById(id);
    }

    public List<MovieEntity> findByTitle(String name){
        return movieRepository.findByTitle(name);
    }

    public MovieEntity create(MovieEntity movieEntity){
        return movieRepository.save(movieEntity);
    }

    public MovieEntity update(MovieEntity movieEntity){
        return movieRepository.save(movieEntity);
    }

    public void delete(long id){
        var movieEntityOpt = movieRepository.findById(id);

        if (movieEntityOpt.isPresent())
            movieRepository.delete(movieEntityOpt.get());
    }
}
