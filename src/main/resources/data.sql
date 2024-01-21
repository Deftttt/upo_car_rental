INSERT INTO car_model(brand, model, n_of_seats, fuel_type, transmition_type, required_category, price, description, active) VALUES
('Skoda', 'Octavia', 5, 'DIESEL', 'AUTOMATIC', 'B', 160, 'Opis samochodu Skoda Octavia', true),
('Honda', 'Civic', 5, 'GASOLINE', 'MANUAL', 'B', 220, 'Opis samochodu Honda Civic', true),
('Volkswagen', 'Golf', 5, 'DIESEL', 'AUTOMATIC', 'B', 180, 'Opis samochodu Volkswagen Golf', true),
('Skoda', 'Fabia', 5, 'GASOLINE', 'AUTOMATIC', 'B', 600, 'Opis samochodu', true),
('BWM', 'E46', 5, 'DIESEL', 'MANUAL', 'B', 400, 'Opis samochodu', true),
('Fiat', 'Punto', 5, 'GASOLINE', 'MANUAL', 'B', 300, 'Opis samochodu', true),
('Toyota', 'Camry', 5, 'GASOLINE', 'AUTOMATIC', 'C', 700, 'Opis samochodu', true),
('Ford', 'Focus', 5, 'GASOLINE', 'MANUAL', 'B', 550, 'Opis samochodu', true),
('Chevrolet', 'Malibu', 5, 'GASOLINE', 'AUTOMATIC', 'C', 750, 'Opis samochodu', true),
('Mercedes-Benz', 'C-Class', 5, 'DIESEL', 'AUTOMATIC', 'C', 900, 'Description of the car', true),
('Renault', 'Megane', 5, 'GASOLINE', 'MANUAL', 'B', 550, 'Description of the car', true),
('Hyundai', 'i20', 5, 'GASOLINE', 'MANUAL', 'C', 500, 'Description of the car', true),
('Kia', 'Soul', 5, 'ELECTRIC', 'AUTOMATIC', 'B', 1000, 'Description of the car', true),
('Mazda', '3', 5, 'GASOLINE', 'AUTOMATIC', 'C', 750, 'Description of the car', true),
('Subaru', 'Impreza', 5, 'GASOLINE', 'MANUAL', 'B', 600, 'Description of the car', true),
('Jaguar', 'XF', 5, 'DIESEL', 'AUTOMATIC', 'C', 1200, 'Description of the car', true),
('Lexus', 'IS', 5, 'GASOLINE', 'AUTOMATIC', 'B', 1100, 'Description of the car', true);

INSERT INTO car(registration_number, latitude, longitude, car_model_id, car_status) VALUES
('RP37388', 50.0710, 19.9427, 1, 'IN_USE'),
('RP67990', 50.0311, 19.9127, 1, 'IN_USE'),
('RP77722', 50.0303, 19.9108, 2, 'IN_USE'),
('KR92013', 50.0274, 19.9483, 3, 'IN_USE'),
('KR90139', 50.0815, 19.9624, 4, 'IN_USE'),
('KR56811', 50.0687, 19.9071, 4, 'IN_REPAIR');

INSERT INTO address(city, street, local_number, post_code) VALUES
('Przemysl', 'Opalinskiego', '17/71', '37-700'),
('Przemysl', 'Grunwaldzka', '13', '37-700'),
('Krakow', 'Norymberska', '10a/37', '31-174'),
('Krakow', 'Szlak', '65', '31-153'),
('Krakow', 'Pawia', '5', '31-154'),
('Balice', 'Medweckiego', '43', '32-083'),
('Kraków', 'Prokocimska', '47', '30-556');



INSERT INTO image(car_model_id, location) VALUES
(1, '001.png'),
(2, '002.png'),
(2, '003.png'),
(2, '004.png'),
(3, '005.png');

INSERT INTO hire_point(name, address_id) VALUES
('Pod kauflandem ul. Norymberska', 3),
('Obok politechniki ul. Szlak', 4),
('Galeria Krakowska - Parking', 5),
('Balice Lotnisko ul. Medweckiego', 6),
('Dworzec Płaszów ul. Prokocimska', 7);


INSERT INTO additional_cost(name, description, price) VALUES
('Fotelik', 'Samochód zostanie doposażony w fotelik dla dziecka', 50),
('Pełne Ubezpieczenie', 'Dodatkowe ubezpieczenie na wypadek szkód', 80),
('Klakta dla psa', 'Klatka dla małego psa lub innego zwierzaka na tylnim siedzeniu', 40),
('Hak na rower', 'Doposażenie o hak do trzymania roweru', 40),
('Zwrot Samochodu w Innej Lokalizacji', 'Możliwość zwrotu samochodu poza wyznaczoną lokalizacją na terenie miasta', 100),
('System Nagłośnienia Premium', 'Wysokiej jakości system dźwiękowy w samochodzie', 50);





