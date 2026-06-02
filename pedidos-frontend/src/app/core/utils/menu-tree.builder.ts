import { OpcionNodo, OpcionPlana } from '../models/opcion.model';

/**
 * PASO 1: Construye el árbol de menú desde una lista plana (misma lógica que el backend).
 * Permite que Angular maneje el menú como una estructura de objetos anidados.
 */
export function buildMenuTree(planas: OpcionPlana[]): OpcionNodo[] {
  const porId = new Map<number, OpcionNodo>();
  const raices: OpcionNodo[] = [];

  // Indexamos todos los nodos en un Map para acceso instantáneo
  for (const plana of planas) {
    porId.set(plana.id, {
      ...plana,
      hijos: [],
      expanded: false
    });
  }

  // PASO 2: Recorremos para enlazar hijos con sus padres
  for (const plana of planas) {
    const nodo = porId.get(plana.id)!;
    const padreId = plana.padreOpcionId;

    if (padreId === null || padreId === undefined) {
      // Si no tiene padre, es un menú de nivel superior
      raices.push(nodo);
    } else {
      // Si tiene padre, lo buscamos y lo añadimos a su lista de hijos
      const padre = porId.get(padreId);
      if (padre) {
        padre.hijos.push(nodo);
      } else {
        // Por seguridad, si el padre no se encuentra, queda como raíz
        raices.push(nodo);
      }
    }
  }

  // PASO 3: Aplicamos recursividad para ordenar cada nivel
  ordenarNodos(raices);
  return raices;
}

/**
 * RECURSIVIDAD: Función que ordena los nodos y sus hijos recursivamente.
 * Se asegura de que el menú se vea en el orden correcto en todos los subniveles.
 */
function ordenarNodos(nodos: OpcionNodo[]): void {
  // Ordena el nivel actual
  nodos.sort((a, b) => (a.orden ?? 0) - (b.orden ?? 0) || a.id - b.id);
  
  // Por cada nodo que tenga hijos...
  nodos.forEach((n) => {
    if (n.hijos.length) {
      // LLAMADA RECURSIVA: Ordena los hijos del nivel siguiente
      ordenarNodos(n.hijos);
    }
  });
}
