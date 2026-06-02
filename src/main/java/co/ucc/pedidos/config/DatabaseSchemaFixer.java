package co.ucc.pedidos.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Al arrancar, aplica SQL idempotente para alinear el esquema de PostgreSQL
 * con el modelo de restaurante (columnas legadas del sistema de pedidos antiguo).
 */
@Configuration
public class DatabaseSchemaFixer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSchemaFixer.class);

    @Bean
    DataSourceInitializer schemaFixInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(connection -> {
            JdbcTemplate jdbc = new JdbcTemplate(dataSource);
            for (String sql : statements()) {
                try {
                    jdbc.execute(sql);
                } catch (Exception ex) {
                    log.debug("SQL omitido (ya aplicado o no aplica): {} - {}", sql, ex.getMessage());
                }
            }
            log.info("Esquema de restaurante verificado");
        });
        return initializer;
    }

    private static String[] statements() {
        return new String[] {
            "ALTER TABLE IF EXISTS producto DROP CONSTRAINT IF EXISTS fk9nyueixdsgbycfhf7allg8su",
            "ALTER TABLE IF EXISTS producto DROP CONSTRAINT IF EXISTS fk_producto_categoria",
            "ALTER TABLE IF EXISTS producto DROP COLUMN IF EXISTS id_categoria",
            "ALTER TABLE IF EXISTS categoria DROP COLUMN IF EXISTS id_categoria",
            "ALTER TABLE IF EXISTS producto ADD COLUMN IF NOT EXISTS categoria_id INTEGER",
            "ALTER TABLE IF EXISTS producto ADD COLUMN IF NOT EXISTS imagen_url VARCHAR(1000)",
            "ALTER TABLE IF EXISTS producto ADD COLUMN IF NOT EXISTS disponible BOOLEAN DEFAULT TRUE",
            """
            DO $$ BEGIN
              IF NOT EXISTS (
                SELECT 1 FROM information_schema.table_constraints
                WHERE table_name = 'producto' AND constraint_name = 'fk_producto_categoria'
              ) THEN
                ALTER TABLE producto ADD CONSTRAINT fk_producto_categoria
                  FOREIGN KEY (categoria_id) REFERENCES categoria(id);
              END IF;
            END $$
            """,
            """
            DO $$ BEGIN
              IF EXISTS (
                SELECT 1 FROM information_schema.columns
                WHERE table_name = 'pedido' AND column_name = 'precio'
              ) AND NOT EXISTS (
                SELECT 1 FROM information_schema.columns
                WHERE table_name = 'pedido' AND column_name = 'total'
              ) THEN
                ALTER TABLE pedido RENAME COLUMN precio TO total;
              END IF;
            END $$
            """,
            "ALTER TABLE IF EXISTS pedido ADD COLUMN IF NOT EXISTS observaciones VARCHAR(500)",
            "ALTER TABLE IF EXISTS estado DROP COLUMN IF EXISTS creado",
            "ALTER TABLE IF EXISTS estado DROP COLUMN IF EXISTS enviado",
            "ALTER TABLE IF EXISTS estado DROP COLUMN IF EXISTS entregado",
            "ALTER TABLE IF EXISTS estado DROP COLUMN IF EXISTS cancelado",
            "ALTER TABLE IF EXISTS estado ADD COLUMN IF NOT EXISTS nombre_estado VARCHAR(50) DEFAULT 'PENDIENTE'",
            """
            CREATE TABLE IF NOT EXISTS categoria (
              id SERIAL PRIMARY KEY,
              nombre VARCHAR(100) NOT NULL,
              descripcion VARCHAR(255)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS detalle_pedido (
              id SERIAL PRIMARY KEY,
              cantidad INTEGER NOT NULL,
              precio_unitario DOUBLE PRECISION NOT NULL,
              id_pedido VARCHAR(255) NOT NULL REFERENCES pedido(id_pedido) ON DELETE CASCADE,
              id_producto VARCHAR(255) NOT NULL REFERENCES producto(id_producto)
            )
            """
        };
    }
}
