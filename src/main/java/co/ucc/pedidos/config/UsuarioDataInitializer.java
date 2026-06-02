package co.ucc.pedidos.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.ucc.pedidos.model.CategoriaModel;
import co.ucc.pedidos.model.Rol;
import co.ucc.pedidos.model.UsuarioModel;
import co.ucc.pedidos.repository.CategoriaRepository;
import co.ucc.pedidos.repository.UsuarioRepository;

/** Crea el admin por defecto y las categorías iniciales del menú si no existen. */
@Configuration
public class UsuarioDataInitializer {

    private static final String ADMIN_EMAIL = "admin@restaurante.com";

    @Bean
    CommandLineRunner initData(UsuarioRepository usuarioRepository, 
                              CategoriaRepository categoriaRepository,
                              PasswordEncoder passwordEncoder) {
        return args -> {
            // Inicializar Admin
            var admins = usuarioRepository.findAll().stream()
                    .filter(u -> u.getRol() == Rol.ADMIN)
                    .toList();

            if (admins.isEmpty()) {
                UsuarioModel admin = new UsuarioModel();
                admin.setEmail(ADMIN_EMAIL);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setNombre("Administrador");
                admin.setRol(Rol.ADMIN);
                admin.setActivo(true);
                usuarioRepository.save(admin);
            } else {
                UsuarioModel principal = admins.stream()
                        .filter(u -> ADMIN_EMAIL.equalsIgnoreCase(u.getEmail()))
                        .findFirst()
                        .orElse(admins.get(0));

                if (!ADMIN_EMAIL.equalsIgnoreCase(principal.getEmail())) {
                    principal.setEmail(ADMIN_EMAIL);
                }
                principal.setNombre("Administrador");
                principal.setRol(Rol.ADMIN);
                principal.setActivo(true);
                usuarioRepository.save(principal);
            }

            // Inicializar Categorías
            crearCategoria(categoriaRepository, "Entradas", "Platos ligeros para empezar");
            crearCategoria(categoriaRepository, "Platos Fuertes", "Especialidades de la casa");
            crearCategoria(categoriaRepository, "Postres", "Dulces y delicias");
            crearCategoria(categoriaRepository, "Bebidas", "Refrescos y jugos naturales");
        };
    }

    private void crearCategoria(CategoriaRepository repo, String nombre, String descripcion) {
        boolean existe = repo.findAll().stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(nombre));
        
        if (!existe) {
            CategoriaModel c = new CategoriaModel();
            c.setNombre(nombre);
            c.setDescripcion(descripcion);
            repo.save(c);
        }
    }
}
