package com.travelapp.travel_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; 
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute; 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.travelapp.travel_app.model.Attraction;
import com.travelapp.travel_app.model.Category;
import com.travelapp.travel_app.model.Hotel;
import com.travelapp.travel_app.model.Transport;
import com.travelapp.travel_app.repository.transport.TransportProviderRepository;
import com.travelapp.travel_app.service.AttractionService;
import com.travelapp.travel_app.service.HotelService;
import com.travelapp.travel_app.service.TransportService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // (Dependency injection Anda sudah benar)
    @Autowired private HotelService hotelService;
    @Autowired private TransportService transportService;
    @Autowired private AttractionService attractionService;
    @Autowired private TransportProviderRepository transportProviderRepository; 

    // (Dashboard dan Hotel tidak berubah)
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("hotelCount", hotelService.getHotelCount());
        model.addAttribute("transportCount", transportService.getTransportCount());
        model.addAttribute("attractionCount", attractionService.getAttractionCount());
        return "admin/dashboard"; 
    }

    // == CRUD UNTUK HOTEL ==
    @GetMapping("/hotels")
    public String showHotelList(Model model) {
        model.addAttribute("hotels", hotelService.getAllHotels());
        return "admin/hotel/hotels"; 
    }
    @GetMapping("/hotels/add")
    public String showAddHotelForm(Model model) {
        model.addAttribute("hotel", new Hotel());
        model.addAttribute("pageTitle", "Add New Hotel");
        return "admin/hotel/hotel-form"; 
    }
    @PostMapping("/hotels/save")
    public String saveHotel(@ModelAttribute("hotel") Hotel hotel) {
        hotelService.saveHotel(hotel); 
        return "redirect:/admin/hotels"; 
    }
    @GetMapping("/hotels/edit/{id}")
    public String showEditHotelForm(@PathVariable("id") Integer id, Model model) {
        Hotel hotel = hotelService.getHotelById(id) 
                .orElseThrow(() -> new IllegalArgumentException("Invalid hotel Id:" + id));
        model.addAttribute("hotel", hotel);
        model.addAttribute("pageTitle", "Edit Hotel");
        return "admin/hotel/hotel-form"; 
    }
    @GetMapping("/hotels/delete/{id}")
    public String deleteHotel(@PathVariable("id") Integer id) {
        hotelService.deleteHotel(id); 
        return "redirect:/admin/hotels"; 
    }
    
    // (Transport tidak berubah)
    // == CRUD UNTUK TRANSPORT ==
    @GetMapping("/transports")
    public String showTransportList(Model model) {
        model.addAttribute("transports", transportService.getAllTransports());
        return "admin/transport/transport"; 
    }
    @GetMapping("/transports/add")
    public String showAddTransportForm(Model model) {
        model.addAttribute("transport", new Transport());
        model.addAttribute("allProviders", transportProviderRepository.findAll()); 
        model.addAttribute("pageTitle", "Add New Transport");
        return "admin/transport/transport-form"; 
    }
    @PostMapping("/transports/save")
    public String saveTransport(@ModelAttribute("transport") Transport transport) {
        transportService.saveTransport(transport);
        return "redirect:/admin/transports"; 
    }
    @GetMapping("/transports/edit/{id}")
    public String showEditTransportForm(@PathVariable("id") Integer id, Model model) {
        Transport transport = transportService.getTransportById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transport Id:" + id));
        model.addAttribute("transport", transport);
        model.addAttribute("allProviders", transportProviderRepository.findAll()); 
        model.addAttribute("pageTitle", "Edit Transport");
        return "admin/transport/transport-form"; 
    }
    @GetMapping("/transports/delete/{id}")
    public String deleteTransport(@PathVariable("id") Integer id) {
        transportService.deleteTransport(id);
        return "redirect:/admin/transports"; 
    }

    // == CRUD UNTUK ATTRACTION (PERBAIKAN) ==
    
    @GetMapping("/attractions")
    public String showAttractionList(Model model) {
        model.addAttribute("attractions", attractionService.getAllAttractions());
        // **PERBAIKAN 1: Menghilangkan typo 'a' berdasarkan gambar**
        return "admin/attraction/attraction"; 
    }

    @GetMapping("/attractions/add")
    public String showAddAttractionForm(Model model) {
        model.addAttribute("attraction", new Attraction());
        model.addAttribute("allCategories", Category.values()); 
        model.addAttribute("pageTitle", "Add New Attraction");
        return "admin/attraction/attraction-form"; // (Path ini sudah benar)
    }

    @PostMapping("/attractions/save")
    public String saveAttraction(@ModelAttribute("attraction") Attraction attraction) {
        attractionService.saveAttraction(attraction);
        // **PERBAIKAN 2: Redirect harus ke URL, bukan folder. Tambahkan 's'.**
        return "redirect:/admin/attractions"; 
    }

    @GetMapping("/attractions/edit/{id}")
    public String showEditAttractionForm(@PathVariable("id") Integer id, Model model) {
        Attraction attraction = attractionService.getAttractionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attraction Id:" + id));
        model.addAttribute("attraction", attraction);
        model.addAttribute("allCategories", Category.values()); 
        model.addAttribute("pageTitle", "Edit Attraction");
        return "admin/attraction/attraction-form"; // (Path ini sudah benar)
    }

    @GetMapping("/attractions/delete/{id}")
    public String deleteAttraction(@PathVariable("id") Integer id) {
        attractionService.deleteAttraction(id);
        // **PERBAIKAN 3: Redirect harus ke URL, bukan folder. Tambahkan 's'.**
        return "redirect:/admin/attractions"; 
    }
}