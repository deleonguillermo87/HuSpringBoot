package com.events.event_Venue.service;

import com.events.event_Venue.dao.EventDao;
import com.events.event_Venue.dto.EventRequest;
import com.events.event_Venue.dto.EventResponse;
import com.events.event_Venue.exception.*;
import com.events.event_Venue.mapper.EventMapper;
import com.events.event_Venue.mapper.EventMapperImpl;
import com.events.event_Venue.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventService {
    private final EventDao eventDao;
    private EventMapper eventMapper = new EventMapperImpl();

    public EventService(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Autowired
    public void setEventMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
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

    public EventResponse create(EventRequest request){
        Event event = eventMapper.toEntity(request);
        Event savedEvent = create(event);
        return eventMapper.toResponse(savedEvent);
    }

    public Page<EventResponse> listarTodo(Pageable pageable){
        return eventDao.findAll(pageable).map(eventMapper::toResponse);
    }

    public Page<EventResponse> ListarEvents(Pageable pageable){
        return listarTodo(pageable);
    }

    public EventResponse buscarId(Long id) {
        Event event = eventDao.findById(id)
                .orElseThrow(() -> new EventNotFoundException("No existe un evento con id: " + id));
        return eventMapper.toResponse(event);
    }

    public Event actualizar(Long id, Event eventActualizar){
        Event event = eventDao.findById(id)
                .orElseThrow(() -> new EventNotFoundException("No existe un evento con id: " + id));

        event.setNombre(eventActualizar.getNombre());
        event.setFecha(eventActualizar.getFecha());
        event.setDescripcion(eventActualizar.getDescripcion());

        return eventDao.save(event);
    }

    public EventResponse actualizar(Long id, EventRequest request){
        Event event = eventDao.findById(id)
                .orElseThrow(() -> new EventNotFoundException("No existe un evento con id: " + id));
        eventMapper.updateEntity(request, event);
        return eventMapper.toResponse(eventDao.save(event));
    }

    public String eliminar (Long id){
        if (!eventDao.existsById(id)) {
            throw new EventNotFoundException("No se puede eliminar porque no existe el evento con ID: " + id);
        }

        eventDao.deleteById(id);
        return "Evento eliminado exitosamente";
    }

}

