package co.ucc.pedidos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.ucc.pedidos.exception.ResourceAlreadyExistsException;
import co.ucc.pedidos.model.ProductoModel;
import co.ucc.pedidos.repository.CategoriaRepository;
import co.ucc.pedidos.repository.ProductoRepository;

/** Gestión de platos de la carta del restaurante (productos del menú). */
@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<ProductoModel> listarProductos() {
        return productoRepository.findAll();
    }

    public Optional<ProductoModel> buscarPorId(String idProducto) {
        return productoRepository.findByIdProducto(idProducto);
    }

    /** Registra un plato nuevo; el precio debe ser mayor que cero y el ID único. */
    public ProductoModel registrarProducto(ProductoModel producto) {
        validarPrecio(producto.getPrecio());
        if (productoRepository.existsByIdProducto(producto.getIdProducto())) {
            throw new ResourceAlreadyExistsException("El producto con ID " + producto.getIdProducto() + " ya existe");
        }
        
        // Vincular categoría si viene el ID
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Long idCat = producto.getCategoria().getId();
            producto.setCategoria(categoriaRepository.findById(idCat)
                .orElse(null));
        }
        
        return productoRepository.save(producto);
    }

    public ProductoModel actualizarProducto(String idProducto, ProductoModel producto) {
        validarPrecio(producto.getPrecio());
        return productoRepository.findByIdProducto(idProducto)
                .map(p -> {
                    p.setCantidad(producto.getCantidad());
                    p.setPrecio(producto.getPrecio());
                    p.setResena(producto.getResena());
                    return productoRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    private void validarPrecio(Double precio) {
        if (precio == null || precio <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor que 0");
        }
    }

    public void eliminarProducto(String idProducto) {
        productoRepository.deleteByIdProducto(idProducto);
    }
}
