package br.com.reschoene.springTests.repositories;

import br.com.reschoene.springTests.entities.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    List<MovieEntity> findByTitle(String name);
}
