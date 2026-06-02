-- SCRIPT DE MIGRACIÓN: Transformación a Gestión de Restaurante
-- Ejecutar en pgAdmin o la consola de Render

-- 1. Crear tabla de Categorías (si no existe)
CREATE TABLE IF NOT EXISTS categoria (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

-- 2. Limpiar datos de pedidos previos para evitar conflictos de integridad con la nueva estructura
-- ADVERTENCIA: Esto borrará el historial viejo. 
DELETE FROM pedido;
DELETE FROM estado;

-- 3. Modificar tabla de Estados para la nueva lógica de restaurante
ALTER TABLE estado DROP COLUMN IF EXISTS creado;
ALTER TABLE estado DROP COLUMN IF EXISTS enviado;
ALTER TABLE estado DROP COLUMN IF EXISTS entregado;
ALTER TABLE estado DROP COLUMN IF EXISTS cancelado;
ALTER TABLE estado ADD COLUMN IF NOT EXISTS nombre_estado VARCHAR(50) DEFAULT 'PENDIENTE';

-- 4. Modificar tabla de Productos (Platos)
ALTER TABLE producto DROP COLUMN IF EXISTS id_categoria;
ALTER TABLE producto ADD COLUMN IF NOT EXISTS imagen_url VARCHAR(1000);
ALTER TABLE producto ADD COLUMN IF NOT EXISTS disponible BOOLEAN DEFAULT TRUE;
ALTER TABLE producto ADD COLUMN IF NOT EXISTS categoria_id INTEGER REFERENCES categoria(id);

-- 5. Modificar tabla de Pedidos
ALTER TABLE pedido DROP COLUMN IF EXISTS id_producto;
ALTER TABLE pedido DROP COLUMN IF EXISTS categoria;
ALTER TABLE pedido RENAME COLUMN precio TO total;
ALTER TABLE pedido ADD COLUMN IF NOT EXISTS observaciones VARCHAR(500);

-- 6. Crear tabla DetallePedido para el Carrito de Compras
CREATE TABLE IF NOT EXISTS detalle_pedido (
    id SERIAL PRIMARY KEY,
    cantidad INTEGER NOT NULL,
    precio_unitario DOUBLE PRECISION NOT NULL,
    id_pedido VARCHAR(255) NOT NULL REFERENCES pedido(id_pedido) ON DELETE CASCADE,
    id_producto VARCHAR(255) NOT NULL REFERENCES producto(id_producto)
);

-- 7. Insertar Categorías Iniciales
INSERT INTO categoria (nombre, descripcion) VALUES 
('Entradas', 'Platos ligeros para empezar'),
('Platos Fuertes', 'Especialidades de la casa'),
('Postres', 'Dulces y delicias'),
('Bebidas', 'Refrescos y jugos naturales')
ON CONFLICT DO NOTHING;

-- 8. Limpiar opciones de menú para que el Initializer las cree de nuevo con la estructura de restaurante
DELETE FROM opciones;
