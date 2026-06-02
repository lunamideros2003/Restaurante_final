import { Cliente } from './cliente.model';
import { Producto } from './producto.model';

export interface EstadoPedido {
  idEstado?: string;
  creado?: boolean;
  enviado?: boolean;
  entregado?: boolean;
  cancelado?: boolean;
}

export interface Pedido {
  idPedido: string;
  precio?: number;
  categoria?: string;
  lugarEntrega?: string;
  estado?: EstadoPedido;
  cliente?: Cliente;
  producto?: Producto;
}
