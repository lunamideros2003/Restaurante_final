import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CategoriaService } from '../../core/services/categoria.service';
import { Categoria } from '../../core/models/categoria.model';

@Component({
  selector: 'app-categorias',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './categorias.component.html',
  styleUrl: './categorias.component.scss'
})
export class CategoriasComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly categoriaService = inject(CategoriaService);

  categorias = signal<Categoria[]>([]);
  mensaje = signal<{ tipo: 'ok' | 'error'; texto: string } | null>(null);

  form = this.fb.group({
    nombre: ['', Validators.required],
    descripcion: ['']
  });

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.categoriaService.listar().subscribe({
      next: (data) => this.categorias.set(data),
      error: () => this.mensaje.set({ tipo: 'error', texto: 'Error al listar categorías' })
    });
  }

  guardar(): void {
    if (this.form.invalid) return;
    const v = this.form.getRawValue();
    this.categoriaService.crear({
      nombre: v.nombre!,
      descripcion: v.descripcion || undefined
    }).subscribe({
      next: () => {
        this.mensaje.set({ tipo: 'ok', texto: 'Categoría de restaurante creada exitosamente.' });
        this.form.reset();
        this.cargar();
      },
      error: (e) => this.mensaje.set({ tipo: 'error', texto: e.error?.message ?? 'Error al crear la categoría' })
    });
  }
}
