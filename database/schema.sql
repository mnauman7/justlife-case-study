CREATE DATABASE IF NOT EXISTS app_db;

-- Creating db user for app-service
CREATE USER 'app_server'@'%' IDENTIFIED BY 'apppassword';
GRANT ALL PRIVILEGES ON app_db.* TO 'app_server'@'%' WITH GRANT OPTION;

USE app_db;

CREATE TABLE IF NOT EXISTS Users (
  user_id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL,
  last_name VARCHAR(30) NOT NULL,
  address VARCHAR(255),
  city VARCHAR(80),
  phone VARCHAR(20),
  is_admin BOOL NOT NULL DEFAULT 0, 
  is_active BOOL NOT NULL DEFAULT 1,
  email varchar(255) NOT NULL,
  password varchar(100) NOT NULL,
  INDEX(last_name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS Vehicles (
  vehicle_id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  vehicle_name VARCHAR(30) NOT NULL,
  capacity TINYINT UNSIGNED NOT NULL DEFAULT 5,
  phone VARCHAR(20),
  is_active BOOL NOT NULL DEFAULT 1
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS JobTypeMaster (
  job_type_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  job_type_name VARCHAR(30) NOT NULL,
  is_active BOOL NOT NULL DEFAULT 1
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS Staff (
  staff_id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL,
  last_name VARCHAR(30) NOT NULL,
  phone VARCHAR(20),
  job_type_id TINYINT UNSIGNED NOT NULL,
  vehicle_id INT(4) UNSIGNED NOT NULL,
  is_active BOOL NOT NULL DEFAULT 1,
  FOREIGN KEY (job_type_id) REFERENCES JobTypeMaster(job_type_id),
  FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
) engine=InnoDB;