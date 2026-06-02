package co.ucc.pedidos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ucc.pedidos.model.PagoModel;

@Repository
public interface PagoRepository extends JpaRepository<PagoModel, String> {

    Optional<PagoModel> findByIdPago(String idPago);

    List<PagoModel> findByClienteIdCliente(String idCliente);

    List<PagoModel> findByMetodoPago(String metodoPago);

    boolean existsByIdPago(String idPago);

    void deleteByIdPago(String idPago);
}