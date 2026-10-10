package com.events.event_Venue.dao;


import com.events.event_Venue.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EventDao extends JpaRepository<Event, Long> {
    boolean existsByNombreAndFecha(String nombre, LocalDateTime fecha);


}
