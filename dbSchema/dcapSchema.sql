-- MySQL dump 10.13  Distrib 8.0.19, for Linux (x86_64)
--
-- Host: localhost    Database: new_cheetah_database
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `data_processing_steps`
--

DROP TABLE IF EXISTS `data_processing_steps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `data_processing_steps` (
  `pk_data_processing_step` bigint unsigned NOT NULL AUTO_INCREMENT,
  `fk_data_processing` bigint unsigned NOT NULL,
  `type` varchar(255) NOT NULL,
  `version` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `configuration` json DEFAULT NULL,
  PRIMARY KEY (`pk_data_processing_step`),
  KEY `fk_data_processing_from_data_processing_step` (`fk_data_processing`),
  CONSTRAINT `FK_data_processing_step_data_processing` FOREIGN KEY (`fk_data_processing`) REFERENCES `data_processings` (`pk_data_processing`),
  CONSTRAINT `FKc9h1h2c3hhs9jyv3x78bbe90q` FOREIGN KEY (`fk_data_processing`) REFERENCES `data_processings` (`pk_data_processing`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_processings`
--

DROP TABLE IF EXISTS `data_processings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `data_processings` (
  `pk_data_processing` bigint unsigned NOT NULL AUTO_INCREMENT,
  `fk_study` bigint unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `comment` varchar(1024) DEFAULT NULL,
  `timestamp_column` varchar(255) DEFAULT NULL,
  `left_pupil_column` varchar(255) DEFAULT NULL,
  `right_pupil_column` varchar(255) DEFAULT NULL,
  `decimal_separator` varchar(10) NOT NULL DEFAULT '.',
  `trial_computation_configuration` json DEFAULT NULL,
  PRIMARY KEY (`pk_data_processing`),
  KEY `fk_study_from_data_processing` (`fk_study`),
  CONSTRAINT `FK_data_processing_study` FOREIGN KEY (`fk_study`) REFERENCES `studies` (`pk_study`),
  CONSTRAINT `FKt492pacheqj3x9nyskumbc4it` FOREIGN KEY (`fk_study`) REFERENCES `studies` (`pk_study`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events` (
  `pk_event` bigint unsigned NOT NULL AUTO_INCREMENT,
  `fk_subject` bigint unsigned DEFAULT NULL,
  `time_start` bigint unsigned DEFAULT NULL,
  `time_end` bigint unsigned NOT NULL,
  `type` varchar(256) NOT NULL,
  `name` varchar(256) DEFAULT NULL,
  `attributes` json DEFAULT NULL,
  PRIMARY KEY (`pk_event`),
  KEY `FK_event_subject` (`fk_subject`),
  CONSTRAINT `FK2owqg1ie3g6l1877fnokev6er` FOREIGN KEY (`fk_subject`) REFERENCES `subjects` (`pk_subject`),
  CONSTRAINT `FK_event_subject` FOREIGN KEY (`fk_subject`) REFERENCES `subjects` (`pk_subject`)
) ENGINE=InnoDB AUTO_INCREMENT=50225 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `pk_notification` bigint unsigned NOT NULL AUTO_INCREMENT,
  `fk_user` bigint unsigned NOT NULL,
  `message` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `is_read` bit(1) NOT NULL,
  `timestamp` datetime NOT NULL,
  `task_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pk_notification`),
  KEY `FK_notification_user` (`fk_user`),
  CONSTRAINT `FK_notification_user` FOREIGN KEY (`fk_user`) REFERENCES `users` (`pk_user`),
  CONSTRAINT `FKk0kp2tqotn6hw5019m099k7ie` FOREIGN KEY (`fk_user`) REFERENCES `users` (`pk_user`)
) ENGINE=InnoDB AUTO_INCREMENT=3283 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pythoncode`
--

DROP TABLE IF EXISTS `pythoncode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pythoncode` (
  `pk_pythoncode` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `parameteradress` varchar(255) NOT NULL,
  `codeadress` varchar(255) NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pk_pythoncode`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settings` (
  `pk_settings` bigint unsigned NOT NULL AUTO_INCREMENT,
  `key_column` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`pk_settings`),
  UNIQUE KEY `key_column` (`key_column`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `studies`
--

DROP TABLE IF EXISTS `studies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studies` (
  `pk_study` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `comment` longtext,
  `synchronized_from` bigint unsigned DEFAULT NULL,
  `fk_user` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`pk_study`),
  KEY `FKgb7d1rr49rbjmv6kfpgrgnncn` (`fk_user`),
  CONSTRAINT `fk_user_studies` FOREIGN KEY (`fk_user`) REFERENCES `users` (`pk_user`),
  CONSTRAINT `FKgb7d1rr49rbjmv6kfpgrgnncn` FOREIGN KEY (`fk_user`) REFERENCES `users` (`pk_user`)
) ENGINE=InnoDB AUTO_INCREMENT=1317 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `studies_to_user`
--

DROP TABLE IF EXISTS `studies_to_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studies_to_user` (
  `fk_user` bigint NOT NULL,
  `fk_study` bigint NOT NULL,
  PRIMARY KEY (`fk_study`,`fk_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subjects`
--

DROP TABLE IF EXISTS `subjects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subjects` (
  `pk_subject` bigint unsigned NOT NULL AUTO_INCREMENT,
  `fk_study` bigint unsigned DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `subject_id` varchar(255) NOT NULL,
  `comment` longtext,
  `synchronized_from` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`pk_subject`),
  KEY `FK_subject_study` (`fk_study`),
  CONSTRAINT `FK_subject_study` FOREIGN KEY (`fk_study`) REFERENCES `studies` (`pk_study`),
  CONSTRAINT `FKj084nhy23v4s8q32kqhnklpfh` FOREIGN KEY (`fk_study`) REFERENCES `studies` (`pk_study`)
) ENGINE=InnoDB AUTO_INCREMENT=1992 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `time_phases`
--

DROP TABLE IF EXISTS `time_phases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `time_phases` (
  `pk_time_phase` bigint unsigned NOT NULL AUTO_INCREMENT,
  `fk_subject` bigint unsigned NOT NULL,
  `fk_parent_time_phase` bigint unsigned DEFAULT NULL,
  `type` varchar(1024) NOT NULL,
  `name` varchar(1024) DEFAULT NULL,
  `time_start` bigint unsigned NOT NULL,
  `time_end` bigint unsigned NOT NULL,
  `attributes` json NOT NULL,
  PRIMARY KEY (`pk_time_phase`),
  KEY `FK_time_phase_subject` (`fk_subject`),
  KEY `FK_time_phase_time_phase_idx` (`fk_parent_time_phase`),
  CONSTRAINT `FK_time_phase_subject` FOREIGN KEY (`fk_subject`) REFERENCES `subjects` (`pk_subject`),
  CONSTRAINT `FKhkypxtbtn50hdkjgljqs369y9` FOREIGN KEY (`fk_parent_time_phase`) REFERENCES `time_phases` (`pk_time_phase`),
  CONSTRAINT `FKhs9cv7ubus6e8waimq3b6tg2` FOREIGN KEY (`fk_subject`) REFERENCES `subjects` (`pk_subject`)
) ENGINE=InnoDB AUTO_INCREMENT=46913 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_data`
--

DROP TABLE IF EXISTS `user_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_data` (
  `pk_user_data` bigint unsigned NOT NULL AUTO_INCREMENT,
  `fk_subject` bigint unsigned DEFAULT NULL,
  `fk_derived_from` bigint unsigned DEFAULT NULL,
  `filename` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL,
  `hidden` bit(1) DEFAULT b'0',
  `comment` longtext,
  `start_timestamp` bigint DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `categories` varchar(255) DEFAULT NULL,
  `fk_user` bigint unsigned DEFAULT NULL,
  `task_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pk_user_data`),
  KEY `FK_user_data_subject` (`fk_subject`),
  KEY `FK_user_data_user_data` (`fk_derived_from`),
  KEY `FKeu24sxq2psca4ly022x0cwyrn` (`fk_user`),
  CONSTRAINT `FK2ru1ndjnsojmu59jlo2c1isuy` FOREIGN KEY (`fk_subject`) REFERENCES `subjects` (`pk_subject`),
  CONSTRAINT `FK_user_data_subject` FOREIGN KEY (`fk_subject`) REFERENCES `subjects` (`pk_subject`),
  CONSTRAINT `FK_user_data_user_data` FOREIGN KEY (`fk_derived_from`) REFERENCES `user_data` (`pk_user_data`),
  CONSTRAINT `FKcvemy5s9bd2mkbfnscbi5b90p` FOREIGN KEY (`fk_derived_from`) REFERENCES `user_data` (`pk_user_data`),
  CONSTRAINT `FKeu24sxq2psca4ly022x0cwyrn` FOREIGN KEY (`fk_user`) REFERENCES `users` (`pk_user`),
  CONSTRAINT `user_data_ibfk_1` FOREIGN KEY (`fk_user`) REFERENCES `users` (`pk_user`)
) ENGINE=InnoDB AUTO_INCREMENT=23514 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_data_tags`
--

DROP TABLE IF EXISTS `user_data_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_data_tags` (
  `pk_user_data_tags` bigint unsigned NOT NULL AUTO_INCREMENT,
  `fk_user_data` bigint unsigned NOT NULL,
  `tag` varchar(255) NOT NULL,
  PRIMARY KEY (`pk_user_data_tags`),
  KEY `fk_user_data_tags_to_user_data` (`fk_user_data`),
  CONSTRAINT `FK_user_data_tags_user_data` FOREIGN KEY (`fk_user_data`) REFERENCES `user_data` (`pk_user_data`),
  CONSTRAINT `FKnjqydfe8k8ca7r3mgw3w9entg` FOREIGN KEY (`fk_user_data`) REFERENCES `user_data` (`pk_user_data`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `pk_user` bigint unsigned NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`pk_user`),
  UNIQUE KEY `user_table_email_idx` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=512 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-05 10:32:39
