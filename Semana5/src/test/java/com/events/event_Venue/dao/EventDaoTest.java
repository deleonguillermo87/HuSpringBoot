package com.events.event_Venue.dao;

import com.events.event_Venue.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EventDaoTest {

    @Autowired
    private EventDao eventDao;

    @Test
    void save_debeGuardarEventConIdAutogenerado() {
        Event event = new Event(null, "Conferencia spring boot",
                LocalDateTime.now().plusDays(5), "Evento tecnico");

        Event guardado = eventDao.save(event);

        assertNotNull(guardado.getId());
        assertEquals("Conferencia spring boot", guardado.getNombre());
    }

    @Test
    void existsById_conIdExistente_debeRetornarTrue() {
        Event event = eventDao.save(new Event(null, "Conferencia spring boot",
                LocalDateTime.now().plusDays(5), "Evento tecnico"));

        assertTrue(eventDao.existsById(event.getId()));
    }

    @Test
    void existsById_conIdInexistente_debeRetornarFalse() {
        assertFalse(eventDao.existsById(999L));
    }

    @Test
    void existsByNombreAndFecha_conCoincidencia_debeRetornarTrue() {
        LocalDateTime fecha = LocalDateTime.now().plusDays(5).withNano(0);
        Event event = new Event(null, "Hackathon backend", fecha, "Competencia de 24 horas");
        eventDao.save(event);

        assertTrue(eventDao.existsByNombreAndFecha("Hackathon backend", fecha));
    }

    @Test
    void existsByNombreAndFecha_sinCoincidencia_debeRetornarFalse() {
        assertFalse(eventDao.existsByNombreAndFecha("No existe", LocalDateTime.now().plusDays(5)));
    }

    @Test
    void findById_conIdExistente_debeRetornarEvent() {
        Event event = eventDao.save(new Event(null, "Meetup java",
                LocalDateTime.now().plusDays(3), "Networking"));

        Optional<Event> resultado = eventDao.findById(event.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Meetup java", resultado.get().getNombre());
    }

    @Test
    void findById_conIdInexistente_debeRetornarVacio() {
        Optional<Event> resultado = eventDao.findById(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deleteById_debeEliminarEvent() {
        Event event = eventDao.save(new Event(null, "Meetup java",
                LocalDateTime.now().plusDays(3), "Networking"));
        Long id = event.getId();

        eventDao.deleteById(id);

        assertFalse(eventDao.existsById(id));
    }

    @Test
    void findAll_debeRetornarTodosLosEventos() {
        eventDao.save(new Event(null, "Evento 1", LocalDateTime.now().plusDays(1), "Desc 1"));
        eventDao.save(new Event(null, "Evento 2", LocalDateTime.now().plusDays(2), "Desc 2"));

        assertEquals(2, eventDao.findAll().size());
    }
}