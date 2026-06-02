package co.ucc.pedidos.dto;

/**
 * Representación plana de una opción de menú (como viene de la BD).
 */
public class OpcionDto {

    private Long id;
    private String nombre;
    private Long padreOpcionId;
    private String ruta;
    private String icono;
    private Integer orden;

    public OpcionDto() {}

    public OpcionDto(Long id, String nombre, Long padreOpcionId, String ruta, String icono, Integer orden) {
        this.id = id;
        this.nombre = nombre;
        this.padreOpcionId = padreOpcionId;
        this.ruta = ruta;
        this.icono = icono;
        this.orden = orden;
    }

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
}
