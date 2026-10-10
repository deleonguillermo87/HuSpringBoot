package com.events.event_Venue.web;

import com.events.event_Venue.dto.EventRequest;
import com.events.event_Venue.dto.EventResponse;
import com.events.event_Venue.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String crear(@Valid @ModelAttribute("event") EventRequest request, BindingResult bindingResult,
                       RedirectAttributes flash, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("event", request);
            return "eventoCrear";
        }
        try {
            eventService.create(request);
            flash.addFlashAttribute("message", "Evento creado exitosamente");
            return "redirect:/admin/event";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("event", request);
            return "eventoCrear";
        }
    }

    @GetMapping("/nuevo")
    public String newEvent(Model model) {
        model.addAttribute("event", new EventRequest());
        return "eventoCrear";
    }

    @GetMapping
    public String listarTodo(Model model) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<EventResponse> page = eventService.ListarEvents(pageable);
        model.addAttribute("events", page.getContent());

        return "eventosList";
    }

    @GetMapping("/{id}")
    public String buscarId(@PathVariable("id") Long id, Model model) {
        model.addAttribute("event", eventService.buscarId(id));
        return "eventosDelete";
    }

    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable("id") Long id, @Valid @ModelAttribute("event") EventRequest request,
                            BindingResult bindingResult, RedirectAttributes flash, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("event", request);
            return "eventosDelete";
        }
        try {
            eventService.actualizar(id, request);
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
