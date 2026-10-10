package com.events.event_Venue.web;

import com.events.event_Venue.dto.EventRequest;
import com.events.event_Venue.dto.EventResponse;
import com.events.event_Venue.service.EventService;
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

import java.time.LocalDateTime;
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
class EventWebControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new EventWebController(eventService)).build();
    }

    @Test
    void listarTodo_conDatos_debeMostrarTabla() throws Exception {
        List<EventResponse> events = List.of(
                new EventResponse(1L, "Conferencia Spring Boot", LocalDateTime.now().plusDays(5), "Evento tecnico")
        );
        Page<EventResponse> page = new PageImpl<>(events);

        when(eventService.ListarEvents(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/admin/event"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventosList"))
                .andExpect(model().attributeExists("events"));
    }

    @Test
    void listarTodo_sinDatos_debeRetornarListaVacia() throws Exception {
        Page<EventResponse> pageVacia = new PageImpl<>(Collections.emptyList());

        when(eventService.ListarEvents(any(Pageable.class))).thenReturn(pageVacia);

        mockMvc.perform(get("/admin/event"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventosList"))
                .andExpect(model().attributeExists("events"));
    }

    @Test
    void newEvent_debeMostrarFormularioConEventoVacio() throws Exception {
        mockMvc.perform(get("/admin/event/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventoCrear"))
                .andExpect(model().attributeExists("event"));
    }

    @Test
    void crear_conDatosValidos_debeRedirigirAlListado() throws Exception {
        when(eventService.create(any(EventRequest.class)))
                .thenReturn(new EventResponse(1L, "Concierto Anual de Jazz", LocalDateTime.now().plusDays(10), "Resumen del evento"));

        mockMvc.perform(post("/admin/event/create")
                        .param("nombre", "Concierto Anual de Jazz")
                        .param("fecha", "2026-10-15T19:30")
                        .param("descripcion", "Resumen del evento"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/event"))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    void crear_conError_debeVolverAlFormularioConError() throws Exception {
        when(eventService.create(any(EventRequest.class)))
                .thenThrow(new RuntimeException("Ya existe un evento con ese nombre."));

        mockMvc.perform(post("/admin/event/create")
                        .param("nombre", "Concierto Anual de Jazz")
                        .param("fecha", "2026-10-15T19:30")
                        .param("descripcion", "Resumen del evento"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventoCrear"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void buscarId_conIdExistente_debeMostrarEvento() throws Exception {
        EventResponse evento = new EventResponse(80L, "Feria Tecnológica", LocalDateTime.now().plusDays(3), "Evento anual");
        when(eventService.buscarId(80L)).thenReturn(evento);

        mockMvc.perform(get("/admin/event/80"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventosDelete"))
                .andExpect(model().attribute("event", evento));
    }

    @Test
    void actualizar_conDatosValidos_debeRedirigirAlListado() throws Exception {
        when(eventService.actualizar(eq(1L), any(EventRequest.class)))
                .thenReturn(new EventResponse(1L, "Concierto Renovado", LocalDateTime.now().plusDays(10), "Descripcion actualizada"));

        mockMvc.perform(post("/admin/event/1/actualizar")
                        .param("nombre", "Concierto Renovado")
                        .param("fecha", "2026-10-20T20:00")
                        .param("descripcion", "Descripcion actualizada"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/event"))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    void actualizar_conError_debeRedirigirAlDetalleConError() throws Exception {
        when(eventService.actualizar(eq(1L), any(EventRequest.class)))
                .thenThrow(new RuntimeException("No se encontró el evento"));

        mockMvc.perform(post("/admin/event/1/actualizar")
                        .param("nombre", "Concierto Renovado")
                        .param("fecha", "2026-10-20T20:00")
                        .param("descripcion", "Descripcion actualizada"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/event/1"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void eliminarEvent_conIdExistente_debeRedirigirAlListado() throws Exception {
        mockMvc.perform(post("/admin/event/1/eliminar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/event"))
                .andExpect(flash().attributeExists("message"));
    }
}