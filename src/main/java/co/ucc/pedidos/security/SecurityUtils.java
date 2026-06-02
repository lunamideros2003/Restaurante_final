package co.ucc.pedidos.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import co.ucc.pedidos.model.Rol;
import co.ucc.pedidos.model.UsuarioModel;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static UsuarioModel getUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UsuarioPrincipal principal) {
            return principal.getUsuario();
        }
        return null;
    }

    public static boolean esAdmin() {
        UsuarioModel usuario = getUsuarioActual();
        return usuario != null && usuario.getRol() == Rol.ADMIN;
    }

    public static boolean esCliente() {
        UsuarioModel usuario = getUsuarioActual();
        return usuario != null && usuario.getRol() == Rol.CLIENTE;
    }

    public static String getIdClienteActual() {
        UsuarioModel usuario = getUsuarioActual();
        return usuario != null ? usuario.getIdCliente() : null;
    }
}
