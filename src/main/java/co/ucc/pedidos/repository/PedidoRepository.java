package co.ucc.pedidos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ucc.pedidos.model.PedidoModel;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, String> {

    Optional<PedidoModel> findByIdPedido(String idPedido);

    List<PedidoModel> findByClienteIdCliente(String idCliente);

    // List<PedidoModel> findByCategoria(String categoria);

    boolean existsByIdPedido(String idPedido);

    void deleteByIdPedido(String idPedido);
}