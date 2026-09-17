package com.events.event_Venue.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({EventNotFoundException.class, FechaNotFoundException.class, NotDuplicateException.class, NotNullFechaException.class, InvaliDataException.class, VenueNotFoundException.class, NotNullValueException.class})

    public ResponseEntity<Object> handleException(Exception ex){

        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }


}
