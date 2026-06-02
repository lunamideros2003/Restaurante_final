package co.ucc.pedidos.controller;

import co.ucc.pedidos.model.PagoModel;
import co.ucc.pedidos.service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<PagoModel> registrarPago(@RequestBody PagoModel pago) {
        return pagoService.registrarPago(pago)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping
    public List<PagoModel> listarPagos() {
        return pagoService.listarPagos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoModel> obtenerPago(@PathVariable String id) {
        return pagoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}