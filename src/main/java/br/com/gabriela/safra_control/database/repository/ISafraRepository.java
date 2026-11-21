package br.com.gabriela.safra_control.database.repository;

import br.com.gabriela.safra_control.database.model.SafraEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ISafraRepository extends JpaRepository<SafraEntity, Integer> {

    boolean existsByAnoAndTipoCulturaIgnoreCase(Integer ano, String cultura);

    boolean existsByAnoAndTipoCulturaIgnoreCaseAndIdNot(Integer ano, String cultura, Integer idIgnorado);
}
