import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ClienteService } from '../../core/services/cliente.service';
import { Cliente } from '../../core/models/cliente.model';

@Component({
  selector: 'app-clientes-lista',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './clientes-lista.component.html',
  styleUrl: './clientes-lista.component.scss'
})
export class ClientesListaComponent implements OnInit {
  private readonly clienteService = inject(ClienteService);

  clientes = signal<Cliente[]>([]);
  cargando = signal(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando.set(true);
    this.error.set(null);
    this.clienteService.listar().subscribe({
      next: (data) => {
        this.clientes.set(data);
        this.cargando.set(false);
      },
      error: (e) => {
        this.error.set(e.error?.message ?? 'Error al cargar clientes');
        this.cargando.set(false);
      }
    });
  }

  correo(c: Cliente): string {
    return c.correo ?? c.correoElectronico ?? '—';
  }
}
