package br.com.reschoene.springTests.controllers;

import br.com.reschoene.springTests.entities.MovieEntity;
import br.com.reschoene.springTests.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    ResponseEntity<Page<MovieEntity>> findAll(Pageable page) {
        return new ResponseEntity<>(movieService.findAll(page), HttpStatus.OK);
    }

    @GetMapping("/movies-all")
    ResponseEntity<List<MovieEntity>> findAllNonPageable() {
        return new ResponseEntity<>(movieService.findAllNonPageable(), HttpStatus.OK);
    }

    @GetMapping("/movies/{id}")
    ResponseEntity<MovieEntity> findById(@PathVariable long id) {
        return new ResponseEntity<>(movieService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie not found")
        ), HttpStatus.OK);
    }

    @GetMapping("/moviesByTitle/{title}")
    ResponseEntity<List<MovieEntity>> findByTitle(@PathVariable String title) {
        return new ResponseEntity<>(movieService.findByTitle(title), HttpStatus.OK);
    }

    @PostMapping("movies")
    public ResponseEntity<MovieEntity> create(@RequestBody MovieEntity movie){
        return new ResponseEntity<>(movieService.create(movie), HttpStatus.CREATED);
    }

    @DeleteMapping("movies/{id}")
    public ResponseEntity<MovieEntity> delete(@PathVariable long id){
        movieService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("movies")
    public ResponseEntity<MovieEntity> update(@RequestBody MovieEntity movie){
        return new ResponseEntity<>(movieService.update(movie), HttpStatus.OK);
    }
}
