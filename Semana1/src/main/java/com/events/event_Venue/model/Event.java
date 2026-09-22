package com.events.event_Venue.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Event {
    private Long id;
    private String nombre;
    private LocalDateTime fecha;
    private String descripcion;

}
