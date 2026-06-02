package co.ucc.pedidos.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.ucc.pedidos.model.OpcionModel;
import co.ucc.pedidos.repository.OpcionRepository;

/**
 * Carga datos de menú si la tabla está vacía (útil cuando no se ejecutó el SQL manual).
 */
@Configuration
public class OpcionDataInitializer {

    @Bean
    CommandLineRunner initOpciones(OpcionRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            Long raiz = crearOpcion(repository, "Restaurante Gourmet", null, null, "restaurant", 1);
            Long menu = crearOpcion(repository, "Carta Menú", raiz, "/productos", "menu_book", 1);
            Long pedidos = crearOpcion(repository, "Mis Pedidos", raiz, "/pedidos", "shopping_basket", 2);
            Long admin = crearOpcion(repository, "Administración", raiz, null, "admin_panel_settings", 3);

            crearOpcion(repository, "Gestión Platos", admin, "/productos/crear", "restaurant_menu", 1);
            crearOpcion(repository, "Gestión Categorías", admin, "/productos/categorias", "category", 2);
            crearOpcion(repository, "Gestión Clientes", admin, "/clientes", "people", 3);
            crearOpcion(repository, "Control Pedidos", admin, "/pedidos/admin", "receipt_long", 4);
            crearOpcion(repository, "Dashboard", admin, "/dashboard", "dashboard", 5);
        };
    }

    private Long crearOpcion(OpcionRepository repo, String nombre, Long padreId, String ruta, String icono, int orden) {
        OpcionModel o = new OpcionModel();
        o.setNombre(nombre);
        o.setPadreOpcionId(padreId);
        o.setRuta(ruta);
        o.setIcono(icono);
        o.setOrden(orden);
        return repo.save(o).getId();
    }
}
