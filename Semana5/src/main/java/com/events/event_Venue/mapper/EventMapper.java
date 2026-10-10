package com.events.event_Venue.mapper;

import com.events.event_Venue.dto.EventRequest;
import com.events.event_Venue.dto.EventResponse;
import com.events.event_Venue.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event toEntity(EventRequest request);

    EventResponse toResponse(Event event);

    default void updateEntity(EventRequest request, @MappingTarget Event entity) {
        entity.setNombre(request.getNombre());
        entity.setFecha(request.getFecha());
        entity.setDescripcion(request.getDescripcion());
    }
}
