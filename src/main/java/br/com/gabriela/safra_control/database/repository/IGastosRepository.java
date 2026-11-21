package br.com.gabriela.safra_control.database.repository;

import br.com.gabriela.safra_control.database.model.GastosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IGastosRepository extends JpaRepository<GastosEntity, Integer> {
    List<GastosEntity> findBySafra_Id(Integer safraId);
    boolean existsBySafra_Id(Integer safraId);
}
