package co.ucc.pedidos.service;

import co.ucc.pedidos.exception.ResourceAlreadyExistsException;
import co.ucc.pedidos.exception.ResourceNotFoundException;
import co.ucc.pedidos.model.ClienteModel;
import co.ucc.pedidos.model.EstadoModel;
import co.ucc.pedidos.model.PedidoModel;
import co.ucc.pedidos.model.ProductoModel;
import co.ucc.pedidos.model.DetallePedidoModel;
import co.ucc.pedidos.repository.ClienteRepository;
import co.ucc.pedidos.repository.PedidoRepository;
import co.ucc.pedidos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Lógica de pedidos del restaurante: valida detalles, vincula cliente/productos
 * y calcula el total antes de persistir.
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    public List<PedidoModel> findAll() {
        return pedidoRepository.findAll();
    }

    public Optional<PedidoModel> findById(String id) {
        return pedidoRepository.findByIdPedido(id);
    }

    /** Crea un pedido con al menos un detalle; rechaza IDs duplicados. */
    @Transactional
    public Optional<PedidoModel> save(PedidoModel pedido) {
        if (pedido == null || pedido.getIdPedido() == null || pedido.getIdPedido().isBlank()) {
            return Optional.empty();
        }

        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un producto");
        }

        limpiarReferenciasSinPersistir(pedido);
        vincularClienteExistente(pedido);
        vincularProductosYCalcularTotal(pedido);

        if (pedidoRepository.existsByIdPedido(pedido.getIdPedido())) {
            throw new ResourceAlreadyExistsException(
                    "Ya existe un pedido con id \"" + pedido.getIdPedido() + "\". Usa otro idPedido o elimínalo antes.");
        }

        return Optional.of(pedidoRepository.save(pedido));
    }

    private void vincularClienteExistente(PedidoModel pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().getIdCliente() == null
                || pedido.getCliente().getIdCliente().isBlank()) {
            throw new IllegalArgumentException("Debe indicar un cliente existente (idCliente).");
        }
        String idCliente = pedido.getCliente().getIdCliente().trim();
        ClienteModel cliente = clienteRepository.findByIdCliente(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: " + idCliente));
        pedido.setCliente(cliente);
    }

    /** Resuelve cada plato en BD, asigna precio unitario y suma el total del pedido. */
    private void vincularProductosYCalcularTotal(PedidoModel pedido) {
        double total = 0;
        for (DetallePedidoModel detalle : pedido.getDetalles()) {
            if (detalle.getProducto() == null || detalle.getProducto().getIdProducto() == null) {
                throw new IllegalArgumentException("Cada detalle debe tener un producto válido");
            }
            String idProd = detalle.getProducto().getIdProducto();
            ProductoModel prod = productoRepository.findByIdProducto(idProd)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + idProd));
            
            detalle.setProducto(prod);
            detalle.setPedido(pedido);
            detalle.setPrecioUnitario(prod.getPrecio());
            total += prod.getPrecio() * detalle.getCantidad();
        }
        pedido.setTotal(total);
    }

    private void limpiarReferenciasSinPersistir(PedidoModel pedido) {
        if (pedido.getEstado() != null) {
            if (pedido.getEstado().getIdEstado() == null) {
                pedido.getEstado().setIdEstado("EST-" + System.currentTimeMillis());
            }
            if (pedido.getEstado().getNombreEstado() == null) {
                pedido.getEstado().setNombreEstado("PENDIENTE");
            }
        }
        if (pedido.getFechaPedido() != null && pedido.getFechaPedido().getIdFechaPedido() == null) {
            pedido.getFechaPedido().setIdFechaPedido("FEC-" + System.currentTimeMillis());
        }
    }

    @Transactional
    public boolean delete(String id) {
        if (pedidoRepository.existsByIdPedido(id)) {
            pedidoRepository.deleteByIdPedido(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Optional<PedidoModel> actualizarEstado(String id, String nombreEstado) {
        Optional<PedidoModel> pedidoOpt = pedidoRepository.findByIdPedido(id);
        if (pedidoOpt.isPresent()) {
            PedidoModel pedido = pedidoOpt.get();
            if (pedido.getEstado() == null) {
                pedido.setEstado(new EstadoModel());
                pedido.getEstado().setIdEstado("EST-" + System.currentTimeMillis());
            }
            pedido.getEstado().setNombreEstado(nombreEstado);
            return Optional.of(pedidoRepository.save(pedido));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<PedidoModel> cancelarPedido(String idPedido) {
        return actualizarEstado(idPedido, "CANCELADO");
    }

    public List<PedidoModel> findByClienteId(String idCliente) {
        return pedidoRepository.findByClienteIdCliente(idCliente);
    }
}
