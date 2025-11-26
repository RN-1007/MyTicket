package com.travelapp.travel_app.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelapp.travel_app.model.Attraction;
import com.travelapp.travel_app.model.AttractionTicket; 
import com.travelapp.travel_app.model.Category;
import com.travelapp.travel_app.model.Hotel;
import com.travelapp.travel_app.model.HotelRoom; 
import com.travelapp.travel_app.model.Order;
import com.travelapp.travel_app.model.OrderDetail;
import com.travelapp.travel_app.model.Role;
import com.travelapp.travel_app.model.Transport;
import com.travelapp.travel_app.model.TransportProvider; // Import Baru
import com.travelapp.travel_app.model.TransportTicket; 
import com.travelapp.travel_app.model.TransportType; // Import Baru
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
import com.travelapp.travel_app.service.user.UserService;
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

    // ============================================================
    // 1. MANAGE HOTELS
    // ============================================================
    
    @GetMapping("/hotels")
    public String showHotelList(Model model) {
        model.addAttribute("hotels", hotelService.getAllHotels());
        model.addAttribute("hotel", new Hotel()); 
        return "admin/hotel/hotels"; 
    }
    
    @PostMapping("/hotels/save")
    public String saveHotel(@ModelAttribute("hotel") Hotel hotel, 
                            @RequestParam("imageFile") MultipartFile multipartFile,
                            RedirectAttributes redirectAttributes) {
        try {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            if (!fileName.isEmpty()) {
                hotel.setImage(fileName);
                Hotel savedHotel = hotelService.saveHotel(hotel);
                String uploadDir = "hotel-photos/" + savedHotel.getHotelId();
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            } else {
                if (hotel.getHotelId() != null) {
                    Hotel existingHotel = hotelService.getHotelById(hotel.getHotelId()).orElse(null);
                    if (existingHotel != null) {
                        hotel.setImage(existingHotel.getImage());
                    }
                }
                hotelService.saveHotel(hotel);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Data Hotel berhasil disimpan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyimpan data: " + e.getMessage());
        }
        return "redirect:/admin/hotels"; 
    }
    
    @GetMapping("/hotels/delete/{id}")
    public String deleteHotel(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            hotelService.deleteHotel(id); 
            redirectAttributes.addFlashAttribute("successMessage", "Data Hotel berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus data.");
        }
        return "redirect:/admin/hotels"; 
    }
    
    // ============================================================
    // 2. MANAGE HOTEL ROOMS
    // ============================================================

    @GetMapping("/hotel-rooms")
    public String showHotelRoomList(Model model) {
        model.addAttribute("hotelRooms", hotelRoomService.findAll());
        model.addAttribute("hotelRoom", new HotelRoom());
        model.addAttribute("allHotels", hotelService.getAllHotels());
        return "admin/hotel/hotel-rooms";
    }

    @PostMapping("/hotel-rooms/save")
    public String saveHotelRoom(@ModelAttribute("hotelRoom") HotelRoom hotelRoom, RedirectAttributes redirectAttributes) {
        try {
            hotelRoomService.save(hotelRoom); 
            redirectAttributes.addFlashAttribute("successMessage", "Data Kamar berhasil disimpan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyimpan data.");
        }
        return "redirect:/admin/hotel-rooms";
    }

    @GetMapping("/hotel-rooms/delete/{id}")
    public String deleteHotelRoom(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            hotelRoomService.deleteById(id); 
            redirectAttributes.addFlashAttribute("successMessage", "Data Kamar berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus data.");
        }
        return "redirect:/admin/hotel-rooms";
    }

    // ============================================================
    // 3. MANAGE TRANSPORTS & PROVIDERS
    // ============================================================

    @GetMapping("/transports")
    public String showTransportList(Model model) {
        model.addAttribute("transports", transportService.getAllTransports());
        model.addAttribute("transport", new Transport());
        
        // Tambahan untuk Form Provider
        model.addAttribute("provider", new TransportProvider()); 
        model.addAttribute("allProviders", transportProviderRepository.findAll()); 
        model.addAttribute("allTransportTypes", TransportType.values()); // Enum untuk dropdown
        
        return "admin/transport/transport"; 
    }
    
    // --- FITUR BARU: SAVE PROVIDER ---
    @PostMapping("/transport-providers/save")
    public String saveTransportProvider(@ModelAttribute("provider") TransportProvider provider, RedirectAttributes redirectAttributes) {
        try {
            transportProviderRepository.save(provider);
            redirectAttributes.addFlashAttribute("successMessage", "Provider berhasil ditambahkan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyimpan provider: " + e.getMessage());
        }
        return "redirect:/admin/transports";
    }
    
    @PostMapping("/transports/save")
    public String saveTransport(@ModelAttribute("transport") Transport transport,
                                @RequestParam("imageFile") MultipartFile multipartFile,
                                RedirectAttributes redirectAttributes) {
        try {
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
            redirectAttributes.addFlashAttribute("successMessage", "Data Transport berhasil disimpan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyimpan data: " + e.getMessage());
        }
        return "redirect:/admin/transports"; 
    }
    
    @GetMapping("/transports/delete/{id}")
    public String deleteTransport(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            transportService.deleteTransport(id);
            redirectAttributes.addFlashAttribute("successMessage", "Data Transport berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus data.");
        }
        return "redirect:/admin/transports"; 
    }

    // ============================================================
    // 4. MANAGE TRANSPORT TICKETS
    // ============================================================

    @GetMapping("/transport-tickets")
    public String showTransportTicketList(Model model) {
        model.addAttribute("transportTickets", transportTicketService.findAll());
        model.addAttribute("transportTicket", new TransportTicket());
        model.addAttribute("allTransports", transportService.getAllTransports());
        return "admin/transport/transport-tickets";
    }

    @PostMapping("/transport-tickets/save")
    public String saveTransportTicket(@ModelAttribute("transportTicket") TransportTicket ticket, RedirectAttributes redirectAttributes) {
        try {
            transportTicketService.save(ticket);
            redirectAttributes.addFlashAttribute("successMessage", "Tiket Transport berhasil disimpan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyimpan tiket.");
        }
        return "redirect:/admin/transport-tickets";
    }

    @GetMapping("/transport-tickets/delete/{id}")
    public String deleteTransportTicket(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            transportTicketService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Tiket Transport berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus tiket.");
        }
        return "redirect:/admin/transport-tickets";
    }

    // ============================================================
    // 5. MANAGE ATTRACTIONS
    // ============================================================

    @GetMapping("/attractions")
    public String showAttractionList(Model model) {
        model.addAttribute("attractions", attractionService.getAllAttractions());
        model.addAttribute("attraction", new Attraction());
        model.addAttribute("allCategories", Category.values()); 
        return "admin/attraction/attraction"; 
    }
    
    @PostMapping("/attractions/save")
    public String saveAttraction(@ModelAttribute("attraction") Attraction attraction,
                                 @RequestParam("imageFile") MultipartFile multipartFile,
                                 RedirectAttributes redirectAttributes) {
        try {
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
            redirectAttributes.addFlashAttribute("successMessage", "Data Atraksi berhasil disimpan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyimpan data: " + e.getMessage());
        }
        return "redirect:/admin/attractions"; 
    }
    
    @GetMapping("/attractions/delete/{id}")
    public String deleteAttraction(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            attractionService.deleteAttraction(id);
            redirectAttributes.addFlashAttribute("successMessage", "Data Atraksi berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus data.");
        }
        return "redirect:/admin/attractions"; 
    }

    // ============================================================
    // 6. MANAGE ATTRACTION TICKETS
    // ============================================================

    @GetMapping("/attraction-tickets")
    public String showAttractionTicketList(Model model) {
        model.addAttribute("attractionTickets", attractionTicketService.findAll());
        model.addAttribute("attractionTicket", new AttractionTicket());
        model.addAttribute("allAttractions", attractionService.getAllAttractions());
        return "admin/attraction/attraction-tickets";
    }

    @PostMapping("/attraction-tickets/save")
    public String saveAttractionTicket(@ModelAttribute("attractionTicket") AttractionTicket ticket, RedirectAttributes redirectAttributes) {
        try {
            attractionTicketService.save(ticket);
            redirectAttributes.addFlashAttribute("successMessage", "Tiket Atraksi berhasil disimpan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyimpan tiket.");
        }
        return "redirect:/admin/attraction-tickets";
    }

    @GetMapping("/attraction-tickets/delete/{id}")
    public String deleteAttractionTicket(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            attractionTicketService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Tiket Atraksi berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus tiket.");
        }
        return "redirect:/admin/attraction-tickets";
    }

    // ============================================================
    // 7. MANAGE USERS
    // ============================================================

    @GetMapping("/users")
    public String showUserList(Model model) {
        model.addAttribute("allUsers", userService.findAll());
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", Role.values());
        return "admin/user/users-list"; 
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        try {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Data User berhasil disimpan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyimpan user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "User berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus user.");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/status/{id}")
    public String toggleUserStatus(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleUserStatus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Status User berhasil diperbarui!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal memperbarui status user.");
        }
        return "redirect:/admin/users";
    }

    // ============================================================
    // 8. TRANSACTION & ORDERS
    // ============================================================

    @GetMapping("/transactions")
    public String showTransactionList(Model model) {
        List<Order> allOrders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
        model.addAttribute("allOrders", allOrders);
        return "admin/order/order-list"; 
    }

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