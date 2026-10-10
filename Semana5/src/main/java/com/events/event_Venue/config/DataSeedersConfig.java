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
    public CommandLineRunner seedData(EventDao eventDao, VenueDao venueDao) {
        return args -> {
            try {
                if (eventDao.count() == 0) {
                    eventDao.save(new Event(null, "Conferencia de Tecnología", LocalDateTime.now().plusDays(7), "Evento de innovación y tecnología."));
                    eventDao.save(new Event(null, "Meetup de Java", LocalDateTime.now().plusDays(12), "Reunion de desarrolladores Java y Spring Boot."));
                }

                if (venueDao.count() == 0) {
                    venueDao.save(new Venue(null, "Centro Eventify", "Av. Siempre Viva 123", 250L));
                    venueDao.save(new Venue(null, "Auditorio Norte", "Cra. 8 # 15-40", 400L));
                }
            } catch (Exception e) {
                System.err.println("No se pudieron cargar los datos iniciales. Verifica que PostgreSQL esté levantado: " + e.getMessage());
            }
        };
    }
}
