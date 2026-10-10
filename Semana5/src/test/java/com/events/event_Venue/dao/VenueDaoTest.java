package com.events.event_Venue.dao;

import com.events.event_Venue.model.Venue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class VenueDaoTest {

    @Autowired
    private VenueDao venueDao;

    @Test
    void save_debeGuardarVenueConIdAutogenerado() {
        Venue venue = new Venue(null, "Teatro amira de la rosa", "Barranquilla", 1000L);

        Venue guardado = venueDao.save(venue);

        assertNotNull(guardado.getId());
        assertEquals("Teatro amira de la rosa", guardado.getNombre());
    }

    @Test
    void existsById_conIdExistente_debeRetornarTrue() {
        Venue venue = venueDao.save(new Venue(null, "Teatro amira de la rosa", "Barranquilla", 1000L));

        assertTrue(venueDao.existsById(venue.getId()));
    }

    @Test
    void existsById_conIdInexistente_debeRetornarFalse() {
        assertFalse(venueDao.existsById(999L));
    }

    @Test
    void existsByNombre_conNombreExistente_debeRetornarTrue() {
        venueDao.save(new Venue(null, "Teatro amira de la rosa", "Barranquilla", 1000L));

        assertTrue(venueDao.existsByNombre("Teatro amira de la rosa"));
    }

    @Test
    void existsByNombre_conNombreInexistente_debeRetornarFalse() {
        assertFalse(venueDao.existsByNombre("No existe"));
    }

    @Test
    void existsByNombreAndIdNot_conMismoId_debeRetornarFalse() {
        Venue venue = venueDao.save(new Venue(null, "Teatro amira de la rosa", "Barranquilla", 1000L));

        assertFalse(venueDao.existsByNombreAndIdNot("Teatro amira de la rosa", venue.getId()));
    }

    @Test
    void existsByNombreAndIdNot_conOtroId_debeRetornarTrue() {
        Venue venue = venueDao.save(new Venue(null, "Teatro amira de la rosa", "Barranquilla", 1000L));

        assertTrue(venueDao.existsByNombreAndIdNot("Teatro amira de la rosa", 999L));
    }

    @Test
    void findById_conIdExistente_debeRetornarVenue() {
        Venue venue = venueDao.save(new Venue(null, "Teatro amira de la rosa", "Barranquilla", 1000L));

        Optional<Venue> resultado = venueDao.findById(venue.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Teatro amira de la rosa", resultado.get().getNombre());
    }

    @Test
    void findById_conIdInexistente_debeRetornarVacio() {
        Optional<Venue> resultado = venueDao.findById(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deleteById_debeEliminarVenue() {
        Venue venue = venueDao.save(new Venue(null, "Teatro amira de la rosa", "Barranquilla", 1000L));
        Long id = venue.getId();

        venueDao.deleteById(id);

        assertFalse(venueDao.existsById(id));
    }

    @Test
    void findAll_debeRetornarTodosLosVenues() {
        venueDao.save(new Venue(null, "Venue 1", "Direccion 1", 100L));
        venueDao.save(new Venue(null, "Venue 2", "Direccion 2", 200L));

        assertEquals(2, venueDao.findAll().size());
    }
}