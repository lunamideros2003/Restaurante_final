package co.ucc.pedidos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ucc.pedidos.model.ProductoModel;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, String> {

    Optional<ProductoModel> findByIdProducto(String idProducto);

    List<ProductoModel> findByCategoriaId(Long idCategoria);

    boolean existsByIdProducto(String idProducto);

    void deleteByIdProducto(String idProducto);
}  ///