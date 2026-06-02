export type Rol = 'ADMIN' | 'CLIENTE';

export interface AuthResponse {
  token: string;
  email: string;
  nombre: string;
  rol: Rol;
  idCliente?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  nombre: string;
  idCliente?: string;
  genero?: string;
  direccion?: string;
}

export interface AuthSession {
  token: string;
  email: string;
  nombre: string;
  rol: Rol;
  idCliente?: string;
}
