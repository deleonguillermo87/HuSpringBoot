package com.events.event_Venue.service;

import com.events.event_Venue.dao.EventDao;
import com.events.event_Venue.exception.*;
import com.events.event_Venue.model.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventDao eventDao;

    @InjectMocks
    private EventService eventService;

    @Test
    void create_conDatosValidos_debeGuardarEvent() {
        Event eventValido = new Event(1L, "Conferencia spring boot",
                LocalDateTime.now().plusDays(5), "Evento tecnico sobre backend");

        when(eventDao.existeId(1L)).thenReturn(false);
        when(eventDao.existeNombreFecha(eventValido.getNombre(), eventValido.getFecha())).thenReturn(false);
        when(eventDao.guardar(eventValido)).thenReturn(eventValido);

        Event resultado = eventService.create(eventValido);

        assertNotNull(resultado);
        assertEquals("Conferencia spring boot", resultado.getNombre());
    }

    @Test
    void create_conIdCeroOMenor_noDebeValidarId() {
        Event eventIdCero = new Event(0L, "Meetup java",
                LocalDateTime.now().plusDays(5), "Networking para devs");

        when(eventDao.existeNombreFecha(eventIdCero.getNombre(), eventIdCero.getFecha())).thenReturn(false);
        when(eventDao.guardar(eventIdCero)).thenReturn(eventIdCero);

        Event resultado = eventService.create(eventIdCero);

        assertNotNull(resultado);
        verify(eventDao, never()).existeId(any());
    }

    @Test
    void create_conIdDuplicado_debeLanzarExcepcion() {
        Event eventIdDuplicado = new Event(1L, "Meetup java",
                LocalDateTime.now().plusDays(5), "Networking para devs");

        when(eventDao.existeId(1L)).thenReturn(true);

        assertThrows(EventNotFoundException.class, () -> eventService.create(eventIdDuplicado));
    }

    @Test
    void create_conNombreVacio_debeLanzarExcepcion() {
        Event eventNombreVacio = new Event(1L, "",
                LocalDateTime.now().plusDays(5), "Descripcion valida");

        when(eventDao.existeId(1L)).thenReturn(false);

        assertThrows(NotNullValueException.class, () -> eventService.create(eventNombreVacio));
    }

    @Test
    void create_conFechaEnPasado_debeLanzarExcepcion() {
        Event eventFechaPasada = new Event(1L, "Hackathon backend",
                LocalDateTime.now().minusDays(1), "Descripcion valida");

        when(eventDao.existeId(1L)).thenReturn(false);

        assertThrows(NotNullFechaException.class, () -> eventService.create(eventFechaPasada));
    }

    @Test
    void create_conNombreFechaDuplicado_debeLanzarExcepcion() {
        LocalDateTime fecha = LocalDateTime.now().plusDays(5);
        Event eventDuplicado = new Event(1L, "Hackathon backend", fecha, "Descripcion valida");

        when(eventDao.existeId(1L)).thenReturn(false);
        when(eventDao.existeNombreFecha("Hackathon backend", fecha)).thenReturn(true);

        assertThrows(NotDuplicateException.class, () -> eventService.create(eventDuplicado));
    }

    @Test
    void create_conDescripcionVacia_debeLanzarExcepcion() {
        LocalDateTime fecha = LocalDateTime.now().plusDays(5);
        Event eventDescripcionVacia = new Event(1L, "Hackathon backend", fecha, "");

        when(eventDao.existeId(1L)).thenReturn(false);
        when(eventDao.existeNombreFecha("Hackathon backend", fecha)).thenReturn(false);

        assertThrows(NotNullValueException.class, () -> eventService.create(eventDescripcionVacia));
    }

    @Test
    void buscarFecha_noEncontrada_debeLanzarExcepcion() {
        LocalDateTime fecha = LocalDateTime.now().plusDays(10);

        when(eventDao.buscarFecha(fecha)).thenReturn(Optional.empty());

        assertThrows(FechaNotFoundException.class, () -> eventService.buscarFecha(fecha));
    }

    @Test
    void eliminar_idExiste_debeEliminarEvent() {
        when(eventDao.existeId(1L)).thenReturn(true);

        String resultado = eventService.eliminar(1L);

        assertEquals("Evento eliminado exitosamente", resultado);
        verify(eventDao, times(1)).eliminar(1L);
    }

    @Test
    void eliminar_idNoExiste_debeLanzarExcepcion() {
        when(eventDao.existeId(99L)).thenReturn(false);

        assertThrows(EventNotFoundException.class, () -> eventService.eliminar(99L));

        verify(eventDao, never()).eliminar(any());
    }
}