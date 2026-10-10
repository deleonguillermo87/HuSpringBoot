package com.events.event_Venue.model;

import com.events.event_Venue.validation.NoPastEvents;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres.")
    @Column(name = "event_name", nullable = false, length = 80)
    private String nombre;

    @NotNull(message = "La fecha es obligatoria.")
    @Future(message = "La fecha debe ser futura.")
    @NoPastEvents(message = "La fecha no puede ser pasada o actual.")
    @Column(name = "date", unique = true, nullable = false)
    private LocalDateTime fecha;

    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 120, message = "La descripción no puede superar 120 caracteres.")
    @Column(name = "description", nullable = false, length = 120)
    private String descripcion;

}
