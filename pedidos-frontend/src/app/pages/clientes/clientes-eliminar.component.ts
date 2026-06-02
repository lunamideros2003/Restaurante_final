import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ClienteService } from '../../core/services/cliente.service';

@Component({
  selector: 'app-clientes-eliminar',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './clientes-eliminar.component.html',
  styleUrl: './clientes-eliminar.component.scss'
})
export class ClientesEliminarComponent {
  private readonly fb = inject(FormBuilder);
  private readonly clienteService = inject(ClienteService);

  mensaje = signal<{ tipo: 'ok' | 'error'; texto: string } | null>(null);

  form = this.fb.group({
    idCliente: ['', Validators.required]
  });

  eliminar(): void {
    const id = this.form.value.idCliente?.trim();
    if (!id) return;

    if (!confirm(`¿Eliminar el cliente ${id}?`)) return;

    this.clienteService.eliminar(id).subscribe({
      next: () => {
        this.mensaje.set({ tipo: 'ok', texto: `Cliente ${id} eliminado.` });
        this.form.reset();
      },
      error: (e) => {
        this.mensaje.set({ tipo: 'error', texto: e.error?.message ?? 'No se pudo eliminar (¿existe el ID?)' });
      }
    });
  }
}
