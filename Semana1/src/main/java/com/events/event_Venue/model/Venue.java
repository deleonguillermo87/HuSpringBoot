package com.events.event_Venue.model;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Venue {
    private Long id;
    private  String nombre;
    private String direccion;
    private Long capacidad;
}
