package co.ucc.pedidos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ucc.pedidos.model.ClienteModel;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, String> {

    Optional<ClienteModel> findByIdCliente(String idCliente);

    Optional<ClienteModel> findByCorreoElectronico(String correoElectronico);

    Optional<ClienteModel> findByNombre(String nombre);

    boolean existsByIdCliente(String idCliente);

    boolean existsByCorreoElectronico(String correoElectronico);

    void deleteByIdCliente(String idCliente);
}
