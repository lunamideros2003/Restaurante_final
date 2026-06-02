package co.ucc.pedidos.model;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Transaccion {
    @Column(name = "id_transaccion")
    private String idTransaccion;
    @Column(name = "fecha_transaccion")
    private LocalDateTime fechaTransaccion;
    @Column(name = "monto")
    private double monto;
    @Column(name = "estado", length = 50)
    private String estado;

    public Transaccion() {
        this.fechaTransaccion = LocalDateTime.now();
    }

    public Transaccion(String idTransaccion, double monto) {
        this.idTransaccion = idTransaccion;
        this.monto = monto;
        this.fechaTransaccion = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }

    public abstract boolean validarTransaccion();

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public LocalDateTime getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(LocalDateTime fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
