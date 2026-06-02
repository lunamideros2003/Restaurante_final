import { DecimalPipe, NgIf } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ProductoService } from '../../core/services/producto.service';
import { AuthService } from '../../core/services/auth.service';
import { Producto } from '../../core/models/producto.model';

@Component({
  selector: 'app-productos-lista',
  standalone: true,
  imports: [RouterLink, DecimalPipe, NgIf],
  templateUrl: './productos-lista.component.html',
  styleUrl: './productos-lista.component.scss'
})
export class ProductosListaComponent implements OnInit {
  private readonly productoService = inject(ProductoService);
  private readonly auth = inject(AuthService);

  esAdmin = this.auth.isAdmin();
  productos = signal<Producto[]>([]);
  cargando = signal(true);
  error = signal<string | null>(null);

  // Imágenes predeterminadas de comida gourmet
  private readonly imagenesComida = [
    'https://images.unsplash.com/photo-1504674900247-0877df9cc836?q=80&w=1000&auto=format&fit=crop',
    'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?q=80&w=1000&auto=format&fit=crop',
    'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=1000&auto=format&fit=crop',
    'https://images.unsplash.com/photo-1473093226795-af9932fe5856?q=80&w=1000&auto=format&fit=crop',
    'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?q=80&w=1000&auto=format&fit=crop',
    'https://images.unsplash.com/photo-1567620905732-2d1ec7bb7445?q=80&w=1000&auto=format&fit=crop',
    'https://images.unsplash.com/photo-1482049016688-2d3e1b311543?q=80&w=1000&auto=format&fit=crop',
    'https://images.unsplash.com/photo-1484723091739-30a097e8f929?q=80&w=1000&auto=format&fit=crop'
  ];

  ngOnInit(): void {
    this.cargar();
  }

  getImagenAleatoria(id: string): string {
    // Usamos el ID para que siempre salga la misma imagen para el mismo plato
    const index = Math.abs(id.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0)) % this.imagenesComida.length;
    return this.imagenesComida[index];
  }

  cargar(): void {
    this.cargando.set(true);
    this.productoService.listar().subscribe({
      next: (data) => {
        this.productos.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('Error al cargar la carta del restaurante');
        this.cargando.set(false);
      }
    });
  }

  agregarAlCarrito(producto: Producto): void {
    // TODO: Implementar servicio de carrito
    console.log('Agregado al carrito:', producto.idProducto);
    alert(`${producto.idProducto} agregado al pedido`);
  }
}
