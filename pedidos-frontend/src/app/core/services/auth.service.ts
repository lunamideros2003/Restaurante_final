import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, AuthSession, LoginRequest, RegisterRequest, Rol } from '../models/auth.model';

const STORAGE_KEY = 'pedidos_auth';

/** Sesión JWT en localStorage; expone rol ADMIN/CLIENTE para guards y componentes. */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly url = `${environment.apiUrl}/auth`;

  readonly session = signal<AuthSession | null>(this.loadSession());

  readonly adminEmail = 'admin@pedidos.com';

  login(data: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.url}/login`, data);
  }

  register(data: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.url}/register`, data);
  }

  /** Guarda token y datos de usuario tras login o registro exitoso. */
  establecerSesion(res: AuthResponse): void {
    this.persistSession(res);
  }

  logout(): void {
    localStorage.removeItem(STORAGE_KEY);
    this.session.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this.session()?.token ?? null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getRol(): Rol | null {
    return this.session()?.rol ?? null;
  }

  isAdmin(): boolean {
    return this.getRol() === 'ADMIN';
  }

  isCliente(): boolean {
    return this.getRol() === 'CLIENTE';
  }

  getIdCliente(): string | null {
    return this.session()?.idCliente ?? null;
  }

  private persistSession(res: AuthResponse): void {
    const session: AuthSession = {
      token: res.token,
      email: res.email,
      nombre: res.nombre,
      rol: res.rol,
      idCliente: res.idCliente
    };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(session));
    this.session.set(session);
  }

  private loadSession(): AuthSession | null {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      return raw ? (JSON.parse(raw) as AuthSession) : null;
    } catch {
      return null;
    }
  }
}
