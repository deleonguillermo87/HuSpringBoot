package com.events.event_Venue.controller;

import com.events.event_Venue.model.Event;
import com.events.event_Venue.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Eventos", description = "Endpoints para la gestion de eventos")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @Operation(
            summary = "Crear un evento",
            description = "Registra un nuevo evento con nombre, fecha y descripcion."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o evento duplicado")
    })
    public ResponseEntity<Event> create(@RequestBody Event event) {
        Event nuevoEvent = eventService.create(event);
        return new ResponseEntity<>(nuevoEvent, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    @Operation(
            summary = "Listar todos los eventos",
            description = "Retorna un listado paginado de eventos, ordenado por nombre de forma ascendente por defecto."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de eventos paginados obtenida correctamente")
    })
    public ResponseEntity<Page<Event>> listarTodo(
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "nombre",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable) {
        return ResponseEntity.ok(eventService.ListarEvents(pageable));
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar evento por ID",
            description = "Retorna la informacion de un evento a partir de su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontro un evento con el ID proporcionado")
    })
    public ResponseEntity<Event> buscarId(
            @Parameter(description = "ID del evento a buscar", example = "1", required = true)
            @RequestParam Long id) {
        Event event = eventService.buscarId(id);
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar evento",
            description = "Actualiza los datos de un evento existente a partir de su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o duplicados")
    })
    public ResponseEntity<String> actualizar(
            @Parameter(description = "ID del evento a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody Event eventActualizar) {
        eventService.actualizar(id, eventActualizar);
        return ResponseEntity.ok("Evento actualizado exitosamente");
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar evento",
            description = "Elimina un evento existente a partir de su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del evento a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        String mensaje = eventService.eliminar(id);
        return ResponseEntity.ok(mensaje);
    }
}