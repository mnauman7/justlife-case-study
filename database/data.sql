-- Setting up a test user
INSERT INTO app_db.`Users`
(first_name, last_name, address, city, phone, is_admin, is_active, email, password)
VALUES('Nauman', 'Tester', 'house 15, model town', 'lahore', '00923334445556', 1, 1, 'nauman@test.com', 'testing12');

-- Setting up sample vehicles
INSERT INTO app_db.`Vehicles`
(vehicle_id, vehicle_name, capacity, phone)
VALUES(1, 'Van 1', 5, '00923334445557'),
(2, 'Van 2', 5, '00923334445558'),
(3, 'Van 3', 5, '00923334445559'),
(4, 'Van 4', 5, '00923334445560'),
(5, 'Van 5', 5, '00923334445561');

-- Setting up job types
INSERT INTO app_db.`JobTypeMaster`
(job_type_id, job_type_name)
VALUES(1, 'Cleaning'),(2, 'Spa');

-- Setting up staff
INSERT INTO app_db.`Staff`
(first_name, last_name, phone, job_type_id, vehicle_id)
VALUES
('Sara', 'Oki', '00923334445562', 1, 1), -- vehicle 1 staff
('Ali', 'Khan', '00923334445563', 1, 1),
('John', 'Adam', '00923334445564', 1, 1),
('Amna', 'Al', '00923334445565', 1, 1),
('Olive', 'Marsh', '00923334445566', 1, 1),
('Bo', 'Jensen', '00923334445567', 1, 2), -- vehicle 2 staff
('Nora', 'Glass', '00923334445568', 1, 2),
('Amin', 'Fahim', '000923334445569', 1, 2),
('Tayyab', 'Khan', '00923334445570', 1, 2),
('Jack', 'Jo', '00923334445571', 1, 2),
('Alan', 'Bass', '00923334445572', 1, 3), -- vehicle 3 staff
('Sky', 'Chen', '00923334445573', 1, 3),
('Jackie', 'Chan', '00923334445574', 1, 3),
('Shahrukh', 'Khan', '00923334445575', 1, 3),
('Malay', 'Ray', '00923334445576', 1, 3),
('Amna', 'Ali', '00923334445577', 1, 4), -- vehicle 4 staff
('Creed', 'Chan', '00923334445578', 1, 4),
('Walter', 'Vu', '00923334445579', 1, 4),
('Jessie', 'Beck', '00923334445580', 1, 4),
('Umar', 'Farooq', '00923334445581', 1, 4),
('Atif', 'Ejaz', '00923334445582', 1, 5), -- vehicle 5 staff
('Iqra', 'Zafar', '00923334445583', 1, 5),
('Adnan', 'Khan', '00923334445584', 1, 5),
('Mack', 'Ayala', '00923334445585', 1, 5),
('Kole', 'Liu', '00923334445586', 1, 5);


-- Setting up time slots (for booking)
INSERT INTO app_db.`TimeSlots`
(start_time, end_time, slot_name)
VALUES
('8:00:00', '8:30:00', "08:00-08:30"),
('8:30:00', '9:00:00', "08:30-09:00"),
('9:00:00', '9:30:00', "09:00-09:30"),
('9:30:00', '10:00:00', "09:30-10:00"),
('10:00:00', '10:30:00', "10:00-10:30"),
('10:30:00', '11:00:00', "10:30-11:00"),
('11:00:00', '11:30:00', "11:00-11:30"),
('11:30:00', '12:00:00', "11:30-12:00"),
('12:00:00', '12:30:00', "12:00-12:30"),
('12:30:00', '13:00:00', "12:30-13:00"),
('13:00:00', '13:30:00', "13:00-13:30"),
('13:30:00', '14:00:00', "13:30-14:00"),
('14:00:00', '14:30:00', "14:00-14:30"),
('14:30:00', '15:00:00', "14:30-15:00"),
('15:00:00', '15:30:00', "15:00-15:30"),
('15:30:00', '16:00:00', "15:30-16:00"),
('16:00:00', '16:30:00', "16:00-16:30"),
('16:30:00', '17:00:00', "16:30-17:00"),
('17:00:00', '17:30:00', "17:00-17:30"),
('17:30:00', '18:00:00', "17:30-18:00"),
('18:00:00', '18:30:00', "18:00-18:30"),
('18:30:00', '19:00:00', "18:30-19:00"),
('19:00:00', '19:30:00', "19:00-19:30"),
('19:30:00', '20:00:00', "19:30-20:00"),
('20:00:00', '20:30:00', "20:00-20:30"),
('20:30:00', '21:00:00', "20:30-21:00"),
('21:00:00', '21:30:00', "21:00-21:30"),
('21:30:00', '22:00:00', "21:30-22:00");


-- Setting up staff occupancy types
INSERT INTO app_db.`StaffOccupancyTypeMaster`
(occupancy_type_id, occupancy_type_name)
VALUES(1, 'Work'),(2, 'Travelling/Preparing'),(3, 'Vehicle is Busy'),(4, 'Break'),(5, 'Leave');