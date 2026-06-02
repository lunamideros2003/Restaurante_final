package co.ucc.pedidos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.OrderBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad del menú dinámico. La relación padre-hijo es recursiva:
 * padre_opcion_id apunta al id de otra fila en la misma tabla.
 */
@Entity
@Table(name = "opciones")
public class OpcionModel {

    // Identificador único de la opción (Clave Primaria)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre que se mostrará en la interfaz del menú
    @Column(nullable = false, length = 150)
    private String nombre;

    // ID del padre. Si es NULL, la opción es de nivel raíz (nivel 1)
    @Column(name = "padre_opcion_id")
    private Long padreOpcionId;

    // Ruta de navegación (ej: /clientes/crear) que Angular usará para el Router
    @Column(length = 255)
    private String ruta;

    // Nombre del icono de Material Icons (ej: 'home', 'person')
    @Column(length = 80)
    private String icono;

    // Valor numérico para determinar la posición entre hermanos
    @Column
    private Integer orden = 0;

    // RECURSIVIDAD: Relación ManyToOne hacia la misma entidad
    // Una opción "pertenece" a un padre que es también un OpcionModel
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_opcion_id", insertable = false, updatable = false)
    private OpcionModel padre;

    // RECURSIVIDAD: Relación OneToMany hacia la misma entidad
    // Una opción puede tener múltiples "hijos" que son también OpcionModel
    @OneToMany(mappedBy = "padre", fetch = FetchType.LAZY)
    @OrderBy("orden ASC")
    private List<OpcionModel> hijos = new ArrayList<>();

    public OpcionModel() {}

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

    public OpcionModel getPadre() {
        return padre;
    }

    public void setPadre(OpcionModel padre) {
        this.padre = padre;
    }

    public List<OpcionModel> getHijos() {
        return hijos;
    }

    public void setHijos(List<OpcionModel> hijos) {
        this.hijos = hijos;
    }
}
