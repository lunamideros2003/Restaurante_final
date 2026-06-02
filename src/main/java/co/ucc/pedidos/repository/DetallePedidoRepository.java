package co.ucc.pedidos.repository;

import co.ucc.pedidos.model.DetallePedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedidoModel, Long> {
    List<DetallePedidoModel> findByPedidoIdPedido(String idPedido);
}
