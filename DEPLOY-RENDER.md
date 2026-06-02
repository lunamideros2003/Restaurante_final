# Despliegue en Render con Docker

Este repositorio se compila dentro de un contenedor Docker (ver [`Dockerfile`](./Dockerfile)) y se sirve como Web Service Java con PostgreSQL administrado.

## Despliegue con Blueprint (recomendado)

[`render.yaml`](./render.yaml) define toda la infraestructura.

1. Sube el repo a GitHub/GitLab.
2. <https://dashboard.render.com/select-repo?type=iac> → **New Blueprint Instance**.
3. Selecciona el repo → **Apply**.
4. Render crea automáticamente:
   - `pedidos-postgres` (PostgreSQL Free)
   - `sistema-pedidos-api` (Web Service con Docker)

## Variables de entorno

### Si usas Blueprint

Las variables se configuran solas desde `render.yaml`. **No tienes que escribir nada en la UI**.

### Si creas el Web Service a mano

Render → tu servicio → **Environment** → **Add Environment Variable**:

| Key | Value | Notas |
|-----|-------|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` | Activa perfil de producción |
| `SPRING_DATASOURCE_URL` | *(Connection String de la BD)* | Pégalo tal cual de Render PostgreSQL → Connection Details → Internal |
| `SPRING_DATASOURCE_USERNAME` | `pedidos` | Usuario de la BD |
| `SPRING_DATASOURCE_PASSWORD` | *(password de la BD)* | La que muestra Render |
| `JWT_SECRET` | *(cadena aleatoria 64+ chars)* | Ej: `openssl rand -base64 64` |
| `CORS_ALLOWED_ORIGINS` | `https://tu-frontend.com` | Orígenes separados por coma |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Crea/migra tablas al arrancar |
| `SPRING_JPA_SHOW_SQL` | `false` | Silencia SQL en logs |

> **Tip**: Si en el mismo dashboard creas primero la BD PostgreSQL, Render autocompleta `SPRING_DATASOURCE_*` al vincularla.

## Orden de despliegue

1. **Sube el repo a GitHub.**
2. **Crea la base de datos** (Dashboard → New + → PostgreSQL).
3. **Crea el Web Service** (Dashboard → New + → Web Service → conecta repo → Runtime: Docker).
4. **Configura env vars** (ver tabla arriba).
5. **Espera el deploy** y copia la URL del backend: `https://sistema-pedidos-api.onrender.com`.
6. **Edita `pedidos-frontend/src/environments/environment.prod.ts`:**
   ```ts
   export const environment = {
     production: true,
     apiUrl: 'https://sistema-pedidos-api.onrender.com/api',
     apiBaseUrl: 'https://sistema-pedidos-api.onrender.com'
   };
   ```
7. **Despliega el frontend** y copia su URL.
8. **Vuelve al backend en Render** y actualiza `CORS_ALLOWED_ORIGINS` con la URL del frontend.

## Verificación

```bash
curl https://sistema-pedidos-api.onrender.com/api/opciones
```

Login:
```bash
curl -X POST https://sistema-pedidos-api.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@restaurante.com","password":"admin123"}'
```

## Build local (opcional)

```bash
docker build -t pedidos-api .
docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/pedidos \
  pedidos-api
```

## Notas

- Render asigna `PORT=10000` en Docker; Spring Boot lo lee automáticamente (`server.port=${PORT:8080}`).
- Plan Free duerme el servicio tras 15 min sin uso (primer request tarda ~30 s).
- El Dockerfile usa multi-stage con `eclipse-temurin:17-jre-alpine` → imagen final ~250 MB.
- `Dockerfile` corre como usuario no-root por seguridad.
