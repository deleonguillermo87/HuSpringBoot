package com.events.event_Venue.dao;

import com.events.event_Venue.model.Event;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EventDao {
    private final  List<Event> events = new ArrayList<>();
    private final AtomicLong counterId = new AtomicLong(1);

    public Event guardar(Event event){
        if(event.getId() == null || event.getId() <= 0){
            event.setId(counterId.getAndIncrement());
        }else {
            if (event.getId() >= counterId.get())
            {
                counterId.set(event.getId() + 1);
            }
        }

        events.add(event);
        return event;
    }

    public List<Event> listarTodo() {
        return events;
    }

    public Optional<Event> buscarFecha(LocalDateTime fecha) {
        return events.stream()
                .filter(e -> e.getFecha().equals(fecha))
                .findAny();
    }

    public boolean existeNombreFecha(String nombre, LocalDateTime fecha){
        return events.stream()
                .anyMatch(e-> e.getNombre().equals(nombre)
                && e.getFecha().equals(fecha));
    }

    // eliminar
    public boolean existeId(Long id){
        return events.stream()
                .anyMatch(e -> e.getId().equals(id));
    }
    // actualizar
    public boolean existeNombreFechaExcluirId(String nombre, LocalDateTime fecha, Long id) {
        return events.stream()
                .anyMatch(e -> e.getNombre().equals(nombre)
                        && e.getFecha().equals(fecha) && !e.getId().equals(id));
    }
    public void actualizar(Event event){
        for (int i = 0; i < events.size(); i++){
            if (events.get(i).getId().equals(event.getId())){
                events.set(i, event);
                return;
            }
        }
    }

    public String eliminar(Long id){
        events.removeIf(e -> e.getId().equals(id));

        return "Evento eliminado exitosamente";
    }
}
