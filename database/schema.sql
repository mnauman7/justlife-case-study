CREATE DATABASE IF NOT EXISTS app_db;

-- Creating db user for app-service
CREATE USER 'app_server'@'%' IDENTIFIED BY 'apppassword';
GRANT ALL PRIVILEGES ON app_db.* TO 'app_server'@'%' WITH GRANT OPTION;

USE app_db;

CREATE TABLE IF NOT EXISTS user (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL,
  last_name VARCHAR(30) NOT NULL,
  address VARCHAR(255),
  city VARCHAR(80),
  telephone VARCHAR(20),
  is_admin BOOL NOT NULL DEFAULT 0, 
  is_active BOOL NOT NULL DEFAULT 1,
  email varchar(255) NOT NULL,
  password varchar(100) NOT NULL,
  INDEX(last_name)
) engine=InnoDB;
