# Despliegue en Render

Este repositorio está preparado para desplegarse en [Render](https://render.com) como un Web Service (Spring Boot) con una base de datos PostgreSQL administrada.

## Despliegue rápido (Infrastructure as Code)

El archivo [`render.yaml`](./render.yaml) define toda la infraestructura (base de datos + servicio web). Render la aprovisiona automáticamente al importarla.

### Opción A — Desde el dashboard de Render (recomendado)

1. Sube este repositorio a GitHub/GitLab (sin los archivos sensibles que ya están en `.gitignore`).
2. Entra a <https://dashboard.render.com/select-repo?type=iac> → **New Blueprint Instance**.
3. Selecciona el repo.
4. Render detectará `render.yaml` y creará:
   - `pedidos-postgres` — base de datos PostgreSQL (plan Free)
   - `sistema-pedidos-api` — Web Service Java (plan Free)
5. Pulsa **Apply**. En pocos minutos tendrás ambos servicios en línea.

### Opción B — Conectar repo manualmente

Si prefieres no usar Blueprint, en <https://dashboard.render.com>:

1. **New + → PostgreSQL**
   - Name: `pedidos-postgres`
   - Plan: Free
   - Region: Oregon
2. **New + → Web Service**
   - Conecta este repo
   - Runtime: **Java**
   - Build Command: `./mvnw -DskipTests clean package`
   - Start Command: `java -jar target/pedidos-0.0.1-SNAPSHOT.jar`
   - Plan: Free
3. En la pestaña **Environment** del Web Service, agrega estas variables (Render las autocompleta si la BD está en el mismo account):

   | Variable | Valor |
   |----------|-------|
   | `SPRING_PROFILES_ACTIVE` | `prod` |
   | `SPRING_DATASOURCE_URL` | *(de la sección "Internal Connection String" de la BD, formato `postgresql://...`)* |
   | `SPRING_DATASOURCE_USERNAME` | *(de la BD)* |
   | `SPRING_DATASOURCE_PASSWORD` | *(de la BD)* |
   | `JWT_SECRET` | *(genera uno largo y aleatorio)* |
   | `CORS_ALLOWED_ORIGINS` | `https://tu-frontend.onrender.com` |
   | `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` |
   | `SPRING_JPA_SHOW_SQL` | `false` |

4. Pulsa **Save Changes** y espera el deploy.

## Variables de entorno

| Variable | Descripción | Default local |
|----------|-------------|---------------|
| `PORT` | Puerto HTTP (lo asigna Render) | `8080` |
| `SPRING_PROFILES_ACTIVE` | Perfil de Spring | `dev` |
| `SPRING_DATASOURCE_URL` | URL JDBC o `postgresql://` (se le antepone `jdbc:` automáticamente) | `jdbc:postgresql://localhost:5432/pedidos` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la BD | `pedidos` |
| `SPRING_DATASOURCE_PASSWORD` | Password de la BD | `pedidos` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Estrategia DDL de Hibernate (`update`, `validate`, `none`) | `update` |
| `SPRING_JPA_SHOW_SQL` | Imprime SQL en logs | `false` |
| `CORS_ALLOWED_ORIGINS` | Orígenes CORS separados por coma | `http://localhost:4200,http://127.0.0.1:4200` |
| `JWT_SECRET` | Clave para firmar JWT (mín. 32 caracteres) | valor dev solo |

## Inicialización de la base de datos

Al primer arranque, la aplicación crea y/o migra el esquema automáticamente:

- `spring.jpa.hibernate.ddl-auto=update` → crea las tablas de las entidades JPA.
- `DatabaseSchemaFixer` (`src/main/java/co/ucc/pedidos/config/DatabaseSchemaFixer.java`) aplica SQL idempotente para alinear columnas legadas.
- `UsuarioDataInitializer` crea el usuario admin (`admin@restaurante.com` / `admin123`) si no existe.
- `OpcionDataInitializer` inserta el menú lateral por defecto si la tabla `opciones` está vacía.

> Si vienes de un esquema anterior, ejecuta el script [`migracion_restaurante.sql`](./migracion_restaurante.sql) desde la consola de Render o con `psql` antes del primer arranque.

## Verificar el despliegue

Una vez en línea, prueba:

```
GET https://sistema-pedidos-api.onrender.com/
GET https://sistema-pedidos-api.onrender.com/api/opciones
POST https://sistema-pedidos-api.onrender.com/api/auth/login
```

```json
{
  "email": "admin@restaurante.com",
  "password": "admin123"
}
```

## Conexión a la BD desde tu máquina

Desde el dashboard de la BD en Render → **External Connection** (psql):

```bash
psql "postgresql://pedidos:XXXXX@dpg-XXXXX-a.oregon-postgres.render.com/pedidos"
```

## Notas

- Plan Free de Render **duerme el servicio tras 15 min de inactividad**. La primera petición puede tardar ~30 s.
- Los datos en el plan Free de PostgreSQL se conservan 90 días desde la creación. Para producción real, usa plan Starter o superior.
- `system.properties` fija Java 17 (debe coincidir con `<java.version>17</java.version>` en `pom.xml`).
