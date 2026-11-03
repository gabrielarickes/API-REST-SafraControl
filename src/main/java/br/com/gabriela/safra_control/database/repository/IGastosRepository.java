package br.com.gabriela.safra_control.database.repository;

import br.com.gabriela.safra_control.database.model.GastosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGastosRepository extends JpaRepository<GastosEntity, Integer> {
}
