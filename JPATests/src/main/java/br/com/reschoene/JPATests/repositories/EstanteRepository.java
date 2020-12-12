package br.com.reschoene.JPATests.repositories;

import br.com.reschoene.JPATests.enitities.Estante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstanteRepository extends JpaRepository<Estante, Integer> {
}
