import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { Rol } from '../../core/models/auth.model';
import { mensajeErrorApi } from '../../core/utils/api-error.util';

type TipoAcceso = 'CLIENTE' | 'ADMIN';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './auth.component.scss'
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  tipoAcceso = signal<TipoAcceso>('CLIENTE');
  mensaje = signal<{ tipo: 'ok' | 'error'; texto: string } | null>(null);
  enviando = signal(false);

  readonly adminEmail = this.auth.adminEmail;

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  cambiarTipo(tipo: TipoAcceso): void {
    this.tipoAcceso.set(tipo);
    this.mensaje.set(null);
    if (tipo === 'ADMIN') {
      this.form.patchValue({ email: this.adminEmail, password: '' });
    } else {
      this.form.patchValue({ email: '', password: '' });
    }
  }

  /** Autentica y comprueba que el rol del token coincida con Cliente o Admin elegido. */
  iniciarSesion(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.mensaje.set({ tipo: 'error', texto: 'Completa correo y contraseña (mínimo 6 caracteres).' });
      return;
    }
    this.enviando.set(true);
    this.mensaje.set(null);
    const v = this.form.getRawValue();
    const tipo = this.tipoAcceso();

    this.auth.login({ email: v.email!.trim().toLowerCase(), password: v.password! }).subscribe({
      next: (res) => {
        if (!this.rolCoincide(res.rol, tipo)) {
          this.mensaje.set({
            tipo: 'error',
            texto: tipo === 'ADMIN'
              ? 'Esta cuenta no es de administrador. Elige "Cliente" o usa admin@pedidos.com.'
              : 'Esta cuenta es de administrador. Elige "Administrador" para entrar.'
          });
          this.enviando.set(false);
          return;
        }
        this.auth.establecerSesion(res);
        this.router.navigate(['/inicio']);
      },
      error: (e) => {
        this.mensaje.set({ tipo: 'error', texto: mensajeErrorApi(e, 'Credenciales inválidas') });
        this.enviando.set(false);
      },
      complete: () => this.enviando.set(false)
    });
  }

  campoInvalido(nombre: 'email' | 'password'): boolean {
    const c = this.form.controls[nombre];
    return c.invalid && (c.touched || c.dirty);
  }

  private rolCoincide(rol: Rol, tipo: TipoAcceso): boolean {
    return (tipo === 'ADMIN' && rol === 'ADMIN') || (tipo === 'CLIENTE' && rol === 'CLIENTE');
  }
}
