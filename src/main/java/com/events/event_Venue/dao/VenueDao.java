package com.events.event_Venue.dao;

import com.events.event_Venue.model.Venue;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class VenueDao {
    private final List<Venue> venues = new ArrayList<>() ;
    private final AtomicLong counterId = new AtomicLong(1);

    public Venue guardarVenue(Venue venue){
        if(venue.getId() == null || venue.getId() <= 0){
            venue.setId(counterId.getAndIncrement());
        }else {
            if (venue.getId() >= counterId.get())
            {
                counterId.set(venue.getId() + 1);
            }
        }
        venues.add(venue) ;
        return venue ;
    }
    public List<Venue> listarVenue(){
        return venues ;
    }
    public Optional<Venue> buscarDireccion(String direccion){
        return venues.stream()
                .filter(e -> e.getDireccion().equals(direccion))
                .findFirst();
    }
    public boolean existeId(Long id){
        return venues.stream()
                .anyMatch(e -> e.getId().equals(id));
    }
    public boolean existeNombre(String nombre) {
        return venues.stream()
                .anyMatch(v -> v.getNombre().equals(nombre));
    }
    public boolean existeNombreExcluirId(String nombre, Long id){
        return venues.stream()
                .anyMatch(v -> v.getNombre().equals(nombre) && !v.getId().equals(id));
    }
    public void update (Venue venueUpdate){
        for(int i = 0; i < venues.size(); i++){
            if(venues.get(i).getId().equals(venueUpdate.getId())){
                venues.set(i, venueUpdate) ;
                return;
            }
        }
    }
    public void deleteById(Long id){
        venues.removeIf(v -> v.getId().equals(id)) ;
    }
}
