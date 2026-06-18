# 🎫 MyTicket - Travel Booking Application

MyTicket is a comprehensive web-based travel application built using Java and Spring Boot. This platform allows users to book various travel needs, including hotels, attractions, and transportation, while providing an admin dashboard for management[cite: 1].

## 🛠️ Tech Stack

*   **Backend:** Java, Spring Boot[cite: 1]
*   **Build Tool:** Maven[cite: 1]
*   **Architecture:** MVC (Model-View-Controller)[cite: 1]
*   **Security:** Spring Security (`SecurityConfig.java`)[cite: 1]
*   **Payment Gateway:** Midtrans Integration (`MidtransConfig.java`)[cite: 1]
*   **Frontend/Views:** HTML Templates (Admin & User Layouts)[cite: 1]
*   **Database:** SQL (`Database/Myticket.sql`)[cite: 1]

## ✨ Key Features

### 👤 User Features
*   **Authentication:** Secure login and registration using Spring Security[cite: 1].
*   **Service Bookings:** 
    *   🏨 **Hotels:** Browse and book hotel rooms (`Hotel.java`, `HotelRoom.java`)[cite: 1].
    *   🎡 **Attractions:** Purchase tickets for various tourist attractions (`Attraction.java`, `AttractionTicket.java`)[cite: 1].
    *   🚌 **Transportation:** Book transport tickets from various providers (`Transport.java`, `TransportTicket.java`)[cite: 1].
*   **Order Management:** View order history, order details, and generate invoices (`my-orders.html`, `invoice.html`)[cite: 1].
*   **Payment:** Integrated payment processing via Midtrans (`PaymentService.java`)[cite: 1].

### 👨‍💼 Admin Features
*   **Dashboard:** Dedicated admin panel for system overview (`admin/dashboard.html`)[cite: 1].
*   **Data Management:** Full CRUD operations for:
    *   Hotels & Rooms[cite: 1]
    *   Attractions & Tickets[cite: 1]
    *   Transport Providers & Tickets[cite: 1]
*   **Order Tracking:** Monitor and manage all user orders (`OrderController.java`, `order-list.html`)[cite: 1].
*   **User Management:** View and manage registered users (`users-list.html`)[cite: 1].

## 📂 Project Structure Highlights

The project follows a clean architectural pattern:
*   `controller/`: Handles incoming HTTP requests for Admin, User, Orders, and Pages[cite: 1].
*   `model/`: Contains entity classes mapping to the database (e.g., User, Order, Hotel, Attraction)[cite: 1].
*   `repository/`: Data access interfaces for database operations[cite: 1].
*   `service/`: Contains core business logic and integrations (e.g., `PaymentService.java`, `OrderService.java`)[cite: 1].
*   `config/`: Configuration files for MVC, Security, and Midtrans[cite: 1].
*   `resources/templates/`: HTML views organized by `admin` and `user` roles[cite: 1].

## 🚀 Getting Started

### Prerequisites
*   Java Development Kit (JDK)
*   Maven
*   SQL Database Server

### Installation
1. Clone the repository.
2. Import the database schema from `Database/Myticket.sql`[cite: 1].
3. Configure your database credentials and Midtrans API keys in `src/main/resources/application.properties`[cite: 1].
4. Run the application using Maven:
```bash
   ./mvnw spring-boot:run
