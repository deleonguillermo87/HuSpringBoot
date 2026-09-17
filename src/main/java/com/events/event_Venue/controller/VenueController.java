package com.events.event_Venue.controller;

import com.events.event_Venue.model.Venue;
import com.events.event_Venue.service.VenueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping
    public ResponseEntity<Venue> create(@RequestBody Venue venue) {
        Venue nuevoVenue = venueService.create(venue);
        return new ResponseEntity<>(nuevoVenue, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Venue>> listarTodo() {
        List<Venue> lista = venueService.listarTodo();
        return ResponseEntity.ok(lista);
    }

    @PutMapping
    public ResponseEntity<Venue> update(@RequestBody Venue venue) {
        Venue venueActualizado = venueService.update(venue);
        return ResponseEntity.ok(venueActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        String mensaje = venueService.eliminar(id);
        return ResponseEntity.ok(mensaje);
    }
}
