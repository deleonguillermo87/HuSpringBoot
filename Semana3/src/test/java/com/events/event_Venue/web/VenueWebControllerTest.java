package com.events.event_Venue.web;

import com.events.event_Venue.model.Venue;
import com.events.event_Venue.service.VenueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VenueWebController.class)
public class VenueWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VenueService venueService;

    // Escenario 1: Visualización con datos (Camino Feliz)
    @Test
    void listarVenues_conDatos_debeMostrarTabla() throws Exception {
        List<Venue> venues = List.of(
                new Venue(1L, "Teatro Amira de la Rosa", "Barranquilla", 1000L)
        );
        Page<Venue> page = new PageImpl<>(venues);

        when(venueService.listarTodo(PageRequest.of(0, 10))).thenReturn(page);

        mockMvc.perform(get("/admin/venues/listar"))
                .andExpect(status().isOk())
                .andExpect(view().name("venueList"))
                .andExpect(model().attributeExists("venue"))
                .andExpect(model().attribute("venue", venues));
    }

    // Escenario 2: Catálogo vacío (Caso de Borde)
    @Test
    void listarVenues_sinDatos_debeRetornarListaVacia() throws Exception {
        Page<Venue> pageVacia = new PageImpl<>(Collections.emptyList());

        when(venueService.listarTodo(PageRequest.of(0, 10))).thenReturn(pageVacia);

        mockMvc.perform(get("/admin/venues/listar"))
                .andExpect(status().isOk())
                .andExpect(view().name("venueList"))
                .andExpect(model().attribute("venue", Collections.emptyList()));
    }

    // Vista del formulario de creación
    @Test
    void newVenue_debeMostrarFormularioConVenueVacio() throws Exception {
        mockMvc.perform(get("/admin/venues/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("venueCrear"))
                .andExpect(model().attributeExists("venue"));
    }

    // Escenario 3: Registro y redirección exitosa (Post-Redirect-Get)
    @Test
    void crear_conDatosValidos_debeRedirigirAlListado() throws Exception {
        Venue venueGuardado = new Venue(1L, "Movistar Arena", "Bogota DC", 6000L);
        when(venueService.create(org.mockito.ArgumentMatchers.any(Venue.class)))
                .thenReturn(venueGuardado);

        mockMvc.perform(post("/admin/venues/crear")
                        .param("nombre", "Movistar Arena")
                        .param("direccion", "Bogota DC")
                        .param("capacidad", "6000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/venues/listar"))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    void crear_conError_debeRedirigirAFormularioConError() throws Exception {
        when(venueService.create(org.mockito.ArgumentMatchers.any(Venue.class)))
                .thenThrow(new RuntimeException("Ya existe un lugar registrado con ese nombre."));

        mockMvc.perform(post("/admin/venues/crear")
                        .param("nombre", "Movistar Arena")
                        .param("direccion", "Bogota DC")
                        .param("capacidad", "6000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/venues/nuevo"))
                .andExpect(flash().attributeExists("error"));
    }

    // Detalle de venue existente
    @Test
    void detalle_conIdExistente_debeMostrarVenue() throws Exception {
        Venue venue = new Venue(80L, "Metropolitano", "Barranquilla", 46000L);
        when(venueService.buscarPorId(80L)).thenReturn(venue);

        mockMvc.perform(get("/admin/venues/80"))
                .andExpect(status().isOk())
                .andExpect(view().name("venueDetalle"))
                .andExpect(model().attribute("venue", venue));
    }

    // Actualizar - Post-Redirect-Get
    @Test
    void actualizar_conDatosValidos_debeRedirigirAlListado() throws Exception {
        Venue venueActualizado = new Venue(1L, "Movistar Arena Renovado", "Bogota DC", 7000L);
        when(venueService.update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any(Venue.class)))
                .thenReturn(venueActualizado);

        mockMvc.perform(post("/admin/venues/1/actualizar")
                        .param("nombre", "Movistar Arena Renovado")
                        .param("direccion", "Bogota DC")
                        .param("capacidad", "7000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/venues/listar"))
                .andExpect(flash().attributeExists("message"));
    }

    // Eliminar - Post-Redirect-Get
    @Test
    void eliminar_conIdExistente_debeRedirigirAlListado() throws Exception {
        mockMvc.perform(post("/admin/venues/1/eliminar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/venues/listar"))
                .andExpect(flash().attributeExists("message"));
    }
}