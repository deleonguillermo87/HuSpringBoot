package com.events.event_Venue.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Los datos enviados no son válidos.");
        return buildProblemDetail(HttpStatus.BAD_REQUEST, "Bad Request", message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("Los datos enviados no cumplen la validación requerida.");
        return buildProblemDetail(HttpStatus.BAD_REQUEST, "Bad Request", message);
    }

    @ExceptionHandler({EventNotFoundException.class, VenueNotFoundException.class, FechaNotFoundException.class})
    public ProblemDetail handleNotFound(RuntimeException ex) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(NotDuplicateException.class)
    public ProblemDetail handleConflict(NotDuplicateException ex) {
        return buildProblemDetail(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
    }

    @ExceptionHandler({NotNullFechaException.class, NotNullValueException.class, InvaliDataException.class, IllegalArgumentException.class})
    public ProblemDetail handleBadRequest(RuntimeException ex) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    private ProblemDetail buildProblemDetail(HttpStatus httpStatus, String title, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, detail);
        problemDetail.setTitle(title);
        return problemDetail;
    }
}
