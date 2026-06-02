import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { mensajeErrorApi } from '../../core/utils/api-error.util';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './auth.component.scss'
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  mensaje = signal<{ tipo: 'ok' | 'error'; texto: string } | null>(null);
  enviando = signal(false);

  form = this.fb.group({
    nombre: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmarPassword: ['', [Validators.required, Validators.minLength(6)]],
    genero: ['otro'],
    direccion: ['']
  });

  registrar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.mensaje.set({ tipo: 'error', texto: 'Revisa los campos marcados antes de guardar.' });
      return;
    }
    const v = this.form.getRawValue();
    if (v.password !== v.confirmarPassword) {
      this.mensaje.set({ tipo: 'error', texto: 'Las contraseñas no coinciden.' });
      return;
    }
    if (v.email!.trim().toLowerCase() === this.auth.adminEmail) {
      this.mensaje.set({ tipo: 'error', texto: 'Este correo está reservado para el administrador.' });
      return;
    }

    this.enviando.set(true);
    this.mensaje.set(null);

    this.auth.register({
      nombre: v.nombre!.trim(),
      email: v.email!.trim().toLowerCase(),
      password: v.password!,
      genero: v.genero || 'otro',
      direccion: v.direccion || ''
    }).subscribe({
      next: (res) => {
        this.auth.establecerSesion(res);
        this.mensaje.set({
          tipo: 'ok',
          texto: `Cuenta creada. Tu ID de cliente es ${res.idCliente ?? '—'}.`
        });
        setTimeout(() => this.router.navigate(['/inicio']), 800);
      },
      error: (e) => {
        this.mensaje.set({ tipo: 'error', texto: mensajeErrorApi(e, 'Error al registrarse') });
        this.enviando.set(false);
      },
      complete: () => this.enviando.set(false)
    });
  }

  campoInvalido(nombre: 'nombre' | 'email' | 'password' | 'confirmarPassword'): boolean {
    const c = this.form.controls[nombre];
    return c.invalid && (c.touched || c.dirty);
  }
}
