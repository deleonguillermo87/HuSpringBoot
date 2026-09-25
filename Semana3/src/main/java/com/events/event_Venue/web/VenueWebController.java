package com.events.event_Venue.web;

import com.events.event_Venue.model.Venue;
import com.events.event_Venue.service.VenueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/admin/venues")
@Controller
public class VenueWebController {
    private VenueService venueService;

    public VenueWebController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute("venue") Venue venue, RedirectAttributes flash){
        try {
            venueService.create(venue);
            flash.addFlashAttribute("message", "Lugar creado exitosamente..");
            return "redirect:/admin/venues/listar";
        } catch (RuntimeException ex){
            flash.addFlashAttribute("error", "No se ha podido registrar el lugar: " + ex.getMessage());
            return "redirect:/admin/venues/nuevo";
        }
    }

    @GetMapping("/nuevo")
    public String newVenue(Model model){
        model.addAttribute("venue", new Venue());
        return "venueCrear";
    }

    @GetMapping("/listar")
    public String listarVenues(Model model){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Venue> page = venueService.listarTodo(pageable);
        model.addAttribute("venue", page.getContent());

        return "venueList";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable("id") Long id, Model model){
        try {
            Venue venue = venueService.buscarPorId(id);
            model.addAttribute("venue", venue);
        } catch (RuntimeException ex) {
            model.addAttribute("venue", null);
            model.addAttribute("error", "No se encontró el lugar: " + ex.getMessage());
        }
        return "venueDetalle";
    }

    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable("id") Long id, RedirectAttributes flash, Venue venue){
        try {
            venueService.update(id, venue);
            flash.addFlashAttribute("message", "Lugar actualizado correctamente");
            return "redirect:/admin/venues/listar";
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "No se pudo actualizar el lugar: " + ex.getMessage());
            return "redirect:/admin/venues/" + id;
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes flash){
        try {
            venueService.eliminar(id);
            flash.addFlashAttribute("message", "Lugar eliminado correctamente");
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "No se logró eliminar el lugar: " + ex.getMessage());
        }
        return "redirect:/admin/venues/listar";
    }

}