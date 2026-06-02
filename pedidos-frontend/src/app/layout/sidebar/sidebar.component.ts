import { Component, OnInit, inject, signal } from '@angular/core';
import { MenuItemComponent } from './menu-item/menu-item.component';
import { MenuService } from '../../core/services/menu.service';
import { AuthService } from '../../core/services/auth.service';
import { OpcionNodo } from '../../core/models/opcion.model';
import { filtrarMenuPorRol } from '../../core/utils/menu-role.filter';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [MenuItemComponent],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
/** Carga el menú desde la API, lo convierte en árbol y filtra por rol del usuario. */
export class SidebarComponent implements OnInit {
  private readonly menuService = inject(MenuService);
  private readonly auth = inject(AuthService);

  menu = signal<OpcionNodo[]>([]);
  collapsed = signal(false);
  cargando = signal(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.menuService.obtenerMenuConstruidoEnCliente().subscribe({
      next: (arbol) => {
        const filtrado = filtrarMenuPorRol(arbol, this.auth.getRol());
        filtrado.forEach((raiz) => (raiz.expanded = true));
        this.menu.set(filtrado);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar el menú. Verifica que el backend esté en http://localhost:8080');
        this.cargando.set(false);
      }
    });
  }

  alternarSidebar(): void {
    this.collapsed.update((v) => !v);
  }
}
