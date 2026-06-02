import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { OpcionNodo, OpcionPlana } from '../models/opcion.model';
import { buildMenuTree } from '../utils/menu-tree.builder';

@Injectable({ providedIn: 'root' })
export class MenuService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/opciones`;

  /** Lista plana desde la API */
  obtenerPlanas(): Observable<OpcionPlana[]> {
    return this.http.get<OpcionPlana[]>(this.baseUrl);
  }

  /** Árbol ya construido en el backend */
  obtenerArbol(): Observable<OpcionNodo[]> {
    return this.http.get<OpcionNodo[]>(`${this.baseUrl}/arbol`);
  }

  /** Lista plana + construcción del árbol en Angular (recursividad en cliente) */
  obtenerMenuConstruidoEnCliente(): Observable<OpcionNodo[]> {
    return this.obtenerPlanas().pipe(map((planas) => buildMenuTree(planas)));
  }
}
