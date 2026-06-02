import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { InicioComponent } from './pages/inicio/inicio.component';
import { ClientesListaComponent } from './pages/clientes/clientes-lista.component';
import { ClientesFormularioComponent } from './pages/clientes/clientes-formulario.component';
import { ClientesEliminarComponent } from './pages/clientes/clientes-eliminar.component';
import { ProductosListaComponent } from './pages/productos/productos-lista.component';
import { ProductosFormularioComponent } from './pages/productos/productos-formulario.component';
import { CategoriasComponent } from './pages/productos/categorias.component';
import { PedidosListaComponent } from './pages/pedidos/pedidos-lista.component';
import { PedidosFormularioComponent } from './pages/pedidos/pedidos-formulario.component';
import { PedidosReportesComponent } from './pages/pedidos/pedidos-reportes.component';
import { LoginComponent } from './pages/auth/login.component';
import { RegisterComponent } from './pages/auth/register.component';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';
import { guestGuard } from './core/guards/guest.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [guestGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [guestGuard] },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'inicio', pathMatch: 'full' },
      { path: 'inicio', component: InicioComponent },
      { path: 'clientes', component: ClientesListaComponent, canActivate: [adminGuard] },
      { path: 'clientes/crear', component: ClientesFormularioComponent, canActivate: [adminGuard] },
      { path: 'clientes/editar', component: ClientesFormularioComponent, canActivate: [adminGuard], data: { modo: 'editar' } },
      { path: 'clientes/eliminar', component: ClientesEliminarComponent, canActivate: [adminGuard] },
      { path: 'productos', component: ProductosListaComponent },
      { path: 'productos/crear', component: ProductosFormularioComponent, canActivate: [adminGuard] },
      { path: 'productos/categorias', component: CategoriasComponent, canActivate: [adminGuard] },
      { path: 'pedidos', component: PedidosListaComponent },
      { path: 'pedidos/crear', component: PedidosFormularioComponent },
      { path: 'pedidos/historial', component: PedidosListaComponent, data: { modo: 'historial' } },
      { path: 'pedidos/reportes', component: PedidosReportesComponent, canActivate: [adminGuard] }
    ]
  },
  { path: '**', redirectTo: 'inicio' }
];
