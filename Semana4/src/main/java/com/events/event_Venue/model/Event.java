package com.events.event_Venue.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_name", nullable = false, length = 80)
    private String nombre;

    @Future
    @Column(name = "date", unique = true, nullable = false)
    private LocalDateTime fecha;

    @Column(name = "description", nullable = false, length = 120)
    private String descripcion;

}
