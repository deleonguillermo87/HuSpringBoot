package com.events.event_Venue.service;

import com.events.event_Venue.dao.EventDao;
import com.events.event_Venue.model.Event;
import org.springframework.stereotype.Service;
import com.events.event_Venue.exception.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EventService {
    private final EventDao eventDao;


    public EventService(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public Event create(Event event){
        if (event.getId() != null &&  event.getId() > 0 && eventDao.existeId(event.getId())){
            throw new EventNotFoundException("El id ya existe...");
        }
        if (event.getNombre() == null || event.getNombre().isBlank()){
            throw new NotNullValueException("El nombre no puede estar vacio..");
        }
        if (event.getFecha() == null || event.getFecha().isBefore(LocalDateTime.now()) ){
            throw new NotNullFechaException("La fecha no puedes estar vacia y El evento no puede programarse para una fecha o hora que ya pasó...");
        }
        if (eventDao.existeNombreFecha(event.getNombre(), event.getFecha())){
            throw new NotDuplicateException("Ya existe un evento con ese nombre programado para esa hora.");
        }

        if (event.getDescripcion() == null || event.getDescripcion().isBlank()){
            throw new NotNullValueException("La descripcion no puede estar vacia...");
        }

        return eventDao.guardar(event);
    }

    public List<Event> ListarEvents (){
        return eventDao.listarTodo();
    }

    public Event buscarFecha (LocalDateTime fecha){
        return eventDao.buscarFecha(fecha)
                .orElseThrow(() -> new FechaNotFoundException("No se encontró ningún evento para la fecha: \" + fecha"));

    }

    public Event actualizar(Event event){
        if (event.getFecha().isBefore(LocalDateTime.now())){
            throw new InvaliDataException("La nueva fecha del evento no puede ser en el pasado.");
        }

        if (eventDao.existeNombreFechaExcluirId(event.getNombre(), event.getFecha(),event.getId())){
            throw new NotDuplicateException("Ya existe otro evento con ese nombre programado para esa hora.");
        }
        boolean actualizado = false;
        for (int i = 0; i < eventDao.listarTodo().size(); i++){
            if (eventDao.listarTodo().get(i).getId().equals(event.getId()))
                eventDao.listarTodo().set(i, event);
                actualizado = true;
                break;
        }
        if (!actualizado){
            throw  new EventNotFoundException("No se puede actualizar porque no existe el evento con ID: " + event.getId());
        }
        eventDao.actualizar(event);
        return event;

    }

    public String eliminar (Long id){
        if (!eventDao.existeId(id)) {
            throw new EventNotFoundException("No se puede eliminar porque no existe el evento con ID: " + id);
        }

        eventDao.eliminar(id);
        return "Evento eliminado exitosamente";
    }


}

