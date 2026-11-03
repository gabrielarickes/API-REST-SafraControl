package br.com.gabriela.safra_control.database.repository;

import br.com.gabriela.safra_control.database.model.SafraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISafraRepository extends JpaRepository<SafraEntity, Integer> {
}
