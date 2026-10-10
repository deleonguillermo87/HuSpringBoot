package com.events.event_Venue.web;

import com.events.event_Venue.dto.VenueRequest;
import com.events.event_Venue.dto.VenueResponse;
import com.events.event_Venue.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/admin/venues")
@Controller
public class VenueWebController {
    private final VenueService venueService;

    public VenueWebController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute("venue") VenueRequest request, BindingResult bindingResult,
                       RedirectAttributes flash, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("venue", request);
            return "venueCrear";
        }
        try {
            venueService.create(request);
            flash.addFlashAttribute("message", "Lugar creado exitosamente..");
            return "redirect:/admin/venues/listar";
        } catch (RuntimeException ex){
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("venue", request);
            return "venueCrear";
        }
    }

    @GetMapping("/nuevo")
    public String newVenue(Model model){
        model.addAttribute("venue", new VenueRequest());
        return "venueCrear";
    }

    @GetMapping("/listar")
    public String listarVenues(Model model){
        Pageable pageable = PageRequest.of(0, 10);
        Page<VenueResponse> page = venueService.listarTodo(pageable);
        model.addAttribute("venue", page.getContent());

        return "venueList";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable("id") Long id, Model model){
        try {
            VenueResponse venue = venueService.buscarPorId(id);
            model.addAttribute("venue", venue);
        } catch (RuntimeException ex) {
            model.addAttribute("venue", null);
            model.addAttribute("error", "No se encontró el lugar: " + ex.getMessage());
        }
        return "venueDetalle";
    }

    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable("id") Long id, @Valid @ModelAttribute("venue") VenueRequest request,
                            BindingResult bindingResult, RedirectAttributes flash, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("venue", request);
            return "venueDetalle";
        }
        try {
            venueService.update(id, request);
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