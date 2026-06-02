import { DecimalPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PedidoService } from '../../core/services/pedido.service';
import { AuthService } from '../../core/services/auth.service';
import { Pedido } from '../../core/models/pedido.model';

@Component({
  selector: 'app-pedidos-lista',
  standalone: true,
  imports: [DecimalPipe],
  templateUrl: './pedidos-lista.component.html',
  styleUrl: './pedidos-lista.component.scss'
})
export class PedidosListaComponent implements OnInit {
  private readonly pedidoService = inject(PedidoService);
  private readonly auth = inject(AuthService);
  private readonly route = inject(ActivatedRoute);

  esAdmin = this.auth.isAdmin();

  titulo = signal('Pedidos');
  pedidos = signal<Pedido[]>([]);
  cargando = signal(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    const modo = this.route.snapshot.data['modo'];
    if (modo === 'historial') {
      this.titulo.set('Historial de Pedidos');
    }
    this.cargar();
  }

  cargar(): void {
    this.cargando.set(true);
    const peticion = this.auth.isCliente()
      ? this.pedidoService.listarMios()
      : this.pedidoService.listar();
    peticion.subscribe({
      next: (data) => {
        this.pedidos.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('Error al cargar pedidos');
        this.cargando.set(false);
      }
    });
  }

  eliminar(id: string): void {
    if (!confirm(`¿Eliminar pedido ${id}?`)) return;
    this.pedidoService.eliminar(id).subscribe({
      next: () => this.cargar(),
      error: () => this.error.set('No se pudo eliminar el pedido')
    });
  }
}
