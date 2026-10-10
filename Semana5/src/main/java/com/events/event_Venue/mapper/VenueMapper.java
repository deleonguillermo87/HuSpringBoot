package com.events.event_Venue.mapper;

import com.events.event_Venue.dto.VenueRequest;
import com.events.event_Venue.dto.VenueResponse;
import com.events.event_Venue.model.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VenueMapper {
    Venue toEntity(VenueRequest request);

    VenueResponse toResponse(Venue venue);

    default void updateEntity(VenueRequest request, @MappingTarget Venue entity) {
        entity.setNombre(request.getNombre());
        entity.setDireccion(request.getDireccion());
        entity.setCapacidad(request.getCapacidad());
    }
}
