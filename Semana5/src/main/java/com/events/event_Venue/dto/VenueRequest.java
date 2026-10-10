package com.events.event_Venue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class VenueRequest {

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres.")
    private String nombre;

    @NotBlank(message = "La dirección no puede estar vacía.")
    @Size(max = 70, message = "La dirección no puede superar 70 caracteres.")
    private String direccion;

    @NotNull(message = "La capacidad es obligatoria.")
    @Positive(message = "La capacidad debe ser mayor a cero.")
    private Long capacidad;

    public VenueRequest() {
    }

    public VenueRequest(String nombre, String direccion, Long capacidad) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidad = capacidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Long capacidad) {
        this.capacidad = capacidad;
    }
}
