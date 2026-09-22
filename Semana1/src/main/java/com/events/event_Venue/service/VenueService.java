package com.events.event_Venue.service;


import com.events.event_Venue.dao.VenueDao;
import com.events.event_Venue.exception.InvaliDataException;
import com.events.event_Venue.exception.NotDuplicateException;
import com.events.event_Venue.exception.NotNullValueException;
import com.events.event_Venue.exception.VenueNotFoundException;
import com.events.event_Venue.model.Venue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VenueService {

    private final VenueDao venueDao;

    public VenueService(VenueDao venueDao) {
        this.venueDao = venueDao;
    }

    public Venue create(Venue venue){
        if (venue.getId() != null && venue.getId() > 0 && venueDao.existeId(venue.getId())){
            throw new VenueNotFoundException("El id ya existe...");
        }

        if (venue.getNombre() == null || venue.getNombre().isBlank()){
            throw new NotNullValueException("El nombre no puede estar vacio..");
        }

        if (venueDao.existeNombre(venue.getNombre())) {
            throw new NotDuplicateException("Ya existe un lugar registrado con ese nombre.");
        }

        if (venue.getDireccion() == null || venue.getDireccion().isBlank()){
            throw new NotNullValueException("La direccion no puede estar vacia...");
        }
        if (venue.getCapacidad() == null ||  venue.getCapacidad() <= 0){
            throw new VenueNotFoundException("Ingrese un numero mayor a 0");
        }

        return venueDao.guardarVenue(venue);

    }

    public List<Venue> listarTodo() {
        return venueDao.listarVenue();
    }

    public Venue update (Venue venue){
        if (venue.getNombre() == null || venue.getNombre().isBlank()){
            throw new NotNullValueException("El nombre no puede estar vacio..");
        }

        if (venueDao.existeNombre(venue.getNombre())) {
            throw new NotDuplicateException("Ya existe un lugar registrado con ese nombre.");
        }

        if (venueDao.existeNombreExcluirId(venue.getNombre(), venue.getId())) {
            throw new NotDuplicateException("Ya existe otro lugar registrado con ese nombre.");

        }

        boolean actualizado = false;
        for (int i = 0; i < venueDao.listarVenue().size(); i++) {
            if (venueDao.listarVenue().get(i).getId().equals(venue.getId())) {
                venueDao.listarVenue().set(i, venue);
                actualizado =true;
                break;
            }
        }

        if (!actualizado) {
            throw new VenueNotFoundException("No se puede actualizar porque no existe el lugar con ID: " + venue.getId());
        }

        venueDao.update(venue);
        return venue;
    }

    public String eliminar(Long id) {
        if (!venueDao.existeId(id)){
            throw new InvaliDataException("No se puede eliminar porque no existe el lugar con ID: " + id);
        }

        venueDao.deleteById(id);
        return "Lugar eliminado exitosamente...";
    }


}
