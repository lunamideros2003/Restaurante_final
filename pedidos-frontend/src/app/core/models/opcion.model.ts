/** Registro plano tal como lo devuelve GET /api/opciones */
export interface OpcionPlana {
  id: number;
  nombre: string;
  padreOpcionId: number | null;
  ruta: string | null;
  icono?: string | null;
  orden?: number | null;
}

/** Nodo del árbol usado en el sidebar (con hijos anidados) */
export interface OpcionNodo {
  id: number;
  nombre: string;
  padreOpcionId: number | null;
  ruta: string | null;
  icono?: string | null;
  orden?: number | null;
  hijos: OpcionNodo[];
  expanded?: boolean;
}
