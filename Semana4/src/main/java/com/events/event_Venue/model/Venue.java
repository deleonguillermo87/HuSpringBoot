package com.events.event_Venue.model;

import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity(name = "Venue")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "venue_name", unique = true, nullable = false, length = 80)
    private  String nombre;
    @Column(name = "direction", nullable = false, length = 70)
    private String direccion;

    @Column(name = "capacity", nullable = false)
    private Long capacidad;
}
