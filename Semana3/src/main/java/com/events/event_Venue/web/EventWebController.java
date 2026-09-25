package com.events.event_Venue.web;

import com.events.event_Venue.model.Event;
import com.events.event_Venue.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/admin/event")
@Controller
public class EventWebController {
    private final EventService eventService;

    public EventWebController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public String crear(@ModelAttribute("event") Event event, RedirectAttributes flash) {
        try {
            eventService.create(event);
            flash.addFlashAttribute("message", "Evento creado exitosamente");
            return "redirect:/admin/event";
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "Error al crear el evento: " + ex.getMessage());
            return "redirect:/admin/event/nuevo";
        }
    }

    @GetMapping("/nuevo")
    public String newEvent(Model model) {
        model.addAttribute("event", new Event());
        return "eventoCrear";
    }

    @GetMapping
    public String listarTodo(Model model) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Event> page = eventService.ListarEvents(pageable);
        model.addAttribute("events", page.getContent());

        return "eventosList";
    }

    @GetMapping("/{id}")
    public String buscarId(@PathVariable("id") Long id, Model model) {
        model.addAttribute("event", eventService.buscarId(id));
        return "eventosDelete";
    }

    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable("id") Long id, Event eventActualizar, RedirectAttributes flash) {
        try {
            eventActualizar.setId(id);
            eventService.actualizar(id, eventActualizar);
            flash.addFlashAttribute("message", "Evento actualizado con éxito");
            return "redirect:/admin/event";
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "No se actualizó el evento: " + ex.getMessage());
            return "redirect:/admin/event/" + id;
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarEvent(@PathVariable("id") Long id, RedirectAttributes flash) {
        try {
            eventService.eliminar(id);
            flash.addFlashAttribute("message", "Evento eliminado con éxito");
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "No se logró eliminar el evento: " + ex.getMessage());
        }
        return "redirect:/admin/event";
    }
}
