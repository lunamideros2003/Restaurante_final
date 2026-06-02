import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ClienteService } from '../../core/services/cliente.service';
import { mensajeErrorApi } from '../../core/utils/api-error.util';

@Component({
  selector: 'app-clientes-formulario',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './clientes-formulario.component.html',
  styleUrl: './clientes-formulario.component.scss'
})
export class ClientesFormularioComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly clienteService = inject(ClienteService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  modoEdicion = signal(false);
  mensaje = signal<{ tipo: 'ok' | 'error'; texto: string } | null>(null);
  enviando = signal(false);

  form = this.fb.group({
    idCliente: ['', Validators.required],
    nombre: ['', Validators.required],
    correo: ['', [Validators.required, Validators.email]],
    genero: ['femenino'],
    direccion: ['']
  });

  ngOnInit(): void {
    this.modoEdicion.set(this.route.snapshot.data['modo'] === 'editar');
    if (this.modoEdicion()) {
      this.form.controls.idCliente.disable();
    }
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const raw = this.form.getRawValue();
    const correo = raw.correo!.trim();
    const cliente = {
      idCliente: raw.idCliente!.trim(),
      nombre: raw.nombre!.trim(),
      correo,
      correoElectronico: correo,
      genero: raw.genero || undefined,
      direccion: raw.direccion?.trim() || undefined
    };

    this.enviando.set(true);
    this.mensaje.set(null);

    const req = this.modoEdicion()
      ? this.clienteService.actualizar(cliente.idCliente, cliente)
      : this.clienteService.crear(cliente);

    req.subscribe({
      next: () => {
        this.mensaje.set({ tipo: 'ok', texto: this.modoEdicion() ? 'Cliente actualizado.' : 'Cliente creado en la base de datos.' });
        this.enviando.set(false);
        if (!this.modoEdicion()) {
          this.form.reset({ genero: 'femenino' });
        }
      },
      error: (e) => {
        this.mensaje.set({ tipo: 'error', texto: mensajeErrorApi(e, 'No se pudo guardar el cliente') });
        this.enviando.set(false);
      }
    });
  }

  buscarParaEditar(): void {
    const id = this.form.controls.idCliente.value?.trim();
    if (!id) return;
    this.clienteService.obtener(id).subscribe({
      next: (c) => {
        this.form.patchValue({
          nombre: c.nombre ?? '',
          correo: c.correo ?? c.correoElectronico ?? '',
          genero: c.genero ?? '',
          direccion: c.direccion ?? ''
        });
        this.mensaje.set({ tipo: 'ok', texto: 'Cliente cargado.' });
      },
      error: () => this.mensaje.set({ tipo: 'error', texto: 'Cliente no encontrado.' })
    });
  }
}
