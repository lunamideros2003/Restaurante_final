package co.ucc.pedidos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "pago")
public class PagoModel extends Transaccion {
    @Id
    @Column(name = "id_pago")
    private String idPago;
    @Column(name = "precio")
    private double precio;
    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private ClienteModel cliente;
    @Column(name = "procesado")
    private boolean procesado;

    public PagoModel() {
        super();
    }

    public PagoModel(double precio, String metodoPago, String idPago) {
        super(idPago, precio);
        this.precio = precio;
        this.metodoPago = metodoPago;
        this.idPago = idPago;
        this.procesado = false;
    }

    @Override
    public boolean validarTransaccion() {
        return metodoPago != null && !metodoPago.isEmpty() && getMonto() > 0;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getMonto() {
        return precio;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getIdPago() {
        return idPago;
    }

    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }

    public boolean isProcesado() {
        return procesado;
    }

    public void setProcesado(boolean procesado) {
        this.procesado = procesado;
    }
}
