-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: consultoriocoesdental
-- ------------------------------------------------------
-- Server version	9.5.0

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'cfb47e44-da03-11f0-a49e-107c61ab15e9:1-251';

--
-- Table structure for table `cita`
--

DROP TABLE IF EXISTS `cita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cita` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado` varchar(50) DEFAULT NULL,
  `fecha_cita` datetime(6) NOT NULL,
  `consultorio_id` bigint NOT NULL,
  `odontologo_id` bigint NOT NULL,
  `paciente_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdf90y1t11oyx02gw08l69ucxm` (`consultorio_id`),
  KEY `FK8nahxvrvcdh8r857a6hoi9maw` (`odontologo_id`),
  KEY `FKdbtp2mwrvqktqu8954d3xaddv` (`paciente_id`),
  CONSTRAINT `FK8nahxvrvcdh8r857a6hoi9maw` FOREIGN KEY (`odontologo_id`) REFERENCES `odontologo` (`id`),
  CONSTRAINT `FKdbtp2mwrvqktqu8954d3xaddv` FOREIGN KEY (`paciente_id`) REFERENCES `paciente` (`id`),
  CONSTRAINT `FKdf90y1t11oyx02gw08l69ucxm` FOREIGN KEY (`consultorio_id`) REFERENCES `consultorio` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cita`
--

LOCK TABLES `cita` WRITE;
/*!40000 ALTER TABLE `cita` DISABLE KEYS */;
INSERT INTO `cita` VALUES (1,'FINALIZADA','2026-07-22 18:00:00.000000',1,12,11),(2,'FINALIZADA','2026-07-30 20:00:00.000000',7,6,11),(3,'CANCELADA','2026-08-12 13:00:00.000000',1,3,11),(4,'FINALIZADA','2026-07-23 16:00:00.000000',1,3,11),(5,'CREADA','2026-07-31 18:00:00.000000',1,2,13),(6,'CANCELADA','2026-07-25 20:00:00.000000',1,3,13),(7,'FINALIZADA','2026-07-25 20:00:00.000000',2,3,13),(8,'PENDIENTE_APROBACION','2026-07-31 18:00:00.000000',2,12,11);
/*!40000 ALTER TABLE `cita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consultorio`
--

DROP TABLE IF EXISTS `consultorio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consultorio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado` enum('ACTIVO','INACTIVO') DEFAULT NULL,
  `nombre_consultorio` varchar(255) NOT NULL,
  `piso` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `consultorio_chk_1` CHECK ((`piso` >= 1))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consultorio`
--

LOCK TABLES `consultorio` WRITE;
/*!40000 ALTER TABLE `consultorio` DISABLE KEYS */;
INSERT INTO `consultorio` VALUES (1,'ACTIVO','Sala Odontológica A',2),(2,'ACTIVO','Sala Odontológica B',2),(3,'ACTIVO','Sala Odontológica C',2),(4,'INACTIVO','Sala Odontológica D',3),(5,'INACTIVO','Sala Odontológica E',4),(6,'INACTIVO','Sala Odontológica F',5),(7,'ACTIVO','Sala Odontologica D',3);
/*!40000 ALTER TABLE `consultorio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `especialidad`
--

DROP TABLE IF EXISTS `especialidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `especialidad` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `costo` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `especialidad`
--

LOCK TABLES `especialidad` WRITE;
/*!40000 ALTER TABLE `especialidad` DISABLE KEYS */;
INSERT INTO `especialidad` VALUES (1,'Ortodoncia',50),(2,'Pediatria',100),(3,'Cirugia',150),(4,'Endodoncia',60),(5,'Anestesia',45),(6,'Ortopedia',80),(7,'Patología Bucal',110),(8,'Endodoncia Infantil',200);
/*!40000 ALTER TABLE `especialidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historial_clinico`
--

DROP TABLE IF EXISTS `historial_clinico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historial_clinico` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `paciente_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKk29340eogudfjgpye1uq7ah3l` (`paciente_id`),
  CONSTRAINT `FKdtqyxqlmud8cxkav7b4wiossh` FOREIGN KEY (`paciente_id`) REFERENCES `paciente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historial_clinico`
--

LOCK TABLES `historial_clinico` WRITE;
/*!40000 ALTER TABLE `historial_clinico` DISABLE KEYS */;
INSERT INTO `historial_clinico` VALUES (1,11),(2,13);
/*!40000 ALTER TABLE `historial_clinico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `odontologo`
--

DROP TABLE IF EXISTS `odontologo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `odontologo` (
  `colegiatura` varchar(5) NOT NULL,
  `fecha_registro` datetime(6) NOT NULL,
  `id` bigint NOT NULL,
  `especialidad_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpx9edj6sfjrqo9mq5l86la8ep` (`especialidad_id`),
  CONSTRAINT `FK2xw2u54hayplb8j3f3t4khejv` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FKpx9edj6sfjrqo9mq5l86la8ep` FOREIGN KEY (`especialidad_id`) REFERENCES `especialidad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `odontologo`
--

LOCK TABLES `odontologo` WRITE;
/*!40000 ALTER TABLE `odontologo` DISABLE KEYS */;
INSERT INTO `odontologo` VALUES ('99887','2026-05-25 20:26:48.638534',2,5),('99887','2026-05-25 20:32:05.317867',3,5),('12345','2026-05-26 00:28:07.319705',4,2),('25847','2026-05-27 19:02:03.798985',5,7),('55555','2026-07-20 21:48:55.834922',6,7),('11112','2026-07-20 22:16:41.762243',9,8),('12124','2026-07-20 22:24:18.253246',12,3);
/*!40000 ALTER TABLE `odontologo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paciente`
--

DROP TABLE IF EXISTS `paciente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paciente` (
  `fecha_inscripcion` datetime(6) NOT NULL,
  `fecha_nacimiento` datetime(6) NOT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKnk7alk2a1iathi4h7mqftlbn` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paciente`
--

LOCK TABLES `paciente` WRITE;
/*!40000 ALTER TABLE `paciente` DISABLE KEYS */;
INSERT INTO `paciente` VALUES ('2026-07-20 22:23:18.860975','2006-05-27 00:00:00.000000',11),('2026-07-21 18:07:03.834079','2006-07-15 00:00:00.000000',13);
/*!40000 ALTER TABLE `paciente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pago`
--

DROP TABLE IF EXISTS `pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado` varchar(255) DEFAULT NULL,
  `fecha_pago` datetime(6) DEFAULT NULL,
  `metodo_pago` varchar(255) DEFAULT NULL,
  `monto` double NOT NULL,
  `cita_id` bigint NOT NULL,
  `estado_pago` enum('ANULADO','PAGADO','PENDIENTE') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8ebpauwtodpjyeb1035r19sh5` (`cita_id`),
  CONSTRAINT `FKq90f1qbia6fmjfrcx472p10nq` FOREIGN KEY (`cita_id`) REFERENCES `cita` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pago`
--

LOCK TABLES `pago` WRITE;
/*!40000 ALTER TABLE `pago` DISABLE KEYS */;
INSERT INTO `pago` VALUES (1,NULL,'2026-07-20 22:41:39.883892','EFECTIVO',50,1,'PAGADO'),(2,NULL,'2026-07-21 18:21:34.461075','EFECTIVO',45,7,'PAGADO'),(3,NULL,NULL,'TRANSFERENCIA',45,4,'PAGADO');
/*!40000 ALTER TABLE `pago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recepcionista`
--

DROP TABLE IF EXISTS `recepcionista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recepcionista` (
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKe3pva7euet2o2d14defrl9lnp` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recepcionista`
--

LOCK TABLES `recepcionista` WRITE;
/*!40000 ALTER TABLE `recepcionista` DISABLE KEYS */;
INSERT INTO `recepcionista` VALUES (7),(14);
/*!40000 ALTER TABLE `recepcionista` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registro_clinico`
--

DROP TABLE IF EXISTS `registro_clinico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registro_clinico` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `detalle` varchar(255) NOT NULL,
  `cita_id` bigint DEFAULT NULL,
  `historial_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3tkujkkeeqe31shkjcjykegnt` (`cita_id`),
  KEY `FKdpb9p1g5pfp9go9h96uox6uac` (`historial_id`),
  CONSTRAINT `FK3tkujkkeeqe31shkjcjykegnt` FOREIGN KEY (`cita_id`) REFERENCES `cita` (`id`),
  CONSTRAINT `FKdpb9p1g5pfp9go9h96uox6uac` FOREIGN KEY (`historial_id`) REFERENCES `historial_clinico` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registro_clinico`
--

LOCK TABLES `registro_clinico` WRITE;
/*!40000 ALTER TABLE `registro_clinico` DISABLE KEYS */;
INSERT INTO `registro_clinico` VALUES (1,'Procedimientos realizados:\n-Inspección a detalle\n-Medicación a medida',1,1),(2,'Se realizo procedimiento estandar',2,1),(3,'Se realizo procedimiento estandar',7,2),(4,'Todo en orden',4,1);
/*!40000 ALTER TABLE `registro_clinico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tratamiento`
--

DROP TABLE IF EXISTS `tratamiento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tratamiento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `tarifa` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tratamiento`
--

LOCK TABLES `tratamiento` WRITE;
/*!40000 ALTER TABLE `tratamiento` DISABLE KEYS */;
/*!40000 ALTER TABLE `tratamiento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `tipo_usuario` varchar(31) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dni` varchar(8) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `estado` enum('ACTIVO','INACTIVO') DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(255) DEFAULT NULL,
  `telefono` varchar(9) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKma71x4n4tydibsd9qt0m71le7` (`dni`),
  UNIQUE KEY `UK2mlfr087gb1ce55f2j87o74t` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('Usuario',1,'11111111','admin@coes.com','ACTIVO','Admin Supremo','$2a$10$M/aadrgDPUdtFAMKG6tPluiXdWVvtNWY5dEv0sVOtnVDv39r/7NXy','Admin','999888777'),('Odontologo',2,'88888888','cesar@coes.com','ACTIVO','Dr. Cesar Bolaños','$2a$10$IQhc9mADRFuv.DoVnHv/Bev67Bd.RBrXuylCkw7buQeQfNBReGsDS','Odontologo','986958741'),('Odontologo',3,'22222222','leonardo@coes.com','ACTIVO','Dr. Leonardo Coronel','$2a$10$rtDajsCHGdV4/cFlIMIwqOxcLgMQlNS.2crRNsZDmbxDMtqY.5ab.','Odontologo','999999999'),('Odontologo',4,'85241234','hugo@coes.com','ACTIVO','Hugo Garcia','$2a$10$.AQJFuur/9PQ41NAafQaO.Ve6W4dhYHWOHVmZ1Q9IG./h.eaC7.aS','Odontologo','986574124'),('Odontologo',5,'65432141','kevin@coes.com','ACTIVO','Kevin Gutierrez','$2a$10$uJ5KxSSG.Np8RNoFDj0gs.ICGo1ponDIygHa0tkENedgzymRzQflK','Odontologo','986524123'),('Odontologo',6,'25356214','carlos@coes.com','ACTIVO','Carlos Rivera','$2a$10$oiImz03VD9iwEg1fgK.7yuH.s1aw8p1hBjwrshZZ9UnS4.AL235Xq','Odontologo','999888565'),('Recepcionista',7,'89856857','julian@coes.com','ACTIVO','Julian Alvarez','$2a$10$zw35D5OsQnkIYnX09tSy8u9CybBi/d5KM6CY4lOuQoAqUK5yNC6We','Recepcionista','969698574'),('Odontologo',9,'45745414','daniel@coes.com','ACTIVO','Daniel Martinez','$2a$10$a8WsF0/WCZBnqGhfYIpmQ.EhkDE/5o1wQQgovrTdxWoVJDSFWtNgu','Odontologo','968587478'),('Paciente',11,'87574877','nayeli@gmail.com','ACTIVO','Nayeli Castillo','$2a$10$sgBKZQpMIufVqHuUWxtSlun08RhFu/wVG73fA4MqByGaEWF9CvAiG','Paciente','989685325'),('Odontologo',12,'58587457','roger@coes.com','ACTIVO','Roger Velasco','$2a$10$erywDyRJQJXdHhiYOftiY.hLHczeb9H8TlR8r67mnqn2A/26xltOm','Odontologo','958956847'),('Paciente',13,'85234174','willy@gmail.com','ACTIVO','Willy Rios','$2a$10$.9CO.U5fFVxx8gQmXwuoNeaeSwFAOLWyIs/075qa6PgBYxSM9Zgie','Paciente','989999656'),('Recepcionista',14,'89856858','marcelo@coes.com','ACTIVO','Marcelo Alarcon','$2a$10$y0vq7ZL3PVar71fc4Aa9/.nlMJjb.AbKyhxnzVl6puD7Fi9oLCs8a','Recepcionista','986858555');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-21 18:53:16
