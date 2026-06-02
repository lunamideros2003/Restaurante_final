import { Component, Input } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { OpcionNodo } from '../../../core/models/opcion.model';

/**
 * COMPONENTE RECURSIVO: Representa un elemento individual del menú.
 * Si el elemento tiene hijos, este componente se renderiza a sí mismo 
 * de forma recursiva en el HTML para crear subniveles.
 */
@Component({
  selector: 'app-menu-item',
  standalone: true,
  // Se importa a sí mismo (MenuItemComponent) para permitir la recursividad en el template
  imports: [RouterLink, RouterLinkActive, MenuItemComponent],
  templateUrl: './menu-item.component.html',
  styleUrl: './menu-item.component.scss'
})
export class MenuItemComponent {
  // Recibe la opción actual del menú (incluyendo sus posibles hijos)
  @Input({ required: true }) opcion!: OpcionNodo;
  
  // Determina si el sidebar está colapsado (solo iconos)
  @Input() collapsed = false;
  
  // Controla el nivel de profundidad para el espaciado (padding) visual
  @Input() depth = 0;

  // Verifica si este nodo tiene sub-opciones
  get tieneHijos(): boolean {
    return this.opcion.hijos?.length > 0;
  }

  // Verifica si este nodo tiene una ruta asignada para navegar
  get tieneRuta(): boolean {
    return !!this.opcion.ruta?.trim();
  }

  // Cambia el estado de expansión (abierto/cerrado) de los hijos
  alternar(): void {
    if (this.tieneHijos) {
      this.opcion.expanded = !this.opcion.expanded;
    }
  }

  // Maneja el clic en el elemento
  alClic(): void {
    // Si es un contenedor con hijos pero sin ruta propia, expande/contrae al hacer clic
    if (this.tieneHijos && !this.tieneRuta) {
      this.alternar();
    }
  }
}
