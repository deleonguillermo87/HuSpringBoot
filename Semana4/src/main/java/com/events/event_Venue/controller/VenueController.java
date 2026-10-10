package com.events.event_Venue.controller;

import com.events.event_Venue.model.Venue;
import com.events.event_Venue.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/venues")
@Tag(name = "Lugares", description = "Endpoints para la gestion de lugares (venues)")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping
    @Operation(
            summary = "Crear un lugar",
            description = "Registra un nuevo lugar con nombre, direccion y capacidad."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lugar creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o lugar duplicado")
    })
    public ResponseEntity<Venue> create(@RequestBody Venue venue) {
        Venue nuevoVenue = venueService.create(venue);
        return new ResponseEntity<>(nuevoVenue, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los lugares",
            description = "Retorna un listado paginado de lugares, ordenado por nombre de forma ascendente por defecto."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de lugares paginados obtenida correctamente")
    })
    public ResponseEntity<Page<Venue>> listarTodo(
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "nombre",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {
        return ResponseEntity.ok(venueService.listarTodo(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar lugar por ID",
            description = "Retorna la informacion de un lugar a partir de su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lugar encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontro un lugar con el ID proporcionado")
    })
    public ResponseEntity<Venue> buscarPorId(
            @Parameter(description = "ID del lugar a buscar", example = "1", required = true)
            @PathVariable Long id) {
        Venue venue = venueService.buscarPorId(id);
        return ResponseEntity.ok(venue);
    }

    @PutMapping
    @Operation(
            summary = "Actualizar lugar",
            description = "Actualiza los datos de un lugar existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lugar actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Lugar no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o duplicados")
    })
    public ResponseEntity<Venue> update(@PathVariable("id")Long id,  @RequestBody Venue venue) {
        Venue venueActualizado = venueService.update(id, venue);
        return ResponseEntity.ok(venueActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar lugar",
            description = "Elimina un lugar existente a partir de su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lugar eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Lugar no encontrado")
    })
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del lugar a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        String mensaje = venueService.eliminar(id);
        return ResponseEntity.ok(mensaje);
    }
}