package com.cafetito.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        return body(HttpStatus.NOT_FOUND, "NO_ENCONTRADO", ex.getMessage());
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<Map<String, String>> handleCredencialesInvalidas(CredencialesInvalidasException ex) {
        return body(HttpStatus.UNAUTHORIZED, "CREDENCIALES_INVALIDAS", ex.getMessage());
    }

    @ExceptionHandler(UsuarioInactivoException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioInactivo(UsuarioInactivoException ex) {
        return body(HttpStatus.INTERNAL_SERVER_ERROR, "USUARIO_INACTIVO", ex.getMessage());
    }

    @ExceptionHandler(SinPrivilegiosException.class)
    public ResponseEntity<Map<String, String>> handleSinPrivilegios(SinPrivilegiosException ex) {
        return body(HttpStatus.FORBIDDEN, "SIN_PRIVILEGIOS", ex.getMessage());
    }

    @ExceptionHandler(CuiDuplicadoException.class)
    public ResponseEntity<Map<String, String>> handleCuiDuplicado(CuiDuplicadoException ex) {
        return body(HttpStatus.CONFLICT, "CUI_DUPLICADO", ex.getMessage());
    }

    @ExceptionHandler(PlacaDuplicadaException.class)
    public ResponseEntity<Map<String, String>> handlePlacaDuplicada(PlacaDuplicadaException ex) {
        return body(HttpStatus.CONFLICT, "PLACA_DUPLICADA", ex.getMessage());
    }

    @ExceptionHandler(FormatoPlacaInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleFormatoPlacaInvalido(FormatoPlacaInvalidoException ex) {
        return body(HttpStatus.BAD_REQUEST, "FORMATO_PLACA", ex.getMessage());
    }

    @ExceptionHandler(ModeloInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleModeloInvalido(ModeloInvalidoException ex) {
        return body(HttpStatus.BAD_REQUEST, "MODELO_INVALIDO", ex.getMessage());
    }

    @ExceptionHandler(TransportistaMenorDeEdadException.class)
    public ResponseEntity<Map<String, String>> handleMenorDeEdad(TransportistaMenorDeEdadException ex) {
        return body(HttpStatus.BAD_REQUEST, "MENOR_DE_EDAD", ex.getMessage());
    }

    @ExceptionHandler(LicenciaVencidaException.class)
    public ResponseEntity<Map<String, String>> handleLicenciaVencida(LicenciaVencidaException ex) {
        return body(HttpStatus.BAD_REQUEST, "LICENCIA_VENCIDA", ex.getMessage());
    }

    @ExceptionHandler(EstadoTransportistaIgualException.class)
    public ResponseEntity<Map<String, String>> handleEstadoTransportistaIgual(EstadoTransportistaIgualException ex) {
        return body(HttpStatus.BAD_REQUEST, "ESTADO_IGUAL", ex.getMessage());
    }

    @ExceptionHandler(TransportistaNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleTransportistaNoEncontrado(TransportistaNoEncontradoException ex) {
        return body(HttpStatus.NOT_FOUND, "NO_ENCONTRADO", ex.getMessage());
    }

    @ExceptionHandler(EstadoTransporteIgualException.class)
    public ResponseEntity<Map<String, String>> handleEstadoTransporteIgual(EstadoTransporteIgualException ex) {
        return body(HttpStatus.BAD_REQUEST, "ESTADO_IGUAL", ex.getMessage());
    }

    @ExceptionHandler(TransporteNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleTransporteNoEncontrado(TransporteNoEncontradoException ex) {
        return body(HttpStatus.NOT_FOUND, "NO_ENCONTRADO", ex.getMessage());
    }

    @ExceptionHandler(CambioEstadoNoPermitidoException.class)
    public ResponseEntity<Map<String, String>> handleCambioEstadoNoPermitido(CambioEstadoNoPermitidoException ex) {
        return body(HttpStatus.BAD_REQUEST, "CAMBIO_ESTADO_NO_PERMITIDO", ex.getMessage());
    }

    @ExceptionHandler(TransportistaInactivoException.class)
    public ResponseEntity<Map<String, String>> handleTransportistaInactivo(TransportistaInactivoException ex) {
        return body(HttpStatus.BAD_REQUEST, "TRANSPORTISTA_INACTIVO", ex.getMessage());
    }

    @ExceptionHandler(TransporteInactivoException.class)
    public ResponseEntity<Map<String, String>> handleTransporteInactivo(TransporteInactivoException ex) {
        return body(HttpStatus.BAD_REQUEST, "TRANSPORTE_INACTIVO", ex.getMessage());
    }

    @ExceptionHandler(CuentaNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleCuentaNoEncontrada(CuentaNoEncontradaException ex) {
        return body(HttpStatus.NOT_FOUND, "NO_ENCONTRADO", ex.getMessage());
    }

    @ExceptionHandler(CuentaEstadoNoPermiteException.class)
    public ResponseEntity<Map<String, String>> handleCuentaEstadoNoPermite(CuentaEstadoNoPermiteException ex) {
        return body(HttpStatus.BAD_REQUEST, "ESTADO_CUENTA", ex.getMessage());
    }

    @ExceptionHandler(ParcialidadNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleParcialidadNoEncontrada(ParcialidadNoEncontradaException ex) {
        return body(HttpStatus.NOT_FOUND, "NO_ENCONTRADO", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        return body(HttpStatus.FORBIDDEN, "SIN_PRIVILEGIOS", "No tiene permisos para realizar esta accion.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return body(HttpStatus.BAD_REQUEST, "VALIDACION", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, ?>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            errors.put(field, error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAny(Exception ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
        return body(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR", message);
    }

    private static ResponseEntity<Map<String, String>> body(HttpStatus status, String error, String message) {
        Map<String, String> body = new HashMap<>();
        body.put("error", error);
        body.put("message", message != null ? message : "");
        return ResponseEntity.status(status).body(body);
    }
}
