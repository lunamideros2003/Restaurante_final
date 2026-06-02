import { Component, inject, signal, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProductoService } from '../../core/services/producto.service';
import { CategoriaService } from '../../core/services/categoria.service';
import { Categoria } from '../../core/models/categoria.model';
import { mensajeErrorApi } from '../../core/utils/api-error.util';

@Component({
  selector: 'app-productos-formulario',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './productos-formulario.component.html',
  styleUrl: './productos-formulario.component.scss'
})
export class ProductosFormularioComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly productoService = inject(ProductoService);
  private readonly categoriaService = inject(CategoriaService);

  mensaje = signal<{ tipo: 'ok' | 'error'; texto: string } | null>(null);
  categorias = signal<Categoria[]>([]);

  form = this.fb.group({
    idProducto: ['', Validators.required],
    precio: [0.01, [Validators.required, Validators.min(0.01)]],
    resena: [''],
    categoriaId: ['']
  });

  ngOnInit(): void {
    this.categoriaService.listar().subscribe({
      next: (cats) => this.categorias.set(cats),
      error: (e) => console.error('Error al cargar categorías', e)
    });
  }

  /** Envía el nuevo plato al backend (requiere sesión ADMIN). */
  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    this.productoService.crear({
      idProducto: v.idProducto!,
      precio: Number(v.precio),
      resena: v.resena || undefined,
      cantidad: 1, // Por defecto para productos del menú
      categoria: v.categoriaId ? { id: Number(v.categoriaId) } : undefined
    }).subscribe({
      next: () => {
        this.mensaje.set({ tipo: 'ok', texto: 'El plato ha sido guardado en la carta exitosamente.' });
        this.form.reset({ precio: 0.01 });
      },
      error: (e) => {
        this.mensaje.set({ tipo: 'error', texto: mensajeErrorApi(e, 'Error al guardar el plato') });
      }
    });
  }
}
