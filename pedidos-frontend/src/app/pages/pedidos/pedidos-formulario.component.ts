import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { PedidoService } from '../../core/services/pedido.service';
import { ClienteService } from '../../core/services/cliente.service';
import { ProductoService } from '../../core/services/producto.service';
import { AuthService } from '../../core/services/auth.service';
import { mensajeErrorApi } from '../../core/utils/api-error.util';

@Component({
  selector: 'app-pedidos-formulario',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './pedidos-formulario.component.html',
  styleUrl: './pedidos-formulario.component.scss'
})
export class PedidosFormularioComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly pedidoService = inject(PedidoService);
  private readonly clienteService = inject(ClienteService);
  private readonly productoService = inject(ProductoService);
  private readonly auth = inject(AuthService);

  esCliente = this.auth.isCliente();
  clientesIds = signal<string[]>([]);
  productosIds = signal<string[]>([]);
  mensaje = signal<{ tipo: 'ok' | 'error'; texto: string } | null>(null);

  form = this.fb.group({
    idPedido: ['', Validators.required],
    precio: [1, [Validators.required, Validators.min(0.01)]],
    categoria: ['electronica', Validators.required],
    lugarEntrega: ['', Validators.required],
    idCliente: ['', Validators.required],
    idProducto: ['', Validators.required]
  });

  ngOnInit(): void {
    if (this.esCliente) {
      const idCliente = this.auth.getIdCliente();
      if (idCliente) {
        this.form.patchValue({ idCliente });
        this.form.controls.idCliente.disable();
      }
    } else {
      this.clienteService.listar().subscribe({
        next: (c) => this.clientesIds.set(c.map((x) => x.idCliente)),
        error: () => {}
      });
    }
    this.productoService.listar().subscribe({
      next: (p) => this.productosIds.set(p.map((x) => x.idProducto)),
      error: () => {}
    });
  }

  /** Crea el pedido; si es CLIENTE, el idCliente se toma de la sesión. */
  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    const idCliente = v.idCliente || this.auth.getIdCliente();
    this.pedidoService.crear({
      idPedido: v.idPedido!,
      precio: Number(v.precio),
      categoria: v.categoria!,
      lugarEntrega: v.lugarEntrega!,
      cliente: { idCliente: idCliente! },
      producto: { idProducto: v.idProducto! }
    }).subscribe({
      next: () => {
        this.mensaje.set({ tipo: 'ok', texto: 'Pedido creado en PostgreSQL.' });
        this.form.reset({ precio: 1, categoria: 'electronica' });
      },
      error: (e) => {
        this.mensaje.set({ tipo: 'error', texto: mensajeErrorApi(e, 'Error al crear pedido') });
      }
    });
  }
}
