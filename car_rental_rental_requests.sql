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
-- Table structure for table `rental_requests`
--

DROP TABLE IF EXISTS `rental_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rental_requests` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `discount_applied` double DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `car_id` bigint DEFAULT NULL,
  `client_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgrlt7ftbytiuxeg0e3u37h4wf` (`car_id`),
  KEY `FK19i1k1qdrd2vo8345dcxqmnvs` (`client_id`),
  CONSTRAINT `FK19i1k1qdrd2vo8345dcxqmnvs` FOREIGN KEY (`client_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKgrlt7ftbytiuxeg0e3u37h4wf` FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rental_requests`
--

LOCK TABLES `rental_requests` WRITE;
/*!40000 ALTER TABLE `rental_requests` DISABLE KEYS */;
INSERT INTO `rental_requests` VALUES (1,0,'2026-03-14','2026-03-12','APPROVED',1,2),(2,0,'2026-03-14','2026-03-12','REJECTED',3,6),(3,0,'2026-03-27','2026-03-24','APPROVED',1,7),(4,0,'2026-03-24','2026-03-21','APPROVED',4,12),(5,0,'2026-03-27','2026-03-24','REJECTED',2,12),(6,0,'2026-04-01','2026-03-31','APPROVED',2,13);
/*!40000 ALTER TABLE `rental_requests` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-22  0:32:22
