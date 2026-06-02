package co.ucc.pedidos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ucc.pedidos.model.ProductoModel;
import co.ucc.pedidos.service.ProductoService;

/** API de platos del menú; GET público, escritura solo ADMIN (SecurityConfig). */
@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<ProductoModel> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoModel> obtenerProducto(@PathVariable String id) {
        return productoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping //
    public ResponseEntity<ProductoModel> registrarProducto(@RequestBody ProductoModel producto) {
        ProductoModel productoRegistrado = productoService.registrarProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoRegistrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoModel> actualizarProducto(
            @PathVariable String id,
            @RequestBody ProductoModel producto) {
        ProductoModel productoActualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
