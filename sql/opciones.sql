-- =============================================================================
-- Script: Menú dinámico jerárquico - tabla opciones
-- Base de datos: PostgreSQL
-- Ejecutar en pgAdmin o psql conectado a tu base de datos
-- =============================================================================

-- Eliminar tabla si existe (solo en desarrollo; comentar en producción con datos)
-- DROP TABLE IF EXISTS opciones CASCADE;

CREATE TABLE IF NOT EXISTS opciones (
    id                BIGSERIAL PRIMARY KEY,
    nombre            VARCHAR(150) NOT NULL,
    padre_opcion_id   BIGINT NULL,
    ruta              VARCHAR(255) NULL,
    icono             VARCHAR(80) NULL,
    orden             INT DEFAULT 0,
    CONSTRAINT fk_opciones_padre
        FOREIGN KEY (padre_opcion_id)
        REFERENCES opciones (id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_opciones_padre ON opciones (padre_opcion_id);

-- =============================================================================
-- Datos de ejemplo (3+ niveles, estructura recursiva)
-- =============================================================================

-- Nivel 1: raíz
INSERT INTO opciones (id, nombre, padre_opcion_id, ruta, icono, orden) VALUES
(1, 'Mi Aplicación', NULL, NULL, 'apps', 1);

-- Nivel 2
INSERT INTO opciones (id, nombre, padre_opcion_id, ruta, icono, orden) VALUES
(2,  'Clientes',   1, '/clientes',   'group', 1),
(3,  'Productos',  1, '/productos',  'inventory_2', 2),
(4,  'Pedidos',    1, '/pedidos',    'shopping_cart', 3);

-- Nivel 3 - Clientes
INSERT INTO opciones (id, nombre, padre_opcion_id, ruta, icono, orden) VALUES
(5,  'Crear Cliente',    2, '/clientes/crear',    'person_add', 1),
(6,  'Editar Cliente',   2, '/clientes/editar',   'edit', 2),
(7,  'Eliminar Cliente', 2, '/clientes/eliminar', 'person_remove', 3);

-- Nivel 3 - Productos
INSERT INTO opciones (id, nombre, padre_opcion_id, ruta, icono, orden) VALUES
(8,  'Crear Producto', 3, '/productos/crear',      'add_box', 1),
(9,  'Categorías',     3, '/productos/categorias', 'category', 2),
(10, 'Inventario',     3, '/productos/inventario', 'warehouse', 3);

-- Nivel 3 - Pedidos
INSERT INTO opciones (id, nombre, padre_opcion_id, ruta, icono, orden) VALUES
(11, 'Crear Pedido', 4, '/pedidos/crear',     'add_shopping_cart', 1),
(12, 'Historial',    4, '/pedidos/historial', 'history', 2),
(13, 'Reportes',     4, '/pedidos/reportes',  'assessment', 3);

-- Ajustar secuencia después de inserts manuales con IDs fijos
SELECT setval(pg_get_serial_sequence('opciones', 'id'), (SELECT MAX(id) FROM opciones));

-- Consulta de verificación: lista plana
-- SELECT id, nombre, padre_opcion_id, ruta FROM opciones ORDER BY id;
