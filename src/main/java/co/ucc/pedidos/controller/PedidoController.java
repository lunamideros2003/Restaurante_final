package co.ucc.pedidos.controller;

import co.ucc.pedidos.model.ClienteModel;
import co.ucc.pedidos.model.PedidoModel;
import co.ucc.pedidos.security.SecurityUtils;
import co.ucc.pedidos.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** API REST de pedidos; los clientes solo pueden crear pedidos con su propio idCliente. */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<PedidoModel> listarPedidos() {
        return pedidoService.findAll();
    }

    /** Lista pedidos del cliente autenticado (rol CLIENTE). */
    @GetMapping("/mios")
    public List<PedidoModel> misPedidos() {
        String idCliente = SecurityUtils.getIdClienteActual();
        if (idCliente == null || idCliente.isBlank()) {
            throw new IllegalArgumentException("No se encontró el cliente asociado al usuario");
        }
        return pedidoService.findByClienteId(idCliente);
    }

    @GetMapping("/")
    public ResponseEntity<String> raiz() {
        return ResponseEntity.ok("API de pedidos: use /api/pedidos para listar pedidos (admin) o /api/pedidos/mios (cliente)");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoModel> obtenerPedido(@PathVariable String id) {
        return pedidoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Crea pedido; si el usuario es CLIENTE, fuerza su idCliente en el cuerpo. */
    @PostMapping
    public ResponseEntity<PedidoModel> crearPedido(@RequestBody PedidoModel pedido) {
        if (SecurityUtils.esCliente()) {
            String idCliente = SecurityUtils.getIdClienteActual();
            if (pedido.getCliente() == null) {
                pedido.setCliente(new ClienteModel());
            }
            pedido.getCliente().setIdCliente(idCliente);
        }
        return pedidoService.save(pedido)
                .map(p -> ResponseEntity.status(HttpStatus.CREATED).body(p))
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable String id) {
        return pedidoService.delete(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}