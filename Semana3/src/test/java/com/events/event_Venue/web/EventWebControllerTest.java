package com.events.event_Venue.web;

import com.events.event_Venue.model.Event;
import com.events.event_Venue.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventWebController.class)
public class EventWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    // Escenario 1: Visualización con datos (Camino Feliz)
    @Test
    void listarTodo_conDatos_debeMostrarTabla() throws Exception {
        List<Event> events = List.of(
                new Event(1L, "Conferencia Spring Boot", LocalDateTime.now().plusDays(5), "Evento tecnico")
        );
        Page<Event> page = new PageImpl<>(events);

        when(eventService.ListarEvents(PageRequest.of(0, 10))).thenReturn(page);

        mockMvc.perform(get("/admin/event"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventosList"))
                .andExpect(model().attributeExists("events"))
                .andExpect(model().attribute("events", events));
    }

    // Escenario 2: Catálogo vacío (Caso de Borde)
    @Test
    void listarTodo_sinDatos_debeRetornarListaVacia() throws Exception {
        Page<Event> pageVacia = new PageImpl<>(Collections.emptyList());

        when(eventService.ListarEvents(PageRequest.of(0, 10))).thenReturn(pageVacia);

        mockMvc.perform(get("/admin/event"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventosList"))
                .andExpect(model().attribute("events", Collections.emptyList()));
    }

    // Vista del formulario de creación
    @Test
    void newEvent_debeMostrarFormularioConEventoVacio() throws Exception {
        mockMvc.perform(get("/admin/event/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventoCrear"))
                .andExpect(model().attributeExists("event"));
    }

    // Escenario 3: Registro y redirección exitosa (Post-Redirect-Get)
    @Test
    void crear_conDatosValidos_debeRedirigirAlListado() throws Exception {
        Event eventoGuardado = new Event(1L, "Concierto Anual de Jazz", LocalDateTime.now().plusDays(10), "Resumen del evento");
        when(eventService.create(any(Event.class))).thenReturn(eventoGuardado);

        mockMvc.perform(post("/admin/event/create")
                        .param("nombre", "Concierto Anual de Jazz")
                        .param("fecha", "2026-10-15T19:30")
                        .param("descripcion", "Resumen del evento"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/event"))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    void crear_conError_debeRedirigirAFormularioConError() throws Exception {
        when(eventService.create(any(Event.class)))
                .thenThrow(new RuntimeException("Ya existe un evento con ese nombre."));

        mockMvc.perform(post("/admin/event/create")
                        .param("nombre", "Concierto Anual de Jazz")
                        .param("fecha", "2026-10-15T19:30")
                        .param("descripcion", "Resumen del evento"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/event/nuevo"))
                .andExpect(flash().attributeExists("error"));
    }

    // Detalle de evento existente
    @Test
    void buscarId_conIdExistente_debeMostrarEvento() throws Exception {
        Event evento = new Event(80L, "Feria Tecnológica", LocalDateTime.now().plusDays(3), "Evento anual");
        when(eventService.buscarId(80L)).thenReturn(evento);

        mockMvc.perform(get("/admin/event/80"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventosDelete"))
                .andExpect(model().attribute("event", evento));
    }

    // Actualizar - Post-Redirect-Get
    @Test
    void actualizar_conDatosValidos_debeRedirigirAlListado() throws Exception {
        Event eventoActualizado = new Event(1L, "Concierto Renovado", LocalDateTime.now().plusDays(10), "Descripcion actualizada");
        when(eventService.actualizar(eq(1L), any(Event.class))).thenReturn(eventoActualizado);

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
        when(eventService.actualizar(eq(1L), any(Event.class)))
                .thenThrow(new RuntimeException("No se encontró el evento"));

        mockMvc.perform(post("/admin/event/1/actualizar")
                        .param("nombre", "Concierto Renovado")
                        .param("fecha", "2026-10-20T20:00")
                        .param("descripcion", "Descripcion actualizada"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/event/1"))
                .andExpect(flash().attributeExists("error"));
    }

    // Eliminar - Post-Redirect-Get
    @Test
    void eliminarEvent_conIdExistente_debeRedirigirAlListado() throws Exception {
        mockMvc.perform(post("/admin/event/1/eliminar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/event"))
                .andExpect(flash().attributeExists("message"));
    }
}