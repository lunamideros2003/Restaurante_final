package co.ucc.pedidos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ucc.pedidos.model.OpcionModel;

@Repository
public interface OpcionRepository extends JpaRepository<OpcionModel, Long> {

    List<OpcionModel> findAllByOrderByOrdenAscIdAsc();

    List<OpcionModel> findByPadreOpcionIdIsNullOrderByOrdenAscIdAsc();
}
