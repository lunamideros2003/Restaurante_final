package co.ucc.pedidos.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.ucc.pedidos.dto.AuthResponse;
import co.ucc.pedidos.dto.LoginRequest;
import co.ucc.pedidos.dto.RegisterRequest;
import co.ucc.pedidos.exception.ResourceAlreadyExistsException;
import co.ucc.pedidos.model.ClienteModel;
import co.ucc.pedidos.model.Rol;
import co.ucc.pedidos.model.UsuarioModel;
import co.ucc.pedidos.repository.ClienteRepository;
import co.ucc.pedidos.repository.UsuarioRepository;
import co.ucc.pedidos.security.JwtService;
import co.ucc.pedidos.security.UsuarioPrincipal;

import java.util.UUID;

/**
 * Autenticación y registro: crea usuarios CLIENTE vinculados a {@link ClienteModel}
 * y emite tokens JWT para el frontend.
 */
@Service
public class AuthService {

    private static final String ADMIN_EMAIL = "admin@pedidos.com";

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /** Registra un nuevo cliente y su usuario con rol CLIENTE; valida correos duplicados. */
    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        String idCliente = resolverIdCliente(request);

        if (ADMIN_EMAIL.equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("Este correo está reservado para el administrador del sistema");
        }
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new ResourceAlreadyExistsException("Ya existe un usuario con el correo: " + email);
        }
        if (clienteRepository.existsByCorreoElectronico(email)) {
            throw new ResourceAlreadyExistsException("Ya existe un cliente con el correo: " + email);
        }
        if (clienteRepository.existsByIdCliente(idCliente)) {
            throw new ResourceAlreadyExistsException("Ya existe un cliente con id: " + idCliente + ". Intenta de nuevo.");
        }

        ClienteModel cliente = new ClienteModel(
                idCliente,
                request.getGenero(),
                request.getNombre().trim(),
                email,
                request.getDireccion() != null ? request.getDireccion().trim() : "");
        clienteRepository.save(cliente);

        UsuarioModel usuario = new UsuarioModel();
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombre().trim());
        usuario.setRol(Rol.CLIENTE);
        usuario.setIdCliente(idCliente);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        return buildAuthResponse(usuario);
    }

    /** Valida credenciales con Spring Security y devuelve JWT con rol e idCliente. */
    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword()));

        UsuarioModel usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        return buildAuthResponse(usuario);
    }

    public AuthResponse perfil(UsuarioPrincipal principal) {
        return buildAuthResponse(principal.getUsuario());
    }

    private String resolverIdCliente(RegisterRequest request) {
        if (request.getIdCliente() != null && !request.getIdCliente().isBlank()) {
            return request.getIdCliente().trim();
        }
        String generado;
        int intentos = 0;
        do {
            generado = "CLI" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
            intentos++;
        } while (clienteRepository.existsByIdCliente(generado) && intentos < 20);
        return generado;
    }

    private AuthResponse buildAuthResponse(UsuarioModel usuario) {
        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token, usuario.getEmail(), usuario.getNombre(), usuario.getRol(), usuario.getIdCliente());
    }
}
