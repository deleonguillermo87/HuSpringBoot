package com.events.event_Venue.service;


import com.events.event_Venue.dao.VenueDao;
import com.events.event_Venue.exception.InvaliDataException;
import com.events.event_Venue.exception.NotDuplicateException;
import com.events.event_Venue.exception.NotNullValueException;
import com.events.event_Venue.exception.VenueNotFoundException;
import com.events.event_Venue.model.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueService {

    private final VenueDao venueDao;

    public VenueService(VenueDao venueDao) {
        this.venueDao = venueDao;
    }

    public Venue create(Venue venue){
        if (venue.getId() != null && venue.getId() > 0 && venueDao.existsById(venue.getId())){
            throw new VenueNotFoundException("El id ya existe...");
        }

        if (venue.getNombre() == null || venue.getNombre().isBlank()){
            throw new NotNullValueException("El nombre no puede estar vacio..");
        }

        if (venueDao.existsByNombre(venue.getNombre())) {
            throw new NotDuplicateException("Ya existe un lugar registrado con ese nombre.");
        }

        if (venue.getDireccion() == null || venue.getDireccion().isBlank()){
            throw new NotNullValueException("La direccion no puede estar vacia...");
        }
        if (venue.getCapacidad() == null ||  venue.getCapacidad() <= 0){
            throw new VenueNotFoundException("Ingrese un numero mayor a 0");
        }

        return venueDao.save(venue);

    }

    public Page<Venue> listarTodo(Pageable pageable) {
        return venueDao.findAll(pageable);
    }

    public Venue buscarPorId(Long id) {
        return venueDao.findById(id)
                .orElseThrow(() -> new VenueNotFoundException("No se encontro un lugar con ID: " + id));
    }

    public Venue update (Venue venue){
        if (venue.getNombre() == null || venue.getNombre().isBlank()){
            throw new NotNullValueException("El nombre no puede estar vacio..");
        }

        if (venueDao.existsByNombre(venue.getNombre())) {
            throw new NotDuplicateException("Ya existe un lugar registrado con ese nombre.");
        }

        if (venueDao.existsByNombreAndIdNot(venue.getNombre(), venue.getId())) {
            throw new NotDuplicateException("Ya existe otro lugar registrado con ese nombre.");

        }

        venueDao.save(venue);
        return venue;
    }

    public String eliminar(Long id) {
        if (!venueDao.existsById(id)){
            throw new InvaliDataException("No se puede eliminar porque no existe el lugar con ID: " + id);
        }

        venueDao.deleteById(id);
        return "Lugar eliminado exitosamente...";
    }


}