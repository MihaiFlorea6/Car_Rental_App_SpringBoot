-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: car_rental
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(100) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'misuflorea06@gmail.com',_binary '','$2a$10$M22wS4lt25xK9OGXtZ.truSYLiK9o5jKX2yMaXGWGR8pxR2ylqr9G','MANAGER','Mihai6'),(2,'andrei1@gmail.com',_binary '','$2a$10$DgufT2Ufn7wUdDj2oejPiOd9nhgx73czGspLb41dkZ3yNsPXfxLqi','CLIENT','Andrei'),(3,'m12@gmail.com',_binary '','$2a$10$A2UIal6UTQ0gAvSU83H8..3k8dja4jW51LqHIN0HnKaXgZYozDU8S','ADMIN','MihaiAdmin'),(4,'ana2@gmail.com',_binary '','$2a$10$63iDgTxLHjP5jua/dmGKH./6voUT7Y1.u/qbRlRhRSpiao.RLLHnW','EMPLOYEE','Ana'),(5,'mihaiflorea611@gmail.com',_binary '','$2a$10$YFb3yW8EqpwvP5tIEoOGZuZ4mZWmgdy6TyXmtetlXpwhAFlkIYkNe','EMPLOYEE','MihaiEmployee'),(6,'cristina@gmail.com',_binary '','$2a$10$lqbMaS64FXgbOpFwY4Cx8.8xm85tT7NgrUz7uTH9v/0hMT/R1hjBa','CLIENT','Cristina'),(7,'radu@gmail.com',_binary '','$2a$10$fAH0ZFjpk5XOOBLVefb2feORZpCqLrb5S24bbkJOBw.yKSJIu6kga','CLIENT','Radu'),(8,'c8@gmail.com',_binary '','$2a$10$p8OZMI7E7ROXd2xTXxbzgen21snSSuQ2y.Nwsuw4WgKOIcTdzR0Rm','MANAGER','Cristian8'),(9,'cristina7@gmail.com',_binary '','$2a$10$NG54NGCKrn7hDWUACMZoc.gXcOnVeB7ci9Bul1wLytgq5EZ82eEJK','EMPLOYEE','CristinaEmployee'),(10,'Angajat@gmail.com',_binary '\0','$2a$10$C4u980BMElWZlpv9VdPJkefTmtheqbwL8I9eax69cwy.qJjXUNSKa','EMPLOYEE','Angajat1'),(11,'Manager@gmail.com',_binary '','$2a$10$I//KKtFaKrgboSLEP.E/Hu1AhEQwX9Ol1YgB52PQKwjll2zTdDWAe','MANAGER','Manager1'),(12,'andrei12@gmail.com',_binary '','$2a$10$wKAJunbOJ9C8kBB/xy7DeON6LTIe2.OM.rGrJVpTPHSe5J8P2bpxS','CLIENT','Andrei12'),(13,'andrei123@gmail.com',_binary '','$2a$10$G8XowdgACtrjp6kxZglAbeF1qZiYlbDQ6yuA5gx2Zh7lIpiBEcjkS','CLIENT','Andrei123'),(14,'angajat2@gmail.com',_binary '','$2a$10$1I1Yr60Uu9Hqbe1rdapwMO56hK6rW17hJHbtY0G7kAMB7cXhksri6','EMPLOYEE','Angajat2');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-22  0:32:23
