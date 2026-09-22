package com.events.event_Venue.config;

import com.events.event_Venue.dao.EventDao;
import com.events.event_Venue.dao.VenueDao;
import com.events.event_Venue.model.Event;
import com.events.event_Venue.model.Venue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataSeedersConfig {

    @Bean
    CommandLineRunner seedVenues(VenueDao venueDao) {
        return args -> {
            venueDao.guardarVenue(new Venue(1L, "Teatro amira de la rosa", "Barranquilla", 1000L));
            venueDao.guardarVenue(new Venue(2L, "Centro de eventos puerta de oro", "Barranquilla", 200L));
            venueDao.guardarVenue(new Venue(3L, "Coliseo elias chegwin", "Barranquilla",4000L));
        };
    }

    @Bean
    CommandLineRunner seedEvents(EventDao eventDao) {
        return args -> {
            eventDao.guardar(new Event(1L, "Conferencia spring boot", LocalDateTime.of(2026, 10, 15, 9, 0), "Evento tecnico sobre desarrollo de backend"));
            eventDao.guardar(new Event(2L, "Meetup de java", LocalDateTime.of(2026, 10, 15, 18, 30), "Networking para desarrolladores"));
            eventDao.guardar(new Event(3L, "Hackathon backend", LocalDateTime.of(2026, 11, 15, 8, 0), "Competencia de programacion de 24 horas"));
        };
    }
}
