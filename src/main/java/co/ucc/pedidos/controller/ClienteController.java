package co.ucc.pedidos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import co.ucc.pedidos.model.ClienteModel;
import co.ucc.pedidos.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteModel> registrarCliente(@Valid @RequestBody ClienteModel cliente) {
        ClienteModel clienteRegistrado = clienteService.registrarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteRegistrado);
    }

    @GetMapping
    public ResponseEntity<List<ClienteModel>> listarClientes() {
        List<ClienteModel> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteModel> obtenerCliente(@PathVariable String idCliente) {
        Optional<ClienteModel> cliente = clienteService.buscarPorId(idCliente);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/correo")
    public ResponseEntity<ClienteModel> obtenerPorCorreo(@RequestParam String correoElectronico) {
        Optional<ClienteModel> cliente = clienteService.buscarPorCorreo(correoElectronico);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<ClienteModel> obtenerPorNombre(@RequestParam String nombre) {
        Optional<ClienteModel> cliente = clienteService.buscarPorNombre(nombre);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<ClienteModel> actualizarCliente(
            @PathVariable String idCliente,
            @RequestBody ClienteModel cliente) {
        ClienteModel clienteActualizado = clienteService.actualizarCliente(idCliente, cliente);
        return ResponseEntity.ok(clienteActualizado);
    }

    @DeleteMapping("/{idCliente}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable String idCliente) {
        clienteService.eliminarCliente(idCliente);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/existe/id")
    public ResponseEntity<Boolean> existePorId(@RequestParam String idCliente) {
        boolean existe = clienteService.existePorId(idCliente);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/existe/correo")
    public ResponseEntity<Boolean> existePorCorreo(@RequestParam String correoElectronico) {
        boolean existe = clienteService.existePorCorreo(correoElectronico);
        return ResponseEntity.ok(existe);
    }
}
