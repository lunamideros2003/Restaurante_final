package co.ucc.pedidos.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "fecha_pedido")
public class FechaPedidoModel {
    @Id
    @Column(name = "id_fecha_pedido")
    private String idFechaPedido;
    @Column(name = "fecha_pedir")
    private LocalDateTime fechaPedir;
    @Column(name = "fecha_recibir")
    private LocalDateTime fechaRecibir;
    @Column(name = "fecha_estimada")
    private LocalDateTime fechaEstimada;
    @OneToOne(mappedBy = "fechaPedido", fetch = FetchType.LAZY)
    @JsonIgnore
    private PedidoModel pedido;

    public FechaPedidoModel() {}

    public String getIdFechaPedido() { return idFechaPedido; }
    public void setIdFechaPedido(String idFechaPedido) { this.idFechaPedido = idFechaPedido; }

    public LocalDateTime getFechaPedir() { return fechaPedir; }
    public void setFechaPedir(LocalDateTime fechaPedir) { this.fechaPedir = fechaPedir; }

    public LocalDateTime getFechaRecibir() { return fechaRecibir; }
    public void setFechaRecibir(LocalDateTime fechaRecibir) { this.fechaRecibir = fechaRecibir; }

    public LocalDateTime getFechaEstimada() { return fechaEstimada; }
    public void setFechaEstimada(LocalDateTime fechaEstimada) { this.fechaEstimada = fechaEstimada; }

    public PedidoModel getPedido() { return pedido; }
    public void setPedido(PedidoModel pedido) { this.pedido = pedido; }
}
