# Guía completa: cómo usar el sistema paso a paso

## ¿Cómo guardo un cliente?

Tienes **dos formas** (ambas guardan en la misma base PostgreSQL):

### Opción A — Desde el frontend (recomendado)

1. Menú → **Clientes** → **Crear Cliente**
2. Llena el formulario (ID, nombre, correo obligatorios)
3. Elige **Género** en el desplegable (femenino / masculino)
4. Pulsa el botón **Guardar**
5. Si todo va bien verás un mensaje verde: *"Cliente creado en la base de datos"*
6. Ve a **Clientes** (listado) para ver la tabla con tu registro

### Opción B — Con Postman

```
POST http://localhost:8080/clientes
Content-Type: application/json
```

```json
{
  "idCliente": "CLI001",
  "nombre": "Luna",
  "correo": "lgmbajvm@gmail.com",
  "genero": "femenino",
  "direccion": "calle 7"
}
```

Respuesta **201 Created** = guardado en PostgreSQL.

---

## Cómo ver los datos en pgAdmin (tu segunda imagen)

1. Abre **pgAdmin** y conéctate al servidor **luna** → base **luni**
2. En el árbol: **Schemas → public → Tables**
3. Clic derecho en la tabla **`cliente`** → **View/Edit Data → All Rows**  
   O abre **Query Tool** y ejecuta:

```sql
SELECT * FROM cliente;
```

4. Para productos y pedidos:

```sql
SELECT * FROM producto;
SELECT * FROM pedido;
SELECT * FROM opciones;
```

5. **Importante:** después de escribir el SQL, pulsa el botón **▶ Execute** (o F5).  
   Si no ejecutas, verás *"No data output"* aunque el SQL esté escrito.

En tu captura tenías `select * from producto` pero sin ejecutar — por eso no salían filas.

---

## Todos los endpoints (GET y POST)

Base: `http://localhost:8080`

### Menú dinámico
| Método | URL | Uso |
|--------|-----|-----|
| GET | `/api/opciones` | Lista plana del menú |
| GET | `/api/opciones/arbol` | Menú en árbol JSON |

### Clientes
| Método | URL | Uso |
|--------|-----|-----|
| GET | `/clientes` | Listar todos |
| GET | `/clientes/{idCliente}` | Un cliente |
| POST | `/clientes` | **Crear** |
| PUT | `/clientes/{idCliente}` | Actualizar |
| DELETE | `/clientes/{idCliente}` | Eliminar |

### Productos
| Método | URL | Uso |
|--------|-----|-----|
| GET | `/productos` | Listar |
| GET | `/productos/{id}` | Un producto |
| POST | `/productos` | **Crear** (precio > 0) |
| PUT | `/productos/{id}` | Actualizar |
| DELETE | `/productos/{id}` | Eliminar |

### Pedidos
| Método | URL | Uso |
|--------|-----|-----|
| GET | `/api/pedidos` | Listar |
| GET | `/api/pedidos/{id}` | Un pedido |
| POST | `/api/pedidos` | **Crear** (precio > 0) |
| DELETE | `/api/pedidos/{id}` | Eliminar |

### Categorías e inventario
| Método | URL | Uso |
|--------|-----|-----|
| GET | `/categorias` | Listar |
| POST | `/categorias` | Crear |
| GET | `/inventarios` | Listar |
| POST | `/inventarios` | Crear |

### Pagos
| Método | URL | Uso |
|--------|-----|-----|
| GET | `/pagos` | Listar |
| GET | `/pagos/{id}` | Un pago |
| POST | `/pagos` | Crear |

**No todo es POST.** Para consultar usas **GET**; para crear **POST**; para actualizar **PUT**; para borrar **DELETE**.

---

## Proceso recomendado (orden correcto)

```
┌─────────────────┐
│ 1. Backend      │  ./mvnw spring-boot:run  (puerto 8080)
└────────┬────────┘
         ▼
┌─────────────────┐
│ 2. Crear datos  │  Frontend O Postman (cliente → producto → pedido)
└────────┬────────┘
         ▼
┌─────────────────┐
│ 3. Ver en app   │  Angular: listados, reportes
└────────┬────────┘
         ▼
┌─────────────────┐
│ 4. Ver en BD    │  pgAdmin: SELECT * FROM cliente;
└─────────────────┘
```

### Ejemplo práctico completo

**Paso 1 — Arrancar backend** (terminal 1)
```bash
./mvnw spring-boot:run
```
Espera: `Started PedidosApplication`

**Paso 2 — Arrancar frontend** (terminal 2)
```bash
cd pedidos-frontend
npm start
```
Abre: http://localhost:4200

**Paso 3 — Crear cliente** (frontend)
- Crear Cliente → Guardar
- O Postman POST `/clientes` (JSON de arriba)

**Paso 4 — Crear producto**
- Productos → Crear Producto → ID `PROD001`, precio `50`, Guardar

**Paso 5 — Crear pedido**
- Pedidos → Crear Pedido → IDs del cliente y producto, precio > 0, Guardar

**Paso 6 — Verificar**
- Frontend: Clientes / Productos / Pedidos / Reportes
- pgAdmin: `SELECT * FROM cliente;` + Execute ▶

---

## Modo claro / oscuro

En la esquina superior derecha (fuera del menú lateral) hay un botón:
- **Luna** (`dark_mode`) → activa modo oscuro (azul pastel)
- **Sol** (`light_mode`) → vuelve al modo claro (morado pastel)

La preferencia se guarda en el navegador.

---

## Errores frecuentes

| Problema | Solución |
|----------|----------|
| Guardar no hace nada | Debes pulsar **Guardar**, no solo llenar campos |
| Correo duplicado | Usa otro correo o borra el cliente en pgAdmin |
| Pedido error 500 | **Corregido:** el pedido debe enlazar cliente/producto ya existentes. Crea CLI001 y PROD001 antes. Reinicia Spring Boot tras actualizar código. |
| Pedido ID duplicado | Usa otro `idPedido` (ej. PED002) o elimina el anterior |
| Frontend no guarda | Reinicia backend; revisa mensaje rojo bajo el título; backend en :8080 |
| pgAdmin vacío | Ejecuta el SQL con ▶ (F5) |
| CORS / red | Backend debe estar en 8080 y reiniciado tras cambios |

## Reiniciar después de cambios en el código

```bash
# Detener Spring Boot: Ctrl+C en la terminal
# Volver a arrancar:
./mvnw spring-boot:run
```

Frontend: recarga el navegador o `npm start` de nuevo.
