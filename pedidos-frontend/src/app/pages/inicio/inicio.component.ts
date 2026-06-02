import { Component, OnInit, inject, signal } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ClienteService } from '../../core/services/cliente.service';
import { ProductoService } from '../../core/services/producto.service';
import { PedidoService } from '../../core/services/pedido.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-inicio',
  standalone: true,
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.scss'
})
export class InicioComponent implements OnInit {
  private readonly clienteService = inject(ClienteService);
  private readonly productoService = inject(ProductoService);
  private readonly pedidoService = inject(PedidoService);
  private readonly auth = inject(AuthService);

  esAdmin = this.auth.isAdmin();
  clientes = signal(0);
  productos = signal(0);
  pedidos = signal(0);
  cargando = signal(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    const pedidos$ = this.auth.isCliente()
      ? this.pedidoService.listarMios()
      : this.pedidoService.listar();
    const clientes$ = this.auth.isAdmin()
      ? this.clienteService.listar()
      : of([]);

    forkJoin({
      clientes: clientes$.pipe(catchError(() => of([]))),
      productos: this.productoService.listar().pipe(catchError(() => of([]))),
      pedidos: pedidos$.pipe(catchError(() => of([])))
    }).subscribe({
      next: (r) => {
        this.clientes.set(r.clientes.length);
        this.productos.set(r.productos.length);
        this.pedidos.set(r.pedidos.length);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar los datos. ¿Está el backend en http://localhost:8080?');
        this.cargando.set(false);
      }
    });
  }
}
