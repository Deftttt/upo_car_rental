package com.upo.springtest.util;

import com.upo.springtest.enums.BookingStatus;
import com.upo.springtest.enums.EmployeePosition;
import com.upo.springtest.enums.Role;
import com.upo.springtest.model.*;
import com.upo.springtest.service.BookingService;
import com.upo.springtest.service.CarService;
import com.upo.springtest.service.HirePointService;
import com.upo.springtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class DataLoader {
    private final UserService userService;
    private final CarService carService;
    private final HirePointService hirePointService;
    private final PasswordEncoder passwordEncoder;
    private final BookingService bookingService;



    @Autowired
    public DataLoader(UserService userService, CarService carService, HirePointService hirePointService, PasswordEncoder passwordEncoder, BookingService bookingService) {
        this.userService = userService;
        this.carService = carService;
        this.hirePointService = hirePointService;
        this.passwordEncoder = passwordEncoder;
        this.bookingService = bookingService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void get(){


        Address a1 = new Address("Przemyśl", "Krucza", "3", "37-700");
        Address a2 = new Address("Kraków", "Norymberska", "10a/37", "31-174");
        Address a3 = new Address("Kraków", "Szlak", "89", "31-153");
        Address a4 = new Address("Przemyśl", "Opalińskiego", "17/71", "37-700");
        Address a5 = new Address("Kraków", "Pawia", "18", "31-154");


        User customerUser = new User("Piotr", "Stasicki", "piotrstasicki@gmail.com", "657909087",
                "customer", passwordEncoder.encode("customer"), Role.CUSTOMER, a1);
        User customerUser2 = new User("Adam", "Michnik", "adammichnik@gmail.com", "435678099",
                "customer2", passwordEncoder.encode("customer2"), Role.CUSTOMER, a5);
        User employeeUser = new User("Karol", "Wojtyła", "employee@gmail.com", "678918099",
                "employee", passwordEncoder.encode("employee"), Role.EMPLOYEE, a2);
        User employeeUser2 = new User("Adam", "Małysz", "employee2@gmail.com", "698138654",
                "employee2", passwordEncoder.encode("employee2"), Role.EMPLOYEE, a3);
        User adminUser = new User("Hubert", "Kwiatkowski", "admin@gmail.com", "90674178",
                "admin", passwordEncoder.encode("admin"), Role.ADMIN, a4);

        Customer c1 = new Customer("00258/20/1862", "1234567812345678", customerUser);
        Customer c2 = new Customer("00167/18/1705", "9876543212345678", customerUser2);
        Employee e1 = new Employee(EmployeePosition.CUSTOMER_SERVICE, employeeUser, 4500);
        Employee e2 = new Employee(EmployeePosition.CUSTOMER_SERVICE, employeeUser2, 5400);

        userService.addCustomer(c1);
        userService.addCustomer(c2);
        userService.addEmployee(e1);
        userService.addEmployee(e2);
        userService.addUser(adminUser);

        List<Car> cars = carService.getCars();
        List<HirePoint> hirePoints = hirePointService.getHirePoints();
        List<AdditionalCost> additionalCosts = bookingService.getAdditionalCosts();


        Booking booking1 = Booking.builder()
                .pickupDate(Timestamp.valueOf("2024-01-15 10:00:00"))
                .returnDate(Timestamp.valueOf("2024-01-20 15:00:00"))
                .car(cars.get(0))
                .customer(c1)
                .employee(e1)
                .pickupLocation(hirePoints.get(0))
                .returnLocation(hirePoints.get(1))
                .bookingStatus(BookingStatus.ACTIVE)
                .additionalCosts(Set.of(additionalCosts.get(0), additionalCosts.get(1)))
                .build();

        Booking booking2 = Booking.builder()
                .pickupDate(Timestamp.valueOf("2024-01-15 13:30:00"))
                .returnDate(Timestamp.valueOf("2024-01-20 12:00:00"))
                .car(cars.get(0))
                .customer(c2)
                .employee(e1)
                .pickupLocation(hirePoints.get(1))
                .returnLocation(hirePoints.get(2))
                .bookingStatus(BookingStatus.ACTIVE)
                .additionalCosts(Set.of(additionalCosts.get(1), additionalCosts.get(3)))
                .build();

        Booking booking3 = Booking.builder()
                .pickupDate(Timestamp.valueOf("2024-02-01 08:30:00"))
                .returnDate(Timestamp.valueOf("2024-02-10 12:00:00"))
                .car(cars.get(1))
                .customer(c2)
                .employee(e1)
                .pickupLocation(hirePoints.get(2))
                .returnLocation(hirePoints.get(3))
                .bookingStatus(BookingStatus.ACTIVE)
                .additionalCosts(Set.of(additionalCosts.get(2)))
                .build();

        Booking booking4 = Booking.builder()
                .pickupDate(Timestamp.valueOf("2024-03-10 12:00:00"))
                .returnDate(Timestamp.valueOf("2024-03-15 18:00:00"))
                .car(cars.get(1))
                .customer(c1)
                .employee(e2)
                .pickupLocation(hirePoints.get(3))
                .returnLocation(hirePoints.get(3))
                .bookingStatus(BookingStatus.ACTIVE)
                .additionalCosts(Set.of(additionalCosts.get(3)))
                .build();

        Booking booking5 = Booking.builder()
                .pickupDate(Timestamp.valueOf("2024-03-10 12:00:00"))
                .returnDate(Timestamp.valueOf("2024-03-15 18:00:00"))
                .car(cars.get(2))
                .customer(c2)
                .employee(e2)
                .pickupLocation(hirePoints.get(4))
                .returnLocation(hirePoints.get(4))
                .bookingStatus(BookingStatus.CANCELED)
                .additionalCosts(Set.of(additionalCosts.get(0), additionalCosts.get(1)))
                .build();

        Booking booking6 = Booking.builder()
                .pickupDate(Timestamp.valueOf("2024-03-10 12:00:00"))
                .returnDate(Timestamp.valueOf("2024-03-15 18:00:00"))
                .car(cars.get(3))
                .customer(c2)
                .employee(e2)
                .pickupLocation(hirePoints.get(1))
                .returnLocation(hirePoints.get(0))
                .bookingStatus(BookingStatus.CANCELED)
                .additionalCosts(Set.of(additionalCosts.get(2)))
                .build();

        Booking booking7 = Booking.builder()
                .pickupDate(Timestamp.valueOf("2024-03-10 12:00:00"))
                .returnDate(Timestamp.valueOf("2024-03-15 18:00:00"))
                .car(cars.get(0))
                .customer(c1)
                .employee(e1)
                .pickupLocation(hirePoints.get(0))
                .returnLocation(hirePoints.get(2))
                .bookingStatus(BookingStatus.FINISHED)
                .additionalCosts(Set.of(additionalCosts.get(2)))
                .build();

        Booking booking8 = Booking.builder()
                .pickupDate(Timestamp.valueOf("2024-03-10 12:00:00"))
                .returnDate(Timestamp.valueOf("2024-03-15 18:00:00"))
                .car(cars.get(2))
                .customer(c1)
                .employee(e2)
                .pickupLocation(hirePoints.get(1))
                .returnLocation(hirePoints.get(2))
                .bookingStatus(BookingStatus.FINISHED)
                .additionalCosts(Set.of(additionalCosts.get(2)))
                .build();



        bookingService.addBooking(booking1);
        bookingService.addBooking(booking2);
        bookingService.addBooking(booking3);
        bookingService.addBooking(booking4);
        bookingService.addBooking(booking5);
        bookingService.addBooking(booking6);
        bookingService.addBooking(booking7);
        bookingService.addBooking(booking8);
    }
}
