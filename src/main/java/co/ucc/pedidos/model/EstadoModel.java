package co.ucc.pedidos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "estado")
public class EstadoModel {
    @Id
    @Column(name = "id_estado")
    private String idEstado;
    
    @Column(name = "nombre_estado", length = 50)
    private String nombreEstado; // PENDIENTE, PREPARANDO, LISTO, ENTREGADO, CANCELADO

    @OneToOne(mappedBy = "estado", fetch = FetchType.LAZY)
    @JsonIgnore
    private PedidoModel pedido;

    public EstadoModel() {
        this.nombreEstado = "PENDIENTE";
    }

    public String getIdEstado() { return idEstado; }
    public void setIdEstado(String idEstado) { this.idEstado = idEstado; }

    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }

    public PedidoModel getPedido() { return pedido; }
    public void setPedido(PedidoModel pedido) { this.pedido = pedido; }
}
