package com.events.event_Venue.controller;

import com.events.event_Venue.model.Event;
import com.events.event_Venue.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> create(@RequestBody Event event) {
        Event nuevoEvent = eventService.create(event);
        return new ResponseEntity<>(nuevoEvent, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Event>> listarTodo() {
        List<Event> lista = eventService.ListarEvents();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Event> buscarPorFecha(@RequestParam LocalDateTime fecha) {
        Event event = eventService.buscarFecha(fecha);
        return ResponseEntity.ok(event);
    }

    @PutMapping
    public ResponseEntity<String> actualizar(@RequestBody Event event) {
        eventService.actualizar(event);
        return ResponseEntity.ok("Evento actualizado exitosamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        String mensaje = eventService.eliminar(id);
        return ResponseEntity.ok(mensaje);
    }
}
