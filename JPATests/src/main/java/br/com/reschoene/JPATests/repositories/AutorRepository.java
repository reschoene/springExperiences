package br.com.reschoene.JPATests.repositories;

import br.com.reschoene.JPATests.enitities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer> {
}
