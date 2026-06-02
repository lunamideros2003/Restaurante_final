import { DecimalPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { PedidoService } from '../../core/services/pedido.service';
import { Pedido } from '../../core/models/pedido.model';

@Component({
  selector: 'app-pedidos-reportes',
  standalone: true,
  imports: [DecimalPipe],
  templateUrl: './pedidos-reportes.component.html',
  styleUrl: './pedidos-reportes.component.scss'
})
export class PedidosReportesComponent implements OnInit {
  private readonly pedidoService = inject(PedidoService);

  totalPedidos = signal(0);
  totalVentas = signal(0);
  promedio = signal(0);
  porCategoria = signal<{ nombre: string; cantidad: number }[]>([]);
  cargando = signal(true);

  ngOnInit(): void {
    this.pedidoService.listar().subscribe({
      next: (pedidos) => this.calcular(pedidos),
      error: () => this.cargando.set(false)
    });
  }

  private calcular(pedidos: Pedido[]): void {
    this.totalPedidos.set(pedidos.length);
    const suma = pedidos.reduce((acc, p) => acc + (p.precio ?? 0), 0);
    this.totalVentas.set(suma);
    this.promedio.set(pedidos.length ? suma / pedidos.length : 0);

    const map = new Map<string, number>();
    for (const p of pedidos) {
      const cat = p.categoria ?? 'Sin categoría';
      map.set(cat, (map.get(cat) ?? 0) + 1);
    }
    this.porCategoria.set([...map.entries()].map(([nombre, cantidad]) => ({ nombre, cantidad })));
    this.cargando.set(false);
  }
}
