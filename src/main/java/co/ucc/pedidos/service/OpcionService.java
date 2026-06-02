package co.ucc.pedidos.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.ucc.pedidos.dto.OpcionArbolDto;
import co.ucc.pedidos.dto.OpcionDto;
import co.ucc.pedidos.model.OpcionModel;
import co.ucc.pedidos.repository.OpcionRepository;

@Service
public class OpcionService {

    @Autowired
    private OpcionRepository opcionRepository;

    /**
     * PASO 1: Obtiene todas las opciones de la BD en una lista plana (sin jerarquía).
     */
    @Transactional(readOnly = true)
    public List<OpcionDto> listarTodasPlanas() {
        return opcionRepository.findAllByOrderByOrdenAscIdAsc()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * PASO 2: Orquesta la construcción del árbol jerárquico.
     */
    @Transactional(readOnly = true)
    public List<OpcionArbolDto> obtenerMenuArbol() {
        List<OpcionDto> planas = listarTodasPlanas();
        return construirArbol(planas);
    }

    /**
     * PASO 3: Transforma la lista plana en una estructura de árbol (Padres e Hijos).
     * Usa un Mapa para indexar rápidamente por ID (Complejidad O(n)).
     */
    public List<OpcionArbolDto> construirArbol(List<OpcionDto> planas) {
        Map<Long, OpcionArbolDto> porId = new HashMap<>();
        List<OpcionArbolDto> raices = new ArrayList<>();

        // Primero, metemos todos los elementos en el mapa para buscarlos por ID fácilmente
        for (OpcionDto plana : planas) {
            porId.put(plana.getId(), toArbolDto(plana));
        }

        // Segundo, recorremos de nuevo para asignar cada hijo a su padre correspondiente
        for (OpcionDto plana : planas) {
            OpcionArbolDto nodo = porId.get(plana.getId());
            Long padreId = plana.getPadreOpcionId();

            if (padreId == null) {
                // Si no tiene padre, es una raíz (nivel superior)
                raices.add(nodo);
            } else {
                // Si tiene padre, lo buscamos en el mapa y lo agregamos a su lista de hijos
                OpcionArbolDto padre = porId.get(padreId);
                if (padre != null) {
                    padre.getHijos().add(nodo);
                } else {
                    // Si el padre no existe (error de datos), lo tratamos como raíz
                    raices.add(nodo);
                }
            }
        }

        // PASO 4: Aplicamos recursividad para ordenar cada nivel del árbol
        ordenarArbolRecursivo(raices);
        return raices;
    }

    /**
     * PASO 5 (RECURSIVIDAD): Método que se llama a sí mismo para recorrer y ordenar
     * cada nivel del árbol de opciones, sin importar la profundidad.
     */
    private void ordenarArbolRecursivo(List<OpcionArbolDto> nodos) {
        // Ordenamos los nodos del nivel actual por el campo 'orden'
        nodos.sort(Comparator
                .comparing(OpcionArbolDto::getOrden, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(OpcionArbolDto::getId));

        // Por cada nodo, revisamos si tiene hijos
        for (OpcionArbolDto nodo : nodos) {
            if (!nodo.getHijos().isEmpty()) {
                // SI TIENE HIJOS: Se llama a la misma función para ordenar el siguiente nivel
                // Esto es lo que permite profundidad infinita
                ordenarArbolRecursivo(nodo.getHijos());
            }
        }
    }

    private OpcionDto toDto(OpcionModel entity) {
        return new OpcionDto(
                entity.getId(),
                entity.getNombre(),
                entity.getPadreOpcionId(),
                entity.getRuta(),
                entity.getIcono(),
                entity.getOrden());
    }

    private OpcionArbolDto toArbolDto(OpcionDto dto) {
        OpcionArbolDto arbol = new OpcionArbolDto();
        arbol.setId(dto.getId());
        arbol.setNombre(dto.getNombre());
        arbol.setPadreOpcionId(dto.getPadreOpcionId());
        arbol.setRuta(dto.getRuta());
        arbol.setIcono(dto.getIcono());
        arbol.setOrden(dto.getOrden());
        return arbol;
    }
}
