package com.events.event_Venue.service;

import com.events.event_Venue.dao.EventDao;
import com.events.event_Venue.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.events.event_Venue.exception.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    private final EventDao eventDao;


    public EventService(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public Event create(Event event){
        if (event.getId() != null &&  event.getId() > 0 && eventDao.existsById(event.getId())){
            throw new EventNotFoundException("El id ya existe...");
        }
        if (event.getNombre() == null || event.getNombre().isBlank()){
            throw new NotNullValueException("El nombre no puede estar vacio..");
        }
        if (event.getFecha() == null || event.getFecha().isBefore(LocalDateTime.now()) ){
            throw new NotNullFechaException("La fecha no puedes estar vacia y El evento no puede programarse para una fecha o hora que ya pasó...");
        }
        if (eventDao.existsByNombreAndFecha(event.getNombre(), event.getFecha())){
            throw new NotDuplicateException("Ya existe un evento con ese nombre programado para esa hora.");
        }

        if (event.getDescripcion() == null || event.getDescripcion().isBlank()){
            throw new NotNullValueException("La descripcion no puede estar vacia...");
        }

        return eventDao.save(event);
    }

    public Page<Event> ListarEvents(Pageable pageable){
        return eventDao.findAll(pageable);
    }

    public Event buscarId(Long id) {
        return eventDao.findById(id)
                .orElseThrow(() -> new EventNotFoundException("No existe un evento con id: " + id));
    }

    public Event actualizar(Long id, Event eventActualizar){
        Event event = eventDao.findById(id)
                .orElseThrow(() -> new EventNotFoundException("No existe un evento con id: " + id));

        event.setNombre(eventActualizar.getNombre());
        event.setFecha(eventActualizar.getFecha());
        event.setDescripcion(eventActualizar.getDescripcion());

        return eventDao.save(event);
    }



    public String eliminar (Long id){
        if (!eventDao.existsById(id)) {
            throw new EventNotFoundException("No se puede eliminar porque no existe el evento con ID: " + id);
        }

        eventDao.deleteById(id);
        return "Evento eliminado exitosamente";
    }


}

