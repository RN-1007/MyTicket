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
import com.travelapp.travel_app.model.AttractionTicket; // <-- IMPORT BARU
import com.travelapp.travel_app.model.Category;
import com.travelapp.travel_app.model.Hotel;
import com.travelapp.travel_app.model.HotelRoom; // <-- IMPORT BARU
import com.travelapp.travel_app.model.Transport;
import com.travelapp.travel_app.model.TransportTicket; // <-- IMPORT BARU
import com.travelapp.travel_app.repository.transport.TransportProviderRepository;
import com.travelapp.travel_app.service.attraction.AttractionService;
import com.travelapp.travel_app.service.attraction.AttractionTicketService; // <-- IMPORT BARU
import com.travelapp.travel_app.service.hotel.HotelRoomService; // <-- IMPORT BARU
import com.travelapp.travel_app.service.hotel.HotelService;
import com.travelapp.travel_app.service.transport.TransportService;
import com.travelapp.travel_app.service.transport.TransportTicketService; // <-- IMPORT BARU

@Controller
@RequestMapping("/admin")
public class AdminController {

    // (Dependency injection Anda sudah benar)
    @Autowired private HotelService hotelService;
    @Autowired private TransportService transportService;
    @Autowired private AttractionService attractionService;
    @Autowired private TransportProviderRepository transportProviderRepository; 

    // --- SERVICE BARU UNTUK SUB-ITEM ---
    @Autowired private HotelRoomService hotelRoomService;
    @Autowired private TransportTicketService transportTicketService;
    @Autowired private AttractionTicketService attractionTicketService;

    // (Dashboard)
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("hotelCount", hotelService.getHotelCount());
        model.addAttribute("transportCount", transportService.getTransportCount());
        model.addAttribute("attractionCount", attractionService.getAttractionCount());
        return "admin/dashboard"; 
    }

    // == CRUD UNTUK HOTEL == (Tidak berubah)
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
    
    // == CRUD UNTUK HOTEL ROOMS == (BARU)
    @GetMapping("/hotel-rooms")
    public String showHotelRoomList(Model model) {
        model.addAttribute("hotelRooms", hotelRoomService.findAll());
        return "admin/hotel/hotel-rooms";
    }

    @GetMapping("/hotel-rooms/add")
    public String showAddHotelRoomForm(Model model) {
        model.addAttribute("hotelRoom", new HotelRoom());
        model.addAttribute("allHotels", hotelService.getAllHotels()); // Untuk dropdown
        model.addAttribute("pageTitle", "Add New Hotel Room");
        return "admin/hotel/hotel-room-form";
    }

    @PostMapping("/hotel-rooms/save")
    public String saveHotelRoom(@ModelAttribute("hotelRoom") HotelRoom hotelRoom) {
        hotelRoomService.save(hotelRoom); 
        return "redirect:/admin/hotel-rooms";
    }

    @GetMapping("/hotel-rooms/edit/{id}")
    public String showEditHotelRoomForm(@PathVariable("id") Integer id, Model model) {
        HotelRoom hotelRoom = hotelRoomService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid hotel room Id:" + id));
        model.addAttribute("hotelRoom", hotelRoom);
        model.addAttribute("allHotels", hotelService.getAllHotels()); // Untuk dropdown
        model.addAttribute("pageTitle", "Edit Hotel Room");
        return "admin/hotel/hotel-room-form";
    }

    @GetMapping("/hotel-rooms/delete/{id}")
    public String deleteHotelRoom(@PathVariable("id") Integer id) {
        hotelRoomService.deleteById(id); 
        return "redirect:/admin/hotel-rooms";
    }


    // == CRUD UNTUK TRANSPORT == (Tidak berubah)
    @GetMapping("/transports")
    public String showTransportList(Model model) {
        model.addAttribute("transports", transportService.getAllTransports());
        return "admin/transport/transport"; 
    }
    // ... (method add, save, edit, delete untuk transport lainnya) ...
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


    // == CRUD UNTUK TRANSPORT TICKETS == (BARU)
    @GetMapping("/transport-tickets")
    public String showTransportTicketList(Model model) {
        model.addAttribute("transportTickets", transportTicketService.findAll());
        return "admin/transport/transport-tickets";
    }

    @GetMapping("/transport-tickets/add")
    public String showAddTransportTicketForm(Model model) {
        model.addAttribute("transportTicket", new TransportTicket());
        model.addAttribute("allTransports", transportService.getAllTransports()); // Untuk dropdown
        model.addAttribute("pageTitle", "Add New Transport Ticket");
        return "admin/transport/transport-ticket-form";
    }

    @PostMapping("/transport-tickets/save")
    public String saveTransportTicket(@ModelAttribute("transportTicket") TransportTicket ticket) {
        transportTicketService.save(ticket);
        return "redirect:/admin/transport-tickets";
    }

    @GetMapping("/transport-tickets/edit/{id}")
    public String showEditTransportTicketForm(@PathVariable("id") Integer id, Model model) {
        TransportTicket ticket = transportTicketService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transport ticket Id:" + id));
        model.addAttribute("transportTicket", ticket);
        model.addAttribute("allTransports", transportService.getAllTransports()); // Untuk dropdown
        model.addAttribute("pageTitle", "Edit Transport Ticket");
        return "admin/transport/transport-ticket-form";
    }

    @GetMapping("/transport-tickets/delete/{id}")
    public String deleteTransportTicket(@PathVariable("id") Integer id) {
        transportTicketService.deleteById(id);
        return "redirect:/admin/transport-tickets";
    }


    // == CRUD UNTUK ATTRACTION == (Tidak berubah)
    @GetMapping("/attractions")
    public String showAttractionList(Model model) {
        model.addAttribute("attractions", attractionService.getAllAttractions());
        return "admin/attraction/attraction"; 
    }
    // ... (method add, save, edit, delete untuk attraction lainnya) ...
    @GetMapping("/attractions/add")
    public String showAddAttractionForm(Model model) {
        model.addAttribute("attraction", new Attraction());
        model.addAttribute("allCategories", Category.values()); 
        model.addAttribute("pageTitle", "Add New Attraction");
        return "admin/attraction/attraction-form"; 
    }
    @PostMapping("/attractions/save")
    public String saveAttraction(@ModelAttribute("attraction") Attraction attraction) {
        attractionService.saveAttraction(attraction);
        return "redirect:/admin/attractions"; 
    }
    @GetMapping("/attractions/edit/{id}")
    public String showEditAttractionForm(@PathVariable("id") Integer id, Model model) {
        Attraction attraction = attractionService.getAttractionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attraction Id:" + id));
        model.addAttribute("attraction", attraction);
        model.addAttribute("allCategories", Category.values()); 
        model.addAttribute("pageTitle", "Edit Attraction");
        return "admin/attraction/attraction-form"; 
    }
    @GetMapping("/attractions/delete/{id}")
    public String deleteAttraction(@PathVariable("id") Integer id) {
        attractionService.deleteAttraction(id);
        return "redirect:/admin/attractions"; 
    }

    // == CRUD UNTUK ATTRACTION TICKETS == (BARU)
    @GetMapping("/attraction-tickets")
    public String showAttractionTicketList(Model model) {
        model.addAttribute("attractionTickets", attractionTicketService.findAll());
        return "admin/attraction/attraction-tickets";
    }

    @GetMapping("/attraction-tickets/add")
    public String showAddAttractionTicketForm(Model model) {
        model.addAttribute("attractionTicket", new AttractionTicket());
        model.addAttribute("allAttractions", attractionService.getAllAttractions()); // Untuk dropdown
        model.addAttribute("pageTitle", "Add New Attraction Ticket");
        return "admin/attraction/attraction-ticket-form";
    }

    @PostMapping("/attraction-tickets/save")
    public String saveAttractionTicket(@ModelAttribute("attractionTicket") AttractionTicket ticket) {
        attractionTicketService.save(ticket);
        return "redirect:/admin/attraction-tickets";
    }

    @GetMapping("/attraction-tickets/edit/{id}")
    public String showEditAttractionTicketForm(@PathVariable("id") Integer id, Model model) {
        AttractionTicket ticket = attractionTicketService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attraction ticket Id:" + id));
        model.addAttribute("attractionTicket", ticket);
        model.addAttribute("allAttractions", attractionService.getAllAttractions()); // Untuk dropdown
        model.addAttribute("pageTitle", "Edit Attraction Ticket");
        return "admin/attraction/attraction-ticket-form";
    }

    @GetMapping("/attraction-tickets/delete/{id}")
    public String deleteAttractionTicket(@PathVariable("id") Integer id) {
        attractionTicketService.deleteById(id);
        return "redirect:/admin/attraction-tickets";
    }
}