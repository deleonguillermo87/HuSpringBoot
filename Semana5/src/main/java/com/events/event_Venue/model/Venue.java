package com.events.event_Venue.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres.")
    @Column(name = "venue_name", unique = true, nullable = false, length = 80)
    private  String nombre;

    @NotBlank(message = "La dirección no puede estar vacía.")
    @Size(max = 70, message = "La dirección no puede superar 70 caracteres.")
    @Column(name = "direction", nullable = false, length = 70)
    private String direccion;

    @NotNull(message = "La capacidad es obligatoria.")
    @Positive(message = "La capacidad debe ser mayor a cero.")
    @Column(name = "capacity", nullable = false)
    private Long capacidad;
}
