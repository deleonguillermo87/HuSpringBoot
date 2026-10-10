package com.events.event_Venue.dto;

import com.events.event_Venue.validation.NoPastEvents;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class EventRequest {

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres.")
    private String nombre;

    @NotNull(message = "La fecha es obligatoria.")
    @Future(message = "La fecha debe ser futura.")
    @NoPastEvents(message = "La fecha no puede ser una fecha pasada o actual.")
    private LocalDateTime fecha;

    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 120, message = "La descripción no puede superar 120 caracteres.")
    private String descripcion;

    public EventRequest() {
    }

    public EventRequest(String nombre, LocalDateTime fecha, String descripcion) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
