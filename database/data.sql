-- Setting up a test user
INSERT INTO app_db.`user`
(first_name, last_name, address, city, telephone, is_admin, is_active, email, password)
VALUES('Nauman', 'Tester', 'test town', 'lahore', '1212121212', 1, 1, 'nauman.tester@test.com', 'testing14');