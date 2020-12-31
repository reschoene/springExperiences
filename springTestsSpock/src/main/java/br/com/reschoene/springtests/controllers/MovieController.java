package br.com.reschoene.springtests.controllers;

import br.com.reschoene.springtests.dto.MovieDTO;
import br.com.reschoene.springtests.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<Page<MovieDTO>> findAll(Pageable page) {
        var entityPage = movieService.findAll(page);

        var dtoPage = entityPage.map(MovieDTO::fromEntity);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    @GetMapping("/movies/all")
    public ResponseEntity<List<MovieDTO>> findAllNonPageable() {
        var entityList = movieService.findAllNonPageable();

        var dtoList = entityList
                .stream()
                .map(MovieDTO::fromEntity)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDTO> findById(@PathVariable long id) {
        var optEntity = movieService.findById(id);

        return new ResponseEntity<>(optEntity
                .map(MovieDTO::fromEntity)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie not found")
                ), HttpStatus.OK);
    }

    @GetMapping("/movies/find")
    public ResponseEntity<List<MovieDTO>> findByTitle(@RequestParam(name = "title") String title) {
        var entityList = movieService.findByTitle(title);

        var dtoList = entityList
                .stream()
                .map(MovieDTO::fromEntity)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("movies")
    public ResponseEntity<MovieDTO> create(@Valid @RequestBody MovieDTO movie) {
        var entity = movieService.create(movie.toEntity());

        return new ResponseEntity<>(MovieDTO.fromEntity(entity), HttpStatus.CREATED);
    }

    @PutMapping("movies")
    public ResponseEntity<MovieDTO> update(@Valid @RequestBody MovieDTO movie) {
        var entity = movieService.update(movie.toEntity());

        return new ResponseEntity<>(MovieDTO.fromEntity(entity), HttpStatus.OK);
    }

    @DeleteMapping("movies/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        movieService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
