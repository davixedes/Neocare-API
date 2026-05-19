package com.neocare.api.interfaces.handler;

import com.neocare.api.application.exception.EntidadeNaoEncontradaException;
import com.neocare.api.application.exception.UsuarioUnsupportedOperation;
import com.neocare.api.domain.exception.ValidacaoDominioException;
import com.neocare.api.domain.logging.Logger;
import com.neocare.api.infrastructure.exception.InfraestruturaException;
import com.neocare.api.infrastructure.logging.LoggerFactory;
import com.neocare.api.interfaces.dto.exception.ErrorResponse;
import com.neocare.api.interfaces.dto.exception.FieldErrorDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FieldErrorDetail>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        List<FieldErrorDetail> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldErrorDetail(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erros);
    }

    @ExceptionHandler(UsuarioUnsupportedOperation.class)
    public ResponseEntity<ErrorResponse> handleUsuarioUnsupportedOperation(
            UsuarioUnsupportedOperation e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, e.getMessage(), request));
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntidadeNaoEncontradaException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    @ExceptionHandler(ValidacaoDominioException.class)
    public ResponseEntity<ErrorResponse> handleValidacaoDominioException(
            ValidacaoDominioException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(buildError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request));
    }

    @ExceptionHandler(InfraestruturaException.class)
    public ResponseEntity<ErrorResponse> handleInfraestruturaException(
            InfraestruturaException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleExceptionGeneric(Exception e, HttpServletRequest request) {
        logger.error("Erro inesperado ao processar a requisição: {}", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado", request));
    }

    private ErrorResponse buildError(HttpStatus status, String message, HttpServletRequest request) {
        return new ErrorResponse(status.value(), message, LocalDateTime.now().toString(), request.getRequestURI());
    }
}
