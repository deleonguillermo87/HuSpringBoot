package com.events.event_Venue.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VenueNotFoundException extends  RuntimeException{
    public VenueNotFoundException(String message){
        super(message);
    }
}
