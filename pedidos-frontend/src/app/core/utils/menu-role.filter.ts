import { OpcionNodo } from '../models/opcion.model';
import { Rol } from '../models/auth.model';

/** Rutas del menú lateral que solo debe ver el administrador del restaurante. */
const RUTAS_SOLO_ADMIN = [
  '/clientes',
  '/clientes/crear',
  '/clientes/editar',
  '/clientes/eliminar',
  '/productos/crear',
  '/productos/categorias',
  '/pedidos/reportes'
];

/** Oculta opciones de administración cuando el usuario es CLIENTE. */
export function filtrarMenuPorRol(arbol: OpcionNodo[], rol: Rol | null): OpcionNodo[] {
  if (rol === 'ADMIN') {
    return arbol;
  }
  return arbol
    .map((nodo) => filtrarNodo(nodo))
    .filter((n) => n !== null) as OpcionNodo[];
}

function filtrarNodo(nodo: OpcionNodo): OpcionNodo | null {
  const hijos = (nodo.hijos ?? [])
    .map((h) => filtrarNodo(h))
    .filter((h): h is OpcionNodo => h !== null);

  const ruta = nodo.ruta ?? '';
  const esAdminOnly = RUTAS_SOLO_ADMIN.some((r) => ruta === r || ruta.startsWith(r + '/'));

  if (esAdminOnly) {
    return null;
  }

  if (ruta === '/clientes' && hijos.length === 0) {
    return null;
  }

  return { ...nodo, hijos };
}
