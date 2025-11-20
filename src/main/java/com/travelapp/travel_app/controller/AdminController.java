package com.travelapp.travel_app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute; 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.travelapp.travel_app.model.Attraction;
import com.travelapp.travel_app.model.AttractionTicket; 
import com.travelapp.travel_app.model.Category;
import com.travelapp.travel_app.model.Hotel;
import com.travelapp.travel_app.model.HotelRoom; 
import com.travelapp.travel_app.model.Order;
import com.travelapp.travel_app.model.OrderDetail;
import com.travelapp.travel_app.model.Role;
import com.travelapp.travel_app.model.Transport;
import com.travelapp.travel_app.model.TransportTicket; 
import com.travelapp.travel_app.model.User;
import com.travelapp.travel_app.repository.UserRepository;
import com.travelapp.travel_app.repository.order.OrderRepository;
import com.travelapp.travel_app.repository.transport.TransportProviderRepository;
import com.travelapp.travel_app.service.attraction.AttractionService;
import com.travelapp.travel_app.service.attraction.AttractionTicketService; 
import com.travelapp.travel_app.service.hotel.HotelRoomService;
import com.travelapp.travel_app.service.hotel.HotelService;
import com.travelapp.travel_app.service.transport.TransportService;
import com.travelapp.travel_app.service.transport.TransportTicketService;
import com.travelapp.travel_app.service.user.UserService; // Import util kita
import com.travelapp.travel_app.util.FileUploadUtil;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private HotelService hotelService;
    @Autowired private TransportService transportService;
    @Autowired private AttractionService attractionService;
    @Autowired private TransportProviderRepository transportProviderRepository; 
    @Autowired private UserRepository userRepository; 
    @Autowired private OrderRepository orderRepository;
    @Autowired private HotelRoomService hotelRoomService;
    @Autowired private TransportTicketService transportTicketService;
    @Autowired private AttractionTicketService attractionTicketService;
    @Autowired private UserService userService;

    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("hotelCount", hotelService.getHotelCount());
        model.addAttribute("transportCount", transportService.getTransportCount());
        model.addAttribute("attractionCount", attractionService.getAttractionCount());
        model.addAttribute("userCount", userService.getUserCount());
        return "admin/dashboard"; 
    }

    // == CRUD HOTEL (UPDATED) ==
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
    public String saveHotel(@ModelAttribute("hotel") Hotel hotel, 
                            @RequestParam("imageFile") MultipartFile multipartFile) throws IOException {
        
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        
        // Logika: Jika upload gambar baru, pakai itu. Jika tidak, cek apakah ini edit data lama.
        if (!fileName.isEmpty()) {
            hotel.setImage(fileName);
            Hotel savedHotel = hotelService.saveHotel(hotel);
            String uploadDir = "hotel-photos/" + savedHotel.getHotelId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            // Jika edit dan tidak ganti gambar, kita perlu ambil gambar lama (jika ada)
            if (hotel.getHotelId() != null) {
                Hotel existingHotel = hotelService.getHotelById(hotel.getHotelId()).orElse(null);
                if (existingHotel != null) {
                    hotel.setImage(existingHotel.getImage());
                }
            }
            hotelService.saveHotel(hotel);
        }

        return "redirect:/admin/hotels"; 
    }
    
    @GetMapping("/hotels/edit/{id}")
    public String showEditHotelForm(@PathVariable("id") Integer id, Model model) {
        Hotel hotel = hotelService.getHotelById(id).orElseThrow(() -> new IllegalArgumentException("Invalid hotel Id:" + id));
        model.addAttribute("hotel", hotel);
        model.addAttribute("pageTitle", "Edit Hotel");
        return "admin/hotel/hotel-form"; 
    }
    @GetMapping("/hotels/delete/{id}")
    public String deleteHotel(@PathVariable("id") Integer id) {
        hotelService.deleteHotel(id); 
        return "redirect:/admin/hotels"; 
    }
    
    // == CRUD HOTEL ROOMS (Tetap) ==
    @GetMapping("/hotel-rooms")
    public String showHotelRoomList(Model model) {
        model.addAttribute("hotelRooms", hotelRoomService.findAll());
        return "admin/hotel/hotel-rooms";
    }
    @GetMapping("/hotel-rooms/add")
    public String showAddHotelRoomForm(Model model) {
        model.addAttribute("hotelRoom", new HotelRoom());
        model.addAttribute("allHotels", hotelService.getAllHotels());
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
        HotelRoom hotelRoom = hotelRoomService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + id));
        model.addAttribute("hotelRoom", hotelRoom);
        model.addAttribute("allHotels", hotelService.getAllHotels());
        model.addAttribute("pageTitle", "Edit Hotel Room");
        return "admin/hotel/hotel-room-form";
    }
    @GetMapping("/hotel-rooms/delete/{id}")
    public String deleteHotelRoom(@PathVariable("id") Integer id) {
        hotelRoomService.deleteById(id); 
        return "redirect:/admin/hotel-rooms";
    }

    // == CRUD TRANSPORT (UPDATED) ==
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
    public String saveTransport(@ModelAttribute("transport") Transport transport,
                                @RequestParam("imageFile") MultipartFile multipartFile) throws IOException {
        
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        
        if (!fileName.isEmpty()) {
            transport.setImage(fileName);
            Transport savedTransport = transportService.saveTransport(transport);
            String uploadDir = "transport-photos/" + savedTransport.getTransportId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (transport.getTransportId() != null) {
                Transport existing = transportService.getTransportById(transport.getTransportId()).orElse(null);
                if (existing != null) transport.setImage(existing.getImage());
            }
            transportService.saveTransport(transport);
        }
        return "redirect:/admin/transports"; 
    }
    
    @GetMapping("/transports/edit/{id}")
    public String showEditTransportForm(@PathVariable("id") Integer id, Model model) {
        Transport transport = transportService.getTransportById(id).orElseThrow(() -> new IllegalArgumentException("Invalid transport Id:" + id));
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

    // == CRUD TRANSPORT TICKETS (Tetap) ==
    @GetMapping("/transport-tickets")
    public String showTransportTicketList(Model model) {
        model.addAttribute("transportTickets", transportTicketService.findAll());
        return "admin/transport/transport-tickets";
    }
    @GetMapping("/transport-tickets/add")
    public String showAddTransportTicketForm(Model model) {
        model.addAttribute("transportTicket", new TransportTicket());
        model.addAttribute("allTransports", transportService.getAllTransports());
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
        TransportTicket ticket = transportTicketService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ticket Id:" + id));
        model.addAttribute("transportTicket", ticket);
        model.addAttribute("allTransports", transportService.getAllTransports());
        model.addAttribute("pageTitle", "Edit Transport Ticket");
        return "admin/transport/transport-ticket-form";
    }
    @GetMapping("/transport-tickets/delete/{id}")
    public String deleteTransportTicket(@PathVariable("id") Integer id) {
        transportTicketService.deleteById(id);
        return "redirect:/admin/transport-tickets";
    }

    // == CRUD ATTRACTION (UPDATED) ==
    @GetMapping("/attractions")
    public String showAttractionList(Model model) {
        model.addAttribute("attractions", attractionService.getAllAttractions());
        return "admin/attraction/attraction"; 
    }
    @GetMapping("/attractions/add")
    public String showAddAttractionForm(Model model) {
        model.addAttribute("attraction", new Attraction());
        model.addAttribute("allCategories", Category.values()); 
        model.addAttribute("pageTitle", "Add New Attraction");
        return "admin/attraction/attraction-form"; 
    }
    
    @PostMapping("/attractions/save")
    public String saveAttraction(@ModelAttribute("attraction") Attraction attraction,
                                 @RequestParam("imageFile") MultipartFile multipartFile) throws IOException {
        
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        
        if (!fileName.isEmpty()) {
            attraction.setImage(fileName);
            Attraction savedAttraction = attractionService.saveAttraction(attraction);
            String uploadDir = "attraction-photos/" + savedAttraction.getAttractionId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
             if (attraction.getAttractionId() != null) {
                Attraction existing = attractionService.getAttractionById(attraction.getAttractionId()).orElse(null);
                if (existing != null) attraction.setImage(existing.getImage());
            }
            attractionService.saveAttraction(attraction);
        }
        return "redirect:/admin/attractions"; 
    }
    
    @GetMapping("/attractions/edit/{id}")
    public String showEditAttractionForm(@PathVariable("id") Integer id, Model model) {
        Attraction attraction = attractionService.getAttractionById(id).orElseThrow(() -> new IllegalArgumentException("Invalid attraction Id:" + id));
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

    // == CRUD ATTRACTION TICKETS (Tetap) ==
    @GetMapping("/attraction-tickets")
    public String showAttractionTicketList(Model model) {
        model.addAttribute("attractionTickets", attractionTicketService.findAll());
        return "admin/attraction/attraction-tickets";
    }
    @GetMapping("/attraction-tickets/add")
    public String showAddAttractionTicketForm(Model model) {
        model.addAttribute("attractionTicket", new AttractionTicket());
        model.addAttribute("allAttractions", attractionService.getAllAttractions());
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
        AttractionTicket ticket = attractionTicketService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ticket Id:" + id));
        model.addAttribute("attractionTicket", ticket);
        model.addAttribute("allAttractions", attractionService.getAllAttractions());
        model.addAttribute("pageTitle", "Edit Attraction Ticket");
        return "admin/attraction/attraction-ticket-form";
    }
    @GetMapping("/attraction-tickets/delete/{id}")
    public String deleteAttractionTicket(@PathVariable("id") Integer id) {
        attractionTicketService.deleteById(id);
        return "redirect:/admin/attraction-tickets";
    }

    // == USER MANAGEMENT (Tetap) ==
    @GetMapping("/users")
    public String showUserList(Model model) {
        model.addAttribute("allUsers", userService.findAll());
        return "admin/user/users-list"; 
    }
    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", Role.values());
        model.addAttribute("pageTitle", "Add New User");
        return "admin/user/user-form";
    }
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword(""); 
        model.addAttribute("user", user);
        model.addAttribute("allRoles", Role.values());
        model.addAttribute("pageTitle", "Edit User");
        return "admin/user/user-form";
    }
    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin/users";
    }
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
    @GetMapping("/users/status/{id}")
    public String toggleUserStatus(@PathVariable("id") Integer id) {
        userService.toggleUserStatus(id);
        return "redirect:/admin/users";
    }
    // == ORDER TRANSACTION LIST ==
    @GetMapping("/transactions")
    public String showTransactionList(Model model) {
        // Mengambil semua order, diurutkan dari yang terbaru (DESC)
        List<Order> allOrders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
        model.addAttribute("allOrders", allOrders);
        return "admin/order/order-list"; 
    }

    // == ORDER MANAGEMENT (Tetap) ==
    @GetMapping("/orders")
    public String showUsersForOrders(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/order/order-users"; 
    }
    @GetMapping("/orders/{userId}")
    public String showUserOrderDetails(@PathVariable("userId") Integer userId, Model model) {
        User user = userService.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        List<Order> userOrders = orderRepository.findByUser(user);
        List<OrderDetail> hotelDetails = new ArrayList<>();
        List<OrderDetail> transportDetails = new ArrayList<>();
        List<OrderDetail> attractionDetails = new ArrayList<>();
        for (Order order : userOrders) {
            for (OrderDetail detail : order.getOrderDetails()) {
                if (detail.getHotelRoom() != null) {
                    hotelDetails.add(detail);
                } else if (detail.getTransportTicket() != null) {
                    transportDetails.add(detail);
                } else if (detail.getAttractionTicket() != null) {
                    attractionDetails.add(detail);
                }
            }
        }
    
        model.addAttribute("user", user);
        model.addAttribute("hotelDetails", hotelDetails);
        model.addAttribute("transportDetails", transportDetails);
        model.addAttribute("attractionDetails", attractionDetails);
        return "admin/order/user-order-details"; 
    }
}