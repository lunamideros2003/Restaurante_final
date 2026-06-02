package co.ucc.pedidos.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class PedidoModel {
    @Id
    @Column(name = "id_pedido")
    private String idPedido;
    @Column(name = "total")
    private Double total;
    @Column(name = "lugar_entrega", length = 255)
    private String lugarEntrega;
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado")
    private EstadoModel estado;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fecha_pedido")
    private FechaPedidoModel fechaPedido;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_cliente")
    private ClienteModel cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedidoModel> detalles = new ArrayList<>();

    public PedidoModel() {
        this.estado = new EstadoModel();
        this.fechaPedido = new FechaPedidoModel();
    }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getIdPedido() { return idPedido; }
    public void setIdPedido(String idPedido) { this.idPedido = idPedido; }

    public EstadoModel getEstado() { return estado; }
    public void setEstado(EstadoModel estado) { this.estado = estado; }

    public String getLugarEntrega() { return lugarEntrega; }
    public void setLugarEntrega(String lugarEntrega) { this.lugarEntrega = lugarEntrega; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public FechaPedidoModel getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(FechaPedidoModel fechaPedido) { this.fechaPedido = fechaPedido; }

    public ClienteModel getCliente() { return cliente; }
    public void setCliente(ClienteModel cliente) { this.cliente = cliente; }

    public List<DetallePedidoModel> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoModel> detalles) { this.detalles = detalles; }
}
