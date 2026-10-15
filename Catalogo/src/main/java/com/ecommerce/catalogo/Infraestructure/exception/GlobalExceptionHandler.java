package com.ecommerce.catalogo.Infraestructure.exception;

import com.ecommerce.catalogo.Domain.exception.ErrorAplicacion;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ErrorAplicacion.class)
    public ResponseEntity<Object> dominio(ErrorAplicacion ex, WebRequest request) {
        int estado = switch(ex.getTipo()) {
            case FORMATO -> 400;
            case VALIDACION -> 422;
            case NO_ENCONTRADO -> 404;
            case CONFLICTO -> 409;
            case NO_DISPONIBLE -> 503;
            case TIEMPO_ESPERA -> 504;
            case RESPUESTA_INVALIDA -> 502;
            case INTERNO -> 500;
        };
        if (estado >= 500) LOG.error("Fallo de servidor: {}", ex.getCodigo(), ex);
        return respuesta(estado, ex.getCodigo(), ex.getMessage(), request, HttpHeaders.EMPTY);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        if (request instanceof ServletWebRequest servlet && servlet.getResponse() != null
                && servlet.getResponse().isCommitted()) return null;
        int estado = status.value();
        String codigo = switch(estado) {
            case 400 -> "SOLICITUD_INVALIDA";
            case 404 -> "RUTA_NO_ENCONTRADA";
            case 405 -> "METODO_NO_PERMITIDO";
            case 406 -> "RESPUESTA_NO_ACEPTABLE";
            case 413 -> "CONTENIDO_DEMASIADO_GRANDE";
            case 415 -> "FORMATO_NO_SOPORTADO";
            default -> estado >= 500 ? "ERROR_INTERNO" : "ERROR_HTTP";
        };
        String detalle = switch(estado) {
            case 400 -> "El cuerpo, los parámetros o sus tipos de datos no son válidos";
            case 404 -> "No se encontró la ruta solicitada";
            case 405 -> "El método HTTP no está permitido para esta ruta";
            case 406 -> "No se puede generar un formato admitido por Accept";
            case 413 -> "El contenido supera el tamaño admitido";
            case 415 -> "El formato de entrada no está admitido; estas operaciones reciben JSON";
            default -> estado >= 500 ? "Ocurrió un error interno" : "No fue posible procesar la solicitud";
        };
        if (estado >= 500) LOG.error("Fallo MVC", ex);
        return respuesta(estado, codigo, detalle, request, headers);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> inesperado(Exception ex, WebRequest request) {

        LOG.error("Error inesperado", ex);
        return respuesta(500, "ERROR_INTERNO", "Ocurrió un error interno", request, HttpHeaders.EMPTY);
    }

    private ResponseEntity<Object> respuesta(int estado, String codigo, String detalle,
            WebRequest request, HttpHeaders originales) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(estado), detalle);
        problema.setType(URI.create("about:blank"));
        problema.setProperty("code", codigo);
        if (request instanceof ServletWebRequest servlet) {
            problema.setInstance(URI.create(servlet.getRequest().getRequestURI()));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(originales);
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        headers.setCacheControl("no-store");
        return new ResponseEntity<>(problema, headers, HttpStatusCode.valueOf(estado));
    }
}
