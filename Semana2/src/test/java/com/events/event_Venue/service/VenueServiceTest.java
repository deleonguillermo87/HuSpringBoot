package com.events.event_Venue.service;

import com.events.event_Venue.dao.VenueDao;
import com.events.event_Venue.exception.InvaliDataException;
import com.events.event_Venue.exception.NotDuplicateException;
import com.events.event_Venue.exception.NotNullValueException;
import com.events.event_Venue.exception.VenueNotFoundException;
import com.events.event_Venue.model.Venue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VenueServiceTest {

    @Mock
    private VenueDao venueDao;

    @InjectMocks
    private VenueService venueService;

    @Test
    void create_conDatosValidos_debeGuardarVenue() {
        Venue venueValido = new Venue(null, "Teatro amira de la rosa", "Barranquilla", 1000L);

        when(venueDao.existsByNombre("Teatro amira de la rosa")).thenReturn(false);
        when(venueDao.save(venueValido)).thenReturn(venueValido);

        Venue resultado = venueService.create(venueValido);

        assertNotNull(resultado);
        assertEquals("Teatro amira de la rosa", resultado.getNombre());
    }

    @Test
    void create_conIdDuplicado_debeLanzarExcepcion() {
        Venue venueIdDuplicado = new Venue(1L, "Teatro amira de la rosa", "Barranquilla", 1000L);

        when(venueDao.existsById(1L)).thenReturn(true);

        assertThrows(VenueNotFoundException.class, () -> venueService.create(venueIdDuplicado));
    }

    @Test
    void create_conNombreVacio_debeLanzarExcepcion() {
        Venue venueVacio = new Venue(null, "", "Barranquilla", 1000L);

        assertThrows(NotNullValueException.class, () -> venueService.create(venueVacio));
    }

    @Test
    void create_conNombreDuplicado_debeLanzarExcepcion() {
        Venue venueDuplicate = new Venue(null, "metro politano", "Barranquilla", 70000L);

        when(venueDao.existsByNombre("metro politano")).thenReturn(true);

        assertThrows(NotDuplicateException.class, () -> venueService.create(venueDuplicate));
    }

    @Test
    void create_conDireccionVacia_debeLanzarExcepcion() {
        Venue venueDireccionVacia = new Venue(null, "Movistar arena", "", 70000L);

        when(venueDao.existsByNombre("Movistar arena")).thenReturn(false);

        assertThrows(NotNullValueException.class, () -> venueService.create(venueDireccionVacia));
    }

    @Test
    void create_conCapacidadInvalida_debeLanzarExcepcion() {
        Venue venueCapacidadInvalida = new Venue(null, "Movistar arena", "Bogota DC", null);

        when(venueDao.existsByNombre("Movistar arena")).thenReturn(false);

        assertThrows(VenueNotFoundException.class, () -> venueService.create(venueCapacidadInvalida));
    }

    @Test
    void buscarPorId_noEncontrado_debeLanzarExcepcion() {
        when(venueDao.findById(99L)).thenReturn(Optional.empty());

        assertThrows(VenueNotFoundException.class, () -> venueService.buscarPorId(99L));
    }

    @Test
    void update_conDatosValidos_debeActualizarVenue() {
        Venue venueActualizado = new Venue(1L, "Movistar arena actualizado", "Bogota DC", 6000L);

        when(venueDao.existsByNombre("Movistar arena actualizado")).thenReturn(false);
        when(venueDao.existsByNombreAndIdNot("Movistar arena actualizado", 1L)).thenReturn(false);

        Venue resultado = venueService.update(venueActualizado);

        assertNotNull(resultado);
        assertEquals("Movistar arena actualizado", resultado.getNombre());
        verify(venueDao, times(1)).save(venueActualizado);
    }

    @Test
    void update_conNombreYaExistente_debeLanzarExcepcion() {
        Venue venueActualizado = new Venue(1L, "Movistar arena", "Bogota DC", 6000L);

        when(venueDao.existsByNombre("Movistar arena")).thenReturn(true);

        assertThrows(NotDuplicateException.class, () -> venueService.update(venueActualizado));
    }

    @Test
    void update_conNombreDuplicadoOtroId_debeLanzarExcepcion() {
        Venue venueActualizado = new Venue(1L, "Movistar arena", "Bogota DC", 6000L);

        when(venueDao.existsByNombre("Movistar arena")).thenReturn(false);
        when(venueDao.existsByNombreAndIdNot("Movistar arena", 1L)).thenReturn(true);

        assertThrows(NotDuplicateException.class, () -> venueService.update(venueActualizado));
    }

    @Test
    void eliminar_idExiste_debeEliminarVenue() {
        when(venueDao.existsById(1L)).thenReturn(true);

        String resultado = venueService.eliminar(1L);

        assertEquals("Lugar eliminado exitosamente...", resultado);
        verify(venueDao, times(1)).deleteById(1L);
    }

    @Test
    void eliminar_idNoExiste_debeLanzarExcepcion() {
        when(venueDao.existsById(99L)).thenReturn(false);

        assertThrows(InvaliDataException.class, () -> venueService.eliminar(99L));

        verify(venueDao, never()).deleteById(any());
    }
}