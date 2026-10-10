package com.events.event_Venue.dao;

import com.events.event_Venue.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueDao extends JpaRepository<Venue, Long> {
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdNot(String nombre, Long id);
}
