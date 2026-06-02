package co.ucc.pedidos.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del menú en formato árbol JSON, con hijos anidados recursivamente.
 */
public class OpcionArbolDto {

    private Long id;
    private String nombre;
    private Long padreOpcionId;
    private String ruta;
    private String icono;
    private Integer orden;
    private List<OpcionArbolDto> hijos = new ArrayList<>();

    public OpcionArbolDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getPadreOpcionId() {
        return padreOpcionId;
    }

    public void setPadreOpcionId(Long padreOpcionId) {
        this.padreOpcionId = padreOpcionId;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public List<OpcionArbolDto> getHijos() {
        return hijos;
    }

    public void setHijos(List<OpcionArbolDto> hijos) {
        this.hijos = hijos != null ? hijos : new ArrayList<>();
    }
}
