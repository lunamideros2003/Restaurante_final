import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Pedido } from '../models/pedido.model';

/** Cliente HTTP para la API de pedidos del restaurante (/api/pedidos). */
@Injectable({ providedIn: 'root' })
export class PedidoService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/pedidos`;

  listar(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(this.url);
  }

  /** Pedidos del cliente autenticado (usa JWT en el interceptor). */
  listarMios(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.url}/mios`);
  }

  crear(pedido: Pedido): Observable<Pedido> {
    return this.http.post<Pedido>(this.url, pedido);
  }

  eliminar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
