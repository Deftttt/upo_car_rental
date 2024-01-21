package com.upo.springtest.service;

import com.upo.springtest.dto.EmployeeRegisterDto;
import com.upo.springtest.dto.UserDto;
import com.upo.springtest.dto.UserRegisterDto;
import com.upo.springtest.enums.Role;
import com.upo.springtest.model.Address;
import com.upo.springtest.model.Customer;
import com.upo.springtest.model.Employee;
import com.upo.springtest.model.User;
import com.upo.springtest.repository.AddressRepository;
import com.upo.springtest.repository.CustomerRepository;
import com.upo.springtest.repository.EmployeeRepository;
import com.upo.springtest.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public RegistrationService(UserRepository userRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository, AddressRepository addressRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public boolean checkIfUsernameAvailable(String username){
        if(userRepository.findByUsername(username) == null){
            return true;
        }
        return false;
    }

    public boolean checkIfEmailAvailable(String email) {
        if(userRepository.findByEmail(email) == null){
            return true;
        }
        return false;
    }

    public boolean checkIfPasswordsMatch(String password, String passwordConf){
        if(password.equals(passwordConf)){
            return true;
        }
        return false;
    }


    public UserDto getUserDto(long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        UserDto userDto =  modelMapper.map(user, UserDto.class);
        userDto.setCity(user.getAddress().getCity());
        userDto.setPostCode(user.getAddress().getPostCode());
        userDto.setStreet(user.getAddress().getStreet());
        userDto.setLocalNumber(user.getAddress().getLocalNumber());

        return userDto;
    }

    public User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow();
    }


    public void registerUser(UserRegisterDto userRegisterDto) {

        Address address = Address.builder()
                .city(userRegisterDto.getCity())
                .postCode(userRegisterDto.getPostCode())
                .street(userRegisterDto.getStreet())
                .localNumber(userRegisterDto.getLocalNumber())
                .build();

        User user = User.builder()
                .firstName(userRegisterDto.getFirstName())
                .lastName(userRegisterDto.getLastName())
                .email(userRegisterDto.getEmail())
                .phoneNumber(userRegisterDto.getPhoneNumber())
                .username(userRegisterDto.getUsername())
                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .role(Role.CUSTOMER)
                .address(address)
                .build();

        Customer customer = Customer.builder()
                .user(user)
                .cardNumber(userRegisterDto.getCardNumber())
                .drivingLicenseNumber(userRegisterDto.getDrivingLicenseNumber())
                .build();

        userRepository.save(customer.getUser());
        customerRepository.save(customer);


    }

    public void registerEmployee(EmployeeRegisterDto employeeRegisterDto) {

        Address address = Address.builder()
                .city(employeeRegisterDto.getCity())
                .postCode(employeeRegisterDto.getPostCode())
                .street(employeeRegisterDto.getStreet())
                .localNumber(employeeRegisterDto.getLocalNumber())
                .build();

        User user = User.builder()
                .firstName(employeeRegisterDto.getFirstName())
                .lastName(employeeRegisterDto.getLastName())
                .email(employeeRegisterDto.getEmail())
                .phoneNumber(employeeRegisterDto.getPhoneNumber())
                .username(employeeRegisterDto.getUsername())
                .password(passwordEncoder.encode(employeeRegisterDto.getPassword()))
                .role(Role.CUSTOMER)
                .address(address)
                .build();

        Employee employee = Employee.builder()
                .user(user)
                .position(employeeRegisterDto.getPosition())
                .salary(employeeRegisterDto.getSalary())
                .build();

        userRepository.save(employee.getUser());
        employeeRepository.save(employee);


    }


    public void editUser(UserDto userDto, long userId) {

        User user = userRepository.findById(userId).orElseThrow();
        Address address = user.getAddress();

        address.setCity(userDto.getCity());
        address.setPostCode(userDto.getPostCode());
        address.setStreet(userDto.getStreet());
        address.setLocalNumber(userDto.getLocalNumber());

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAddress(address);


        userRepository.save(user);

        // update userInfo in SpringSecurity
        Collection<SimpleGrantedAuthority> nowAuthorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), nowAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
