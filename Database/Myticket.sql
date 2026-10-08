USE travel_app;

-- --------------------------------------------------------
-- Drop Tables in correct foreign key order
-- --------------------------------------------------------
DROP TABLE IF EXISTS order_details;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS attraction_tickets;
DROP TABLE IF EXISTS attractions;
DROP TABLE IF EXISTS hotel_rooms;
DROP TABLE IF EXISTS hotels;
DROP TABLE IF EXISTS transport_tickets;
DROP TABLE IF EXISTS transports;
DROP TABLE IF EXISTS transport_providers;
DROP TABLE IF EXISTS users;

-- --------------------------------------------------------
-- Table structure for table `users`
-- --------------------------------------------------------
CREATE TABLE users (
  user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) DEFAULT NULL,
  email VARCHAR(255) UNIQUE DEFAULT NULL,
  phone VARCHAR(255) DEFAULT NULL,
  password VARCHAR(255) DEFAULT NULL,
  role ENUM('ROLE_ADMIN','ROLE_USER') DEFAULT 'ROLE_USER'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO users (user_id, name, email, phone, password, role) VALUES
(1, 'Budi Santoso', 'budi@example.com', '08123456789', '32250170a0dca92d53ec9624f336ca24', 'ROLE_USER'),
(2, 'Siti Aminah', 'siti@example.com', '08129876543', '73a054cc528f91ca1bbdda3589b6a22d', 'ROLE_USER'),
(3, 'Admin', 'admin@travel.app', '081200001111', '$2a$10$NQBnoJXBI94Mmwg5p.LDMe.TCk9qv/nCjKg3aVytCd3SAickg/Ope', 'ROLE_ADMIN'),
(4, 'user', 'user@travel.app', '081200002222', '$2a$10$xVH.VcD5Zq6Nh2cW7IRQB.k5ykkZyxOqyN5SFPxSBDIELXrL7W.gC', 'ROLE_USER');

-- --------------------------------------------------------
-- Table structure for table `hotels`
-- --------------------------------------------------------
CREATE TABLE hotels (
  hotel_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) DEFAULT NULL,
  location VARCHAR(255) DEFAULT NULL,
  stars INT DEFAULT NULL,
  image VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO hotels (hotel_id, name, location, stars, image) VALUES
(1, 'Hotel Mulia', 'Jakarta', 5, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?q=80&w=1200&auto=format&fit=crop'),
(2, 'Hotel Bali Paradise', 'Bali', 4, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?q=80&w=1200&auto=format&fit=crop');

-- --------------------------------------------------------
-- Table structure for table `hotel_rooms`
-- --------------------------------------------------------
CREATE TABLE hotel_rooms (
  room_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  hotel_id INT DEFAULT NULL,
  room_type VARCHAR(255) DEFAULT NULL,
  price DECIMAL(38,2) DEFAULT NULL,
  capacity INT DEFAULT NULL,
  CONSTRAINT fk_hotel_rooms_hotel FOREIGN KEY (hotel_id) REFERENCES hotels (hotel_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO hotel_rooms (room_id, hotel_id, room_type, price, capacity) VALUES
(1, 1, 'Deluxe', 1200000.00, 2),
(2, 2, 'Family Suite', 2000000.00, 4);

-- --------------------------------------------------------
-- Table structure for table `attractions`
-- --------------------------------------------------------
CREATE TABLE attractions (
  attraction_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) DEFAULT NULL,
  location VARCHAR(255) DEFAULT NULL,
  category ENUM('Nature','Museum','Theme Park','Culture') DEFAULT NULL,
  image VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO attractions (attraction_id, name, location, category, image) VALUES
(1, 'Borobudur Temple', 'Magelang', 'Culture', 'https://images.unsplash.com/photo-1596402184320-417e7178b2cd?q=80&w=1200&auto=format&fit=crop'),
(2, 'Ancol Dreamland', 'Jakarta', 'Theme Park', 'https://images.unsplash.com/photo-1513889961551-628c1e5e2ee9?q=80&w=1200&auto=format&fit=crop');

-- --------------------------------------------------------
-- Table structure for table `attraction_tickets`
-- --------------------------------------------------------
CREATE TABLE attraction_tickets (
  attraction_ticket_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  attraction_id INT DEFAULT NULL,
  price DECIMAL(38,2) DEFAULT NULL,
  valid_date DATETIME(6) DEFAULT NULL,
  CONSTRAINT fk_attraction_tickets_attraction FOREIGN KEY (attraction_id) REFERENCES attractions (attraction_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO attraction_tickets (attraction_ticket_id, attraction_id, price, valid_date) VALUES
(1, 1, 75000.00, '2025-10-05 00:00:00.000000'),
(2, 2, 150000.00, '2025-10-06 00:00:00.000000');

-- --------------------------------------------------------
-- Table structure for table `transport_providers`
-- --------------------------------------------------------
CREATE TABLE transport_providers (
  provider_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) DEFAULT NULL,
  type ENUM('Bus','Train','Plane','Boat') DEFAULT NULL,
  contact VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO transport_providers (provider_id, name, type, contact) VALUES
(1, 'Garuda Indonesia', 'Plane', '021-555111'),
(2, 'Damri', 'Bus', '021-444222');

-- --------------------------------------------------------
-- Table structure for table `transports`
-- --------------------------------------------------------
CREATE TABLE transports (
  transport_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  provider_id INT DEFAULT NULL,
  name VARCHAR(255) DEFAULT NULL,
  capacity INT DEFAULT NULL,
  image VARCHAR(255) DEFAULT NULL,
  CONSTRAINT fk_transports_provider FOREIGN KEY (provider_id) REFERENCES transport_providers (provider_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO transports (transport_id, provider_id, name, capacity, image) VALUES
(1, 1, 'GA-1001 Jakarta-Bali', 180, 'https://images.unsplash.com/photo-1436491865332-7a61a109cc05?q=80&w=1200&auto=format&fit=crop'),
(2, 2, 'Damri Jakarta-Bandung', 40, 'https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?q=80&w=1200&auto=format&fit=crop');

-- --------------------------------------------------------
-- Table structure for table `transport_tickets`
-- --------------------------------------------------------
CREATE TABLE transport_tickets (
  ticket_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  transport_id INT DEFAULT NULL,
  departure_date DATETIME(6) DEFAULT NULL,
  price DECIMAL(38,2) DEFAULT NULL,
  CONSTRAINT fk_transport_tickets_transport FOREIGN KEY (transport_id) REFERENCES transports (transport_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO transport_tickets (ticket_id, transport_id, departure_date, price) VALUES
(1, 1, '2025-10-01 00:00:00.000000', 1500000.00),
(2, 2, '2025-10-02 00:00:00.000000', 120000.00);

-- --------------------------------------------------------
-- Table structure for table `orders`
-- --------------------------------------------------------
CREATE TABLE orders (
  order_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id INT DEFAULT NULL,
  order_date DATETIME DEFAULT NULL,
  status ENUM('Pending','Paid','Cancelled') DEFAULT 'Pending',
  CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO orders (order_id, user_id, order_date, status) VALUES
(1, 1, '2025-09-22 19:22:30', 'Paid'),
(2, 2, '2025-09-22 19:22:30', 'Pending');

-- --------------------------------------------------------
-- Table structure for table `order_details`
-- --------------------------------------------------------
CREATE TABLE order_details (
  order_detail_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  order_id INT DEFAULT NULL,
  item_type ENUM('Transport','Hotel','Attraction') DEFAULT NULL,
  item_id INT DEFAULT NULL,
  quantity INT DEFAULT NULL,
  total_price DECIMAL(38,2) DEFAULT NULL,
  attraction_ticket_id INT DEFAULT NULL,
  room_id INT DEFAULT NULL,
  ticket_id INT DEFAULT NULL,
  CONSTRAINT fk_order_details_order FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
  CONSTRAINT fk_order_details_hotel_room FOREIGN KEY (room_id) REFERENCES hotel_rooms (room_id) ON DELETE SET NULL,
  CONSTRAINT fk_order_details_attraction_ticket FOREIGN KEY (attraction_ticket_id) REFERENCES attraction_tickets (attraction_ticket_id) ON DELETE SET NULL,
  CONSTRAINT fk_order_details_transport_ticket FOREIGN KEY (ticket_id) REFERENCES transport_tickets (ticket_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO order_details (order_detail_id, order_id, item_type, item_id, quantity, total_price, attraction_ticket_id, room_id, ticket_id) VALUES
(1, 1, 'Transport', 1, 2, 3000000.00, NULL, NULL, 1),
(2, 1, 'Hotel', 1, 1, 1200000.00, NULL, 1, NULL),
(3, 2, 'Attraction', 2, 3, 450000.00, 2, NULL, NULL);
