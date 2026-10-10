package com.events.event_Venue.web;

import com.events.event_Venue.dto.VenueRequest;
import com.events.event_Venue.dto.VenueResponse;
import com.events.event_Venue.service.VenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class VenueWebControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VenueService venueService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new VenueWebController(venueService)).build();
    }

    @Test
    void listarVenues_conDatos_debeMostrarTabla() throws Exception {
        List<VenueResponse> venues = List.of(
                new VenueResponse(1L, "Teatro Amira de la Rosa", "Barranquilla", 1000L)
        );
        Page<VenueResponse> page = new PageImpl<>(venues);

        when(venueService.listarTodo(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/admin/venues/listar"))
                .andExpect(status().isOk())
                .andExpect(view().name("venueList"))
                .andExpect(model().attributeExists("venue"));
    }

    @Test
    void listarVenues_sinDatos_debeRetornarListaVacia() throws Exception {
        Page<VenueResponse> pageVacia = new PageImpl<>(Collections.emptyList());

        when(venueService.listarTodo(any(Pageable.class))).thenReturn(pageVacia);

        mockMvc.perform(get("/admin/venues/listar"))
                .andExpect(status().isOk())
                .andExpect(view().name("venueList"))
                .andExpect(model().attributeExists("venue"));
    }

    @Test
    void newVenue_debeMostrarFormularioConVenueVacio() throws Exception {
        mockMvc.perform(get("/admin/venues/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("venueCrear"))
                .andExpect(model().attributeExists("venue"));
    }

    @Test
    void crear_conDatosValidos_debeRedirigirAlListado() throws Exception {
        when(venueService.create(any(VenueRequest.class)))
                .thenReturn(new VenueResponse(1L, "Movistar Arena", "Bogota DC", 6000L));

        mockMvc.perform(post("/admin/venues/crear")
                        .param("nombre", "Movistar Arena")
                        .param("direccion", "Bogota DC")
                        .param("capacidad", "6000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/venues/listar"))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    void crear_conError_debeVolverAlFormularioConError() throws Exception {
        when(venueService.create(any(VenueRequest.class)))
                .thenThrow(new RuntimeException("Ya existe un lugar registrado con ese nombre."));

        mockMvc.perform(post("/admin/venues/crear")
                        .param("nombre", "Movistar Arena")
                        .param("direccion", "Bogota DC")
                        .param("capacidad", "6000"))
                .andExpect(status().isOk())
                .andExpect(view().name("venueCrear"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void detalle_conIdExistente_debeMostrarVenue() throws Exception {
        VenueResponse venue = new VenueResponse(80L, "Metropolitano", "Barranquilla", 46000L);
        when(venueService.buscarPorId(80L)).thenReturn(venue);

        mockMvc.perform(get("/admin/venues/80"))
                .andExpect(status().isOk())
                .andExpect(view().name("venueDetalle"))
                .andExpect(model().attribute("venue", venue));
    }

    @Test
    void actualizar_conDatosValidos_debeRedirigirAlListado() throws Exception {
        when(venueService.update(eq(1L), any(VenueRequest.class)))
                .thenReturn(new VenueResponse(1L, "Movistar Arena Renovado", "Bogota DC", 7000L));

        mockMvc.perform(post("/admin/venues/1/actualizar")
                        .param("nombre", "Movistar Arena Renovado")
                        .param("direccion", "Bogota DC")
                        .param("capacidad", "7000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/venues/listar"))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    void actualizar_conError_debeRedirigirAlDetalleConError() throws Exception {
        when(venueService.update(eq(1L), any(VenueRequest.class)))
                .thenThrow(new RuntimeException("No se encontró el lugar"));

        mockMvc.perform(post("/admin/venues/1/actualizar")
                        .param("nombre", "Movistar Arena Renovado")
                        .param("direccion", "Bogota DC")
                        .param("capacidad", "7000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/venues/1"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void eliminar_conIdExistente_debeRedirigirAlListado() throws Exception {
        mockMvc.perform(post("/admin/venues/1/eliminar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/venues/listar"))
                .andExpect(flash().attributeExists("message"));
    }
}