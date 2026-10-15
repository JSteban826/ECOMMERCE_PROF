package com.ecommerce.auth.Domain.exception;

public class ErrorAplicacion extends RuntimeException {
    public enum Tipo { FORMATO, VALIDACION, NO_ENCONTRADO, CONFLICTO,
        NO_DISPONIBLE, TIEMPO_ESPERA, RESPUESTA_INVALIDA, INTERNO }
    private final Tipo tipo;
    private final String codigo;
    public ErrorAplicacion(Tipo tipo, String codigo, String mensaje) {
        super(mensaje);
        this.tipo = tipo;
        this.codigo = codigo;
    }
    public ErrorAplicacion(Tipo tipo, String codigo, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.tipo = tipo;
        this.codigo = codigo;
    }
    public Tipo getTipo() { return tipo; }
    public String getCodigo() { return codigo; }
}
