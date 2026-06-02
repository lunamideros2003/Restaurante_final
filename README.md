# Restaurante Gourmet — Spring Boot + Angular

Sistema web para gestión de restaurante: carta de platos, categorías, pedidos, clientes y pagos.

## Requisitos

- Java 17+
- Maven 3.6+
- Node.js 18+ (frontend)

## Ejecución

**Backend** (puerto 8080):

```powershell
.\start-backend.ps1
```

**Frontend** (puerto 4200):

```powershell
cd pedidos-frontend
.\start-frontend.ps1
```

## Credenciales admin (desarrollo)

| Email | Contraseña |
|-------|------------|
| `admin@restaurante.com` | `admin123` |

## Endpoints principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/login` | Iniciar sesión (JWT) |
| POST | `/api/auth/register` | Registro cliente |
| GET | `/productos` | Carta de platos |
| POST | `/productos` | Crear plato (ADMIN) |
| GET | `/categorias` | Listar categorías |
| POST | `/api/pedidos` | Crear pedido |
| GET | `/api/pedidos/mios` | Mis pedidos (CLIENTE) |
| GET | `/api/opciones` | Menú lateral dinámico |

## Tests

```bash
./mvnw test
```

## Menú dinámico

Ver [docs/MENU-DINAMICO.md](docs/MENU-DINAMICO.md).

## Estructura

```
src/main/java/co/ucc/pedidos/   # Backend Spring Boot
pedidos-frontend/               # Angular 19
migracion_restaurante.sql         # Script SQL opcional
```
