package com.travelapp.travel_app.controller;

import com.travelapp.travel_app.model.Hotel;
import com.travelapp.travel_app.service.AttractionService;
import com.travelapp.travel_app.service.HotelService;
import com.travelapp.travel_app.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Sekarang inject Service, bukan Repository
    @Autowired private HotelService hotelService;
    @Autowired private TransportService transportService;
    @Autowired private AttractionService attractionService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Memanggil service untuk mengambil data
        model.addAttribute("hotelCount", hotelService.getHotelCount());
        model.addAttribute("transportCount", transportService.getTransportCount());
        model.addAttribute("attractionCount", attractionService.getAttractionCount());
        return "admin/dashboard"; 
    }

    // == CRUD UNTUK HOTEL ==

    @GetMapping("/hotels")
    public String showHotelList(Model model) {
        model.addAttribute("hotels", hotelService.getAllHotels());
        return "admin/hotels"; 
    }

    @GetMapping("/hotels/add")
    public String showAddHotelForm(Model model) {
        model.addAttribute("hotel", new Hotel());
        model.addAttribute("pageTitle", "Add New Hotel");
        return "admin/hotel-form"; 
    }

    @PostMapping("/hotels/save")
    public String saveHotel(@ModelAttribute("hotel") Hotel hotel) {
        hotelService.saveHotel(hotel); // Menggunakan service untuk menyimpan
        return "redirect:/admin/hotels";
    }

    @GetMapping("/hotels/edit/{id}")
    public String showEditHotelForm(@PathVariable("id") Integer id, Model model) {
        Hotel hotel = hotelService.getHotelById(id) // Menggunakan service
                .orElseThrow(() -> new IllegalArgumentException("Invalid hotel Id:" + id));
        model.addAttribute("hotel", hotel);
        model.addAttribute("pageTitle", "Edit Hotel");
        return "admin/hotel-form"; 
    }

    @GetMapping("/hotels/delete/{id}")
    public String deleteHotel(@PathVariable("id") Integer id) {
        hotelService.deleteHotel(id); // Menggunakan service
        return "redirect:/admin/hotels";
    }
    
    // (Anda bisa menambahkan method serupa untuk Transport dan Attraction
    //  dengan membuat service-nya terlebih dahulu)
}