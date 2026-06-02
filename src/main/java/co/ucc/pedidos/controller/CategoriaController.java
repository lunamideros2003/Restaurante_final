package co.ucc.pedidos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import co.ucc.pedidos.model.CategoriaModel;
import co.ucc.pedidos.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<CategoriaModel> listarCategorias() {
        return categoriaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<CategoriaModel> registrarCategoria(@RequestBody CategoriaModel categoria) {
        CategoriaModel categoriaRegistrada = categoriaRepository.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRegistrada);
    }
}
