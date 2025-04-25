package org.f420.duxchallenge.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.f420.duxchallenge.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        log.error("excepcion capturada: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMessage(ex.getErrorMessage())
                .statusCode(ex.getStatusCode())
                .timestamp(ZonedDateTime.now())
                .build(), HttpStatus.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("\n"));
        log.error("excepcion capturada: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMessage(message)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(ZonedDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponse> handleAuthExceptions(JWTVerificationException ex) {
        log.error("excepcion capturada: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .timestamp(ZonedDateTime.now())
                .build(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        log.error("excepcion capturada: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(ZonedDateTime.now())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
