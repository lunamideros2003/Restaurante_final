package co.ucc.pedidos.exception;

public class PagoNoEncontradoException extends RuntimeException {
    public PagoNoEncontradoException(String message) {
        super(message);
    }
}