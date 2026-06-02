package co.ucc.pedidos.service;

import co.ucc.pedidos.exception.MontoInvalidoException;
import co.ucc.pedidos.exception.PagoNoEncontradoException;
import co.ucc.pedidos.model.PagoModel;
import co.ucc.pedidos.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Registro y consulta de pagos asociados a clientes. */
@Service
public class PagoService {
    
    @Autowired
    private PagoRepository pagoRepository;

    /** Valida monto, procesa la transacción y persiste el pago. */
    @Transactional
    public Optional<PagoModel> registrarPago(PagoModel pago) {
        if (pago == null) {
            throw new MontoInvalidoException("El pago no puede ser nulo");
        }
        if (pago.getMonto() <= 0) {
            throw new MontoInvalidoException("El monto del pago debe ser mayor que 0");
        }
        procesarPago(pago);
        return Optional.of(pagoRepository.save(pago));
    }

    public List<PagoModel> listarPagos() {
        return pagoRepository.findAll();
    }

    public Optional<PagoModel> buscarPorId(String id) {
        Optional<PagoModel> pago = pagoRepository.findByIdPago(id);
        if (pago.isEmpty()) {
            throw new PagoNoEncontradoException("No se encontró el pago con id: " + id);
        }
        return pago;
    }

    public boolean validarMetodoPago(PagoModel pago) {
        return pago.getMetodoPago() != null && !pago.getMetodoPago().isEmpty();
    }

    public void procesarPago(PagoModel pago) {
        if (pago.validarTransaccion()) {
            pago.setProcesado(true);
            pago.setEstado("COMPLETADO");
        }
    }

    @Transactional
    public Optional<PagoModel> cancelarPago(String idPago) {
        Optional<PagoModel> pagoOpt = pagoRepository.findByIdPago(idPago);
        if (pagoOpt.isPresent()) {
            PagoModel pago = pagoOpt.get();
            pago.setProcesado(false);
            pago.setEstado("CANCELADO");
            return Optional.of(pagoRepository.save(pago));
        }
        return Optional.empty();
    }

    public String generarComprobante(PagoModel pago) {
        return "Comprobante - Pago: " + pago.getIdPago() + ", Monto: " + pago.getPrecio() + ", Metodo: " + pago.getMetodoPago();
    }

    public List<PagoModel> findByClienteId(String idCliente) {
        return pagoRepository.findByClienteIdCliente(idCliente);
    }
}