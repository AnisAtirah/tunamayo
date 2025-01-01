-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 01, 2025 at 12:27 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tunamayo_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `recurtask`
--

CREATE TABLE `recurtask` (
  `numTask` int(200) NOT NULL,
  `Title` varchar(200) NOT NULL,
  `Description` varchar(200) NOT NULL,
  `RecurrenceInterval` varchar(200) NOT NULL,
  `NextOccurence` date NOT NULL,
  `Due` date NOT NULL,
  `Completion` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `recurtask`
--

INSERT INTO `recurtask` (`numTask`, `Title`, `Description`, `RecurrenceInterval`, `NextOccurence`, `Due`, `Completion`) VALUES
(1, 'FOP project', 'tunamayo', 'daily', '2025-03-01', '2025-04-01', 1),
(2, 'Vid IS', 'thanos', 'daily', '2025-01-02', '2025-01-03', 1),
(3, 'Lab Assignment', 'CSO', 'weekly', '2025-01-01', '2025-01-08', 0),
(4, 'VIVA 3', 'FOP tm', 'monthly', '2025-01-02', '2025-01-03', 1),
(5, 'entahh', 'haih', 'daily', '2025-01-02', '2025-01-03', 1),
(6, 'myfa', 'aaa', 'daily', '2025-02-01', '2025-03-01', 0),
(7, 'penatnya', 'pls', 'daily', '2025-01-01', '2025-01-02', 0),
(8, 'geram', 'cepatla', 'weekly', '2025-01-01', '2025-01-08', 0),
(9, 'haih', 'huh', 'monthly', '2025-01-01', '2025-02-01', 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
