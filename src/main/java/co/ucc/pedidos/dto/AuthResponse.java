package co.ucc.pedidos.dto;

import co.ucc.pedidos.model.Rol;

public class AuthResponse {

    private String token;
    private String email;
    private String nombre;
    private Rol rol;
    private String idCliente;

    public AuthResponse() {}

    public AuthResponse(String token, String email, String nombre, Rol rol, String idCliente) {
        this.token = token;
        this.email = email;
        this.nombre = nombre;
        this.rol = rol;
        this.idCliente = idCliente;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
}
