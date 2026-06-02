import { Categoria } from './categoria.model';

export interface Producto {
  idProducto: string;
  cantidad?: number;
  precio?: number;
  resena?: string;
  imagenUrl?: string;
  disponible?: boolean;
  categoria?: Categoria;
}
