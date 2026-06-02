package co.ucc.pedidos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ucc.pedidos.dto.OpcionArbolDto;
import co.ucc.pedidos.dto.OpcionDto;
import co.ucc.pedidos.service.OpcionService;

@RestController
@RequestMapping("/api/opciones")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
public class OpcionController {

    @Autowired
    private OpcionService opcionService;

    /**
     * Lista plana: ideal para que Angular construya el árbol en el cliente.
     */
    @GetMapping
    public ResponseEntity<List<OpcionDto>> listarTodas() {
        return ResponseEntity.ok(opcionService.listarTodasPlanas());
    }

    /**
     * Menú ya armado en formato árbol JSON con hijos anidados.
     */
    @GetMapping("/arbol")
    public ResponseEntity<List<OpcionArbolDto>> obtenerArbol() {
        return ResponseEntity.ok(opcionService.obtenerMenuArbol());
    }
}
