CREATE DATABASE IF NOT EXISTS app_db;

-- Creating db user for app-service
CREATE USER 'app_server'@'%' IDENTIFIED BY 'apppassword';
GRANT ALL PRIVILEGES ON app_db.* TO 'app_server'@'%' WITH GRANT OPTION;

USE app_db;

CREATE TABLE IF NOT EXISTS Users (
  user_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL,
  last_name VARCHAR(30) NOT NULL,
  address VARCHAR(255),
  city VARCHAR(80),
  phone VARCHAR(20),
  is_admin BOOL NOT NULL DEFAULT 0, 
  is_active BOOL NOT NULL DEFAULT 1,
  email varchar(255) NOT NULL,
  password varchar(100) NOT NULL
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

CREATE TABLE IF NOT EXISTS TimeSlots (
  slot_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  slot_name VARCHAR(20) NOT NULL
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS Appointments (
  appointment_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT UNSIGNED NOT NULL,
  starting_time_id TINYINT UNSIGNED NOT NULL,
  duration TINYINT UNSIGNED NOT NULL,
  appointment_date DATETIME NOT NULL,
  service_type TINYINT UNSIGNED NOT NULL,
  vehicle_id INT(4) UNSIGNED NOT NULL,
  address VARCHAR(255),
  city VARCHAR(80),
  created_date DATETIME NOT NULL,
  updated_date DATETIME NOT NULL,
  FOREIGN KEY (user_id) REFERENCES Users(user_id),
  FOREIGN KEY (starting_time_id) REFERENCES TimeSlots(slot_id),
  FOREIGN KEY (service_type) REFERENCES JobTypeMaster(job_type_id),
  FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS AppointmentStaff (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  appointment_id BIGINT UNSIGNED NOT NULL,
  staff_id INT(4) UNSIGNED NOT NULL,
  FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id),
  FOREIGN KEY (staff_id) REFERENCES Staff(staff_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS StaffOccupancy (
  occupancy_id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  staff_id INT(4) UNSIGNED NOT NULL,
  time_slot_id TINYINT UNSIGNED NOT NULL,
  occupancy_date DATE NOT NULL,
  occupancy_type_id TINYINT UNSIGNED NOT NULL,
  FOREIGN KEY (staff_id) REFERENCES Staff(staff_id),
  FOREIGN KEY (time_slot_id) REFERENCES TimeSlots(slot_id),
  FOREIGN KEY (occupancy_type_id) REFERENCES StaffOccupancyTypeMaster(occupancy_type_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS StaffOccupancyTypeMaster (
  occupancy_type_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  occupancy_type_name VARCHAR(30) NOT NULL,
  is_active BOOL NOT NULL DEFAULT 1
) engine=InnoDB;