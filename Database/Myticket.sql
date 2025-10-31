-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 31, 2025 at 01:53 AM
-- Server version: 8.4.3
-- PHP Version: 8.3.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `travel_app`
--

-- --------------------------------------------------------

--
-- Table structure for table `attractions`
--

CREATE TABLE `attractions` (
  `attraction_id` int NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `location` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `category` enum('Nature','Museum','Theme Park','Culture') COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `attractions`
--

INSERT INTO `attractions` (`attraction_id`, `name`, `location`, `category`) VALUES
(1, 'Borobudur Temple', 'Magelang', 'Culture'),
(2, 'Ancol Dreamland', 'Jakarta', 'Theme Park');

-- --------------------------------------------------------

--
-- Table structure for table `attraction_tickets`
--

CREATE TABLE `attraction_tickets` (
  `attraction_ticket_id` int NOT NULL,
  `attraction_id` int DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `valid_date` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `attraction_tickets`
--

INSERT INTO `attraction_tickets` (`attraction_ticket_id`, `attraction_id`, `price`, `valid_date`) VALUES
(1, 1, 75000.00, '2025-10-05 00:00:00.000000'),
(2, 2, 150000.00, '2025-10-06 00:00:00.000000');

-- --------------------------------------------------------

--
-- Table structure for table `hotels`
--

CREATE TABLE `hotels` (
  `hotel_id` int NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `location` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `stars` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hotels`
--

INSERT INTO `hotels` (`hotel_id`, `name`, `location`, `stars`) VALUES
(1, 'Hotel Mulia', 'Jakarta', 5),
(2, 'Hotel Bali Paradise', 'Bali', 4);

-- --------------------------------------------------------

--
-- Table structure for table `hotel_rooms`
--

CREATE TABLE `hotel_rooms` (
  `room_id` int NOT NULL,
  `hotel_id` int DEFAULT NULL,
  `room_type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `capacity` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hotel_rooms`
--

INSERT INTO `hotel_rooms` (`room_id`, `hotel_id`, `room_type`, `price`, `capacity`) VALUES
(1, 1, 'Deluxe', 1200000.00, 2),
(2, 2, 'Family Suite', 2000000.00, 4);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `order_date` datetime DEFAULT NULL,
  `status` enum('Pending','Paid','Cancelled') COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `order_date`, `status`) VALUES
(1, 1, '2025-09-22 19:22:30', 'Paid'),
(2, 2, '2025-09-22 19:22:30', 'Pending');

-- --------------------------------------------------------

--
-- Table structure for table `order_details`
--

CREATE TABLE `order_details` (
  `order_detail_id` int NOT NULL,
  `order_id` int DEFAULT NULL,
  `item_type` enum('Transport','Hotel','Attraction') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `item_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `total_price` decimal(38,2) DEFAULT NULL,
  `attraction_ticket_id` int DEFAULT NULL,
  `room_id` int DEFAULT NULL,
  `ticket_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_details`
--

INSERT INTO `order_details` (`order_detail_id`, `order_id`, `item_type`, `item_id`, `quantity`, `total_price`, `attraction_ticket_id`, `room_id`, `ticket_id`) VALUES
(1, 1, 'Transport', 1, 2, 3000000.00, NULL, NULL, NULL),
(2, 1, 'Hotel', 1, 1, 1200000.00, NULL, NULL, NULL),
(3, 2, 'Attraction', 2, 3, 450000.00, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `transports`
--

CREATE TABLE `transports` (
  `transport_id` int NOT NULL,
  `provider_id` int DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `capacity` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transports`
--

INSERT INTO `transports` (`transport_id`, `provider_id`, `name`, `capacity`) VALUES
(1, 1, 'GA-1001 Jakarta-Bali', 180),
(2, 2, 'Damri Jakarta-Bandung', 40);

-- --------------------------------------------------------

--
-- Table structure for table `transport_providers`
--

CREATE TABLE `transport_providers` (
  `provider_id` int NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` enum('Bus','Train','Plane','Boat') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `contact` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transport_providers`
--

INSERT INTO `transport_providers` (`provider_id`, `name`, `type`, `contact`) VALUES
(1, 'Garuda Indonesia', 'Plane', '021-555111'),
(2, 'Damri', 'Bus', '021-444222');

-- --------------------------------------------------------

--
-- Table structure for table `transport_tickets`
--

CREATE TABLE `transport_tickets` (
  `ticket_id` int NOT NULL,
  `transport_id` int DEFAULT NULL,
  `departure_date` datetime(6) DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transport_tickets`
--

INSERT INTO `transport_tickets` (`ticket_id`, `transport_id`, `departure_date`, `price`) VALUES
(1, 1, '2025-10-01 00:00:00.000000', 1500000.00),
(2, 2, '2025-10-02 00:00:00.000000', 120000.00);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` enum('ROLE_ADMIN','ROLE_USER') COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `name`, `email`, `phone`, `password`, `role`) VALUES
(1, 'Budi Santoso', 'budi@example.com', '08123456789', '32250170a0dca92d53ec9624f336ca24', NULL),
(2, 'Siti Aminah', 'siti@example.com', '08129876543', '73a054cc528f91ca1bbdda3589b6a22d', NULL),
(3, 'Admin', 'admin@travel.app', '081200001111', '$2a$10$NQBnoJXBI94Mmwg5p.LDMe.TCk9qv/nCjKg3aVytCd3SAickg/Ope', 'ROLE_ADMIN'),
(4, 'user', 'user@travel.app', '081200002222', '$2a$10$xVH.VcD5Zq6Nh2cW7IRQB.k5ykkZyxOqyN5SFPxSBDIELXrL7W.gC', 'ROLE_USER');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attractions`
--
ALTER TABLE `attractions`
  ADD PRIMARY KEY (`attraction_id`);

--
-- Indexes for table `attraction_tickets`
--
ALTER TABLE `attraction_tickets`
  ADD PRIMARY KEY (`attraction_ticket_id`),
  ADD KEY `attraction_id` (`attraction_id`);

--
-- Indexes for table `hotels`
--
ALTER TABLE `hotels`
  ADD PRIMARY KEY (`hotel_id`);

--
-- Indexes for table `hotel_rooms`
--
ALTER TABLE `hotel_rooms`
  ADD PRIMARY KEY (`room_id`),
  ADD KEY `hotel_id` (`hotel_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `order_details`
--
ALTER TABLE `order_details`
  ADD PRIMARY KEY (`order_detail_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `FKd6hqwssqlcop4beic6hrp4ogw` (`attraction_ticket_id`),
  ADD KEY `FK2x0l27fycyiibmuv6jrvoevfb` (`room_id`),
  ADD KEY `FKfp5w8y690aiq1mgadykvnt1o3` (`ticket_id`);

--
-- Indexes for table `transports`
--
ALTER TABLE `transports`
  ADD PRIMARY KEY (`transport_id`),
  ADD KEY `provider_id` (`provider_id`);

--
-- Indexes for table `transport_providers`
--
ALTER TABLE `transport_providers`
  ADD PRIMARY KEY (`provider_id`);

--
-- Indexes for table `transport_tickets`
--
ALTER TABLE `transport_tickets`
  ADD PRIMARY KEY (`ticket_id`),
  ADD KEY `transport_id` (`transport_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `attractions`
--
ALTER TABLE `attractions`
  MODIFY `attraction_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `attraction_tickets`
--
ALTER TABLE `attraction_tickets`
  MODIFY `attraction_ticket_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `hotels`
--
ALTER TABLE `hotels`
  MODIFY `hotel_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `hotel_rooms`
--
ALTER TABLE `hotel_rooms`
  MODIFY `room_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `order_details`
--
ALTER TABLE `order_details`
  MODIFY `order_detail_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `transports`
--
ALTER TABLE `transports`
  MODIFY `transport_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `transport_providers`
--
ALTER TABLE `transport_providers`
  MODIFY `provider_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `transport_tickets`
--
ALTER TABLE `transport_tickets`
  MODIFY `ticket_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `attraction_tickets`
--
ALTER TABLE `attraction_tickets`
  ADD CONSTRAINT `attraction_tickets_ibfk_1` FOREIGN KEY (`attraction_id`) REFERENCES `attractions` (`attraction_id`);

--
-- Constraints for table `hotel_rooms`
--
ALTER TABLE `hotel_rooms`
  ADD CONSTRAINT `hotel_rooms_ibfk_1` FOREIGN KEY (`hotel_id`) REFERENCES `hotels` (`hotel_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `order_details`
--
ALTER TABLE `order_details`
  ADD CONSTRAINT `FK2x0l27fycyiibmuv6jrvoevfb` FOREIGN KEY (`room_id`) REFERENCES `hotel_rooms` (`room_id`),
  ADD CONSTRAINT `FKd6hqwssqlcop4beic6hrp4ogw` FOREIGN KEY (`attraction_ticket_id`) REFERENCES `attraction_tickets` (`attraction_ticket_id`),
  ADD CONSTRAINT `FKfp5w8y690aiq1mgadykvnt1o3` FOREIGN KEY (`ticket_id`) REFERENCES `transport_tickets` (`ticket_id`),
  ADD CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`);

--
-- Constraints for table `transports`
--
ALTER TABLE `transports`
  ADD CONSTRAINT `transports_ibfk_1` FOREIGN KEY (`provider_id`) REFERENCES `transport_providers` (`provider_id`);

--
-- Constraints for table `transport_tickets`
--
ALTER TABLE `transport_tickets`
  ADD CONSTRAINT `transport_tickets_ibfk_1` FOREIGN KEY (`transport_id`) REFERENCES `transports` (`transport_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
