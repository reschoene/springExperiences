package br.com.reschoene.JPATests.repositories;

import br.com.reschoene.JPATests.enitities.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer> {
}
