# Menú dinámico jerárquico — Guía completa

Sistema de menú lateral con **profundidad infinita**, datos en **PostgreSQL**, API **Spring Boot** y UI **Angular**.

---

## Arquitectura

```
Angular (pedidos-frontend)
    │  HTTP GET /api/opciones
    │  HTTP GET /api/opciones/arbol
    ▼
Spring Boot (puerto 8080)
    │  JPA / Hibernate
    ▼
PostgreSQL — tabla `opciones`
    │
    └── pgAdmin (administración visual)
```

---

## Estructura de carpetas

### Backend (`src/main/java/co/ucc/pedidos/`)

| Carpeta | Archivos |
|---------|----------|
| `model/` | `OpcionModel.java` — entidad JPA |
| `dto/` | `OpcionDto.java`, `OpcionArbolDto.java` |
| `repository/` | `OpcionRepository.java` |
| `service/` | `OpcionService.java` — arma el árbol |
| `controller/` | `OpcionController.java` — REST |
| `config/` | `WebConfig.java` (CORS), `OpcionDataInitializer.java` |

### SQL

- `sql/opciones.sql` — CREATE TABLE, FK recursiva, INSERTs de ejemplo

### Frontend (`pedidos-frontend/src/app/`)

| Carpeta | Rol |
|---------|-----|
| `core/models/` | Interfaces TypeScript tipadas |
| `core/services/` | `MenuService` — consume la API |
| `core/utils/` | `menu-tree.builder.ts` — árbol en cliente |
| `layout/sidebar/` | Sidebar colapsable |
| `layout/sidebar/menu-item/` | **Componente recursivo** |
| `layout/main-layout/` | Shell con sidebar + contenido |
| `pages/` | Vistas placeholder por ruta |

---

## Base de datos

### Tabla `opciones`

| Columna | Tipo | Descripción |
|---------|------|-------------|
| `id` | BIGSERIAL | PK |
| `nombre` | VARCHAR | Texto visible en el menú |
| `padre_opcion_id` | BIGINT NULL | FK recursiva → `opciones.id` |
| `ruta` | VARCHAR NULL | Ruta Angular (`/clientes/crear`) |
| `icono` | VARCHAR NULL | Nombre Material Icon (opcional) |
| `orden` | INT | Orden entre hermanos |

**Raíz:** `padre_opcion_id IS NULL` (ej. "Mi Aplicación").

### Cómo se guarda la jerarquía en BD

No hay columnas “nivel 1, nivel 2”. Solo filas relacionadas:

```
id=1  Mi Aplicación     padre=NULL
id=2  Clientes          padre=1
id=5  Crear Cliente     padre=2
```

La profundidad es **ilimitada**: cualquier fila puede ser padre de otra.

### Script en pgAdmin

1. Conéctate a tu base PostgreSQL (la misma de `application.properties`).
2. Abre `sql/opciones.sql` y ejecuta el script.
3. Si la tabla ya existe con datos del inicializador Java, los INSERT pueden chocar — usa solo uno de los dos métodos de carga inicial.

---

## Backend — Paso a paso

### 1. Entidad (`OpcionModel`)

Mapea la tabla. `padre_opcion_id` es la FK lógica; `@ManyToOne` / `@OneToMany` documentan la relación recursiva en JPA.

### 2. Repository

`findAllByOrderByOrdenAscIdAsc()` devuelve **todas** las filas en lista plana.

### 3. Servicio — construir el árbol (recursividad conceptual)

```text
1. Crear Map<id, OpcionArbolDto> con un nodo por fila
2. Para cada fila:
   - Si padreOpcionId es null → agregar a lista "raíces"
   - Si no → agregar el nodo a hijos[] del padre en el Map
3. Ordenar raíces e hijos por "orden" e "id"
```

No hace falta llamar funciones recursivas en Java: con **dos pasadas** sobre N filas se arma cualquier profundidad.

Método: `OpcionService.construirArbol()`.

### 4. Endpoints

| Método | URL | Respuesta |
|--------|-----|-----------|
| GET | `/api/opciones` | Lista plana `[{ id, nombre, padreOpcionId, ruta, ... }]` |
| GET | `/api/opciones/arbol` | Árbol `[{ id, nombre, hijos: [...] }]` |

### 5. Datos iniciales

`OpcionDataInitializer` inserta el menú de ejemplo si la tabla está vacía al arrancar (con `ddl-auto=update`).

---

## Frontend Angular — Paso a paso

### 1. `MenuService`

- `obtenerPlanas()` → lista desde API.
- `obtenerArbol()` → árbol ya hecho en backend.
- `obtenerMenuConstruidoEnCliente()` → planas + `buildMenuTree()` (demuestra recursividad también en Angular).

### 2. `buildMenuTree()` (misma idea que el backend)

Archivo: `core/utils/menu-tree.builder.ts`.

### 3. Recursividad en la **vista**

`MenuItemComponent` se importa a sí mismo:

```typescript
imports: [..., MenuItemComponent]
```

En la plantilla, por cada hijo:

```html
@for (hijo of opcion.hijos; track hijo.id) {
  <app-menu-item [opcion]="hijo" [depth]="depth + 1" />
}
```

Cada instancia renderiza un nivel; si un hijo tiene más hijos, vuelve a renderizar `<app-menu-item>`. Así la profundidad es **infinita** en UI.

### 4. Expandir / contraer

- Propiedad `opcion.expanded` en el nodo.
- Clic en ítem sin ruta (solo contenedor) → `alternar()`.
- Los hijos se muestran con `@if (opcion.expanded)`.

### 5. Navegación

- Si `ruta` tiene valor → `routerLink` + `routerLinkActive`.
- Rutas definidas en `app.routes.ts` (placeholders listos para tus pantallas reales).

### 6. Sidebar colapsable

`SidebarComponent` alterna ancho; en modo colapsado solo se ven iconos.

---

## Pantallas con datos reales (CRUD)

El menú solo define **navegación**. Las pantallas de contenido llaman a la API:

| Ruta menú | API usada |
|-----------|-----------|
| Inicio | Resumen: clientes + productos + pedidos |
| Clientes | `GET/POST/PUT/DELETE /clientes` |
| Crear/Editar cliente | Formularios → POST o PUT |
| Productos | `GET/POST /productos` |
| Categorías / Inventario | `GET/POST /categorias`, `/inventarios` |
| Pedidos / Historial | `GET/DELETE /api/pedidos` |
| Crear pedido | `POST /api/pedidos` (cliente y producto deben existir) |
| Reportes | Estadísticas desde lista de pedidos |

**Orden recomendado para probar:** 1) Crear cliente → 2) Crear producto → 3) Crear pedido → 4) Ver listados y reportes.

## Cómo ejecutar

### Backend

```bash
./mvnw spring-boot:run
```

Verificar:

- http://localhost:8080/api/opciones
- http://localhost:8080/api/opciones/arbol

### Frontend

```bash
cd pedidos-frontend
npm install
npm start
```

Abrir: http://localhost:4200

---

## Buenas prácticas aplicadas

- **Separación de capas:** controller → service → repository → entity.
- **DTOs** para no exponer entidades JPA directamente.
- **Tipado fuerte** en TypeScript (`OpcionPlana`, `OpcionNodo`).
- **Componente recursivo** reutilizable con `@Input`.
- **CORS** configurado para desarrollo local.
- **PostgreSQL** con `ddl-auto=update` (no borra datos al reiniciar).
- **Escalable:** nuevas opciones = INSERT en BD, sin redeploy de menú hardcodeado.

---

## Flujo resumido

1. PostgreSQL almacena filas con `padre_opcion_id`.
2. API devuelve lista plana o árbol JSON.
3. Angular (opcional) vuelve a armar el árbol con `buildMenuTree`.
4. `MenuItemComponent` pinta cada nivel y se llama a sí mismo por cada hijo.
5. El usuario expande nodos y navega con el Router.
