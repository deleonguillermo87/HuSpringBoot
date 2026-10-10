package com.events.event_Venue.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotDuplicateException extends RuntimeException {
    public NotDuplicateException(String message){
        super(message);
    }
}
