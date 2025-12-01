package com.travelapp.travel_app.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.travelapp.travel_app.model.Attraction;
import com.travelapp.travel_app.model.AttractionTicket;
import com.travelapp.travel_app.model.Category;
import com.travelapp.travel_app.model.Hotel;
import com.travelapp.travel_app.model.HotelRoom;
import com.travelapp.travel_app.model.Role;
import com.travelapp.travel_app.model.Transport;
import com.travelapp.travel_app.model.TransportProvider;
import com.travelapp.travel_app.model.TransportTicket;
import com.travelapp.travel_app.model.TransportType;
import com.travelapp.travel_app.model.User;
import com.travelapp.travel_app.repository.UserRepository;
import com.travelapp.travel_app.repository.attraction.AttractionRepository;
import com.travelapp.travel_app.repository.attraction.AttractionTicketRepository;
import com.travelapp.travel_app.repository.hotel.HotelRepository;
import com.travelapp.travel_app.repository.hotel.HotelRoomRepository;
import com.travelapp.travel_app.repository.transport.TransportProviderRepository;
import com.travelapp.travel_app.repository.transport.TransportRepository;
import com.travelapp.travel_app.repository.transport.TransportTicketRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    
    @Autowired private HotelRepository hotelRepository;
    @Autowired private HotelRoomRepository hotelRoomRepository;
    
    @Autowired private TransportProviderRepository transportProviderRepository;
    @Autowired private TransportRepository transportRepository;
    @Autowired private TransportTicketRepository transportTicketRepository;
    
    @Autowired private AttractionRepository attractionRepository;
    @Autowired private AttractionTicketRepository attractionTicketRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        
        // Cek jika data hotel masih kosong, baru isi data dummy
        if (hotelRepository.count() == 0) {
            seedHotels();
        }
        
        if (transportRepository.count() == 0) {
            seedTransports();
        }
        
        if (attractionRepository.count() == 0) {
            seedAttractions();
        }
    }

    private void seedUsers() {
        if (userRepository.findByEmail("admin@travel.app").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@travel.app");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("081200001111");
            admin.setRole(Role.ROLE_ADMIN);
            admin.setIsActive(true);
            userRepository.save(admin);
        }

        if (userRepository.findByEmail("user@travel.app").isEmpty()) {
            User user = new User();
            user.setName("User Demo");
            user.setEmail("user@travel.app");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setPhone("081200002222");
            user.setRole(Role.ROLE_USER);
            user.setIsActive(true);
            userRepository.save(user);
        }
    }

    private void seedHotels() {
        String[] cities = {"Jakarta", "Bali", "Bandung", "Yogyakarta", "Surabaya"};
        String[] hotelNames = {"Grand Luxury", "Ocean View", "Mountain Retreat", "City Center", "Sunset Paradise"};

        for (int i = 1; i <= 10; i++) {
            Hotel hotel = new Hotel();
            String city = cities[random.nextInt(cities.length)];
            hotel.setName(hotelNames[random.nextInt(hotelNames.length)] + " " + i);
            hotel.setLocation("Jl. Raya No. " + i + ", " + city);
            hotel.setStars(3 + random.nextInt(3)); // Random bintang 3-5
            // Menggunakan placeholder image (kosongkan jika ingin upload manual nanti)
            // hotel.setImage("hotel-" + i + ".jpg"); 
            
            Hotel savedHotel = hotelRepository.save(hotel);

            // Create 2 Rooms for each Hotel
            createHotelRoom(savedHotel, "Standard Room", new BigDecimal(500000 + (i * 10000)), 2);
            createHotelRoom(savedHotel, "Deluxe Suite", new BigDecimal(1200000 + (i * 20000)), 4);
            
            System.out.println("Seeded Hotel: " + hotel.getName());
        }
    }

    private void createHotelRoom(Hotel hotel, String type, BigDecimal price, int capacity) {
        HotelRoom room = new HotelRoom();
        room.setHotel(hotel);
        room.setRoomType(type);
        room.setPrice(price);
        room.setCapacity(capacity);
        hotelRoomRepository.save(room);
    }

    private void seedTransports() {
        // 1. Buat Providers Dulu
        List<TransportProvider> providers = Arrays.asList(
            createProvider("Garuda Indonesia", TransportType.Plane),
            createProvider("KAI Indonesia", TransportType.Train),
            createProvider("Sinar Jaya", TransportType.Bus),
            createProvider("Pelni", TransportType.Boat)
        );

        // 2. Buat 10 Transport
        for (int i = 1; i <= 10; i++) {
            TransportProvider provider = providers.get(random.nextInt(providers.size()));
            
            Transport transport = new Transport();
            transport.setName(provider.getName() + " Armada " + i);
            transport.setCapacity(50 + random.nextInt(100));
            transport.setProvider(provider);
            // transport.setImage("transport-" + i + ".jpg");
            
            Transport savedTransport = transportRepository.save(transport);

            // 3. Buat 2 Tiket per Transport
            createTransportTicket(savedTransport, "Economy Class", new BigDecimal(150000 + (i*5000)));
            createTransportTicket(savedTransport, "Business Class", new BigDecimal(500000 + (i*10000)));
            
            System.out.println("Seeded Transport: " + transport.getName());
        }
    }

    private TransportProvider createProvider(String name, TransportType type) {
        TransportProvider p = new TransportProvider();
        p.setName(name);
        p.setType(type);
        p.setContact("contact@" + name.toLowerCase().replaceAll(" ", "") + ".com");
        return transportProviderRepository.save(p);
    }

    private void createTransportTicket(Transport transport, String ticketClass, BigDecimal price) {
        TransportTicket ticket = new TransportTicket();
        ticket.setTransport(transport);
        ticket.setTicketClass(ticketClass); // Pastikan field ini ada di Model TransportTicket
        ticket.setPrice(price);
        // Departure Date tidak diset karena user yang memilih (Open Date)
        transportTicketRepository.save(ticket);
    }

    private void seedAttractions() {
        String[] attrNames = {"Dufan", "Taman Safari", "Museum Angkut", "Candi Borobudur", "Jatim Park"};
        String[] locations = {"Jakarta", "Bogor", "Malang", "Magelang", "Batu"};
        
        for (int i = 1; i <= 10; i++) {
            Attraction attraction = new Attraction();
            attraction.setName(attrNames[random.nextInt(attrNames.length)] + " " + i);
            attraction.setLocation(locations[random.nextInt(locations.length)]);
            attraction.setCategory(Category.values()[random.nextInt(Category.values().length)]);
            // attraction.setImage("attraction-" + i + ".jpg");
            
            Attraction savedAttraction = attractionRepository.save(attraction);

            // Buat 2 Tiket per Atraksi
            createAttractionTicket(savedAttraction, "Regular Pass", new BigDecimal(75000 + (i*2000)));
            createAttractionTicket(savedAttraction, "VIP Fast Track", new BigDecimal(150000 + (i*5000)));
            
            System.out.println("Seeded Attraction: " + attraction.getName());
        }
    }

    private void createAttractionTicket(Attraction attraction, String type, BigDecimal price) {
        AttractionTicket ticket = new AttractionTicket();
        ticket.setAttraction(attraction);
        ticket.setTicketType(type); // Pastikan field ini ada di Model AttractionTicket
        ticket.setPrice(price);
        // Valid Date tidak diset karena user yang memilih
        attractionTicketRepository.save(ticket);
    }
}