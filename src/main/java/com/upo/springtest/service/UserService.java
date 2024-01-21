package com.upo.springtest.service;

import com.upo.springtest.dto.EmployeeDto;
import com.upo.springtest.enums.EmployeePosition;
import com.upo.springtest.enums.Role;
import com.upo.springtest.model.*;
import com.upo.springtest.repository.*;
import com.upo.springtest.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public UserService(AddressRepository addressRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository, BookingRepository bookingRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public EmployeeDto getSingleEmployeeDto(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        return modelMapper.map(employee, EmployeeDto.class);
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAllEmployees();
    }

    public void deleteEmployee(long employeeId) {
        Employee deletedEmployee = getDeletedEmployee();
        List<Booking> bookings = bookingRepository.findAllBookingsByEmployeeId(employeeId);
        for (Booking booking : bookings) {
            booking.setEmployee(deletedEmployee);
        }
        employeeRepository.deleteById(employeeId);
    }

    public Employee getDeletedEmployee() {
        // Sprawdź, czy "Usunięty pracownik" istnieje w bazie danych
        Employee deletedEmployee = employeeRepository.findEmployeeByUserUsername("deleted_employee");

        // Jeżeli nie istnieje, stwórz nowego "Usuniętego pracownika" i zapisz go w bazie danych
        if (deletedEmployee == null) {
            User deletedUser = new User("Deleted", "Employee", "deleted@example.com", "000000000", "deleted_employee", "password", Role.EMPLOYEE, null);
            deletedEmployee = new Employee(EmployeePosition.DELETED, deletedUser, 0);
            addEmployee(deletedEmployee);
        }

        return deletedEmployee;
    }



    public Employee addEmployee(Employee employee) {

        userRepository.save(employee.getUser());
        return employeeRepository.save(employee);
    }


    public Customer addCustomer(Customer customer) {

        userRepository.save(customer.getUser());
        return customerRepository.save(customer);
    }


    public Employee getSingleEmployee(long id) {
        return employeeRepository.findById(id).orElseThrow();
    }

    public Employee getCustomerServiceEmployeeWithMinBookings(){
        return employeeRepository.findCustomerServiceEmployeeWithMinBookings().get(0);
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAllCustomers();
    }

    public Customer getSingleCustomer(long id) {
        return customerRepository.findById(id).orElseThrow();
    }

    public Customer getCustomerByUsername(String username) {
        return customerRepository.findCustomerByUserUsername(username);
    }

    public Employee getEmployeeByUsername(String username) {
        return employeeRepository.findEmployeeByUserUsername(username);
    }

    public Employee editEmployee(EmployeeDto employeeDto, long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        employee.setPosition(employeeDto.getPosition());
        employee.setSalary(employeeDto.getSalary());

        return employeeRepository.save(employee);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }



}
