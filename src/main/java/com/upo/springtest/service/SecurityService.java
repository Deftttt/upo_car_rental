package com.upo.springtest.service;

import com.upo.springtest.model.Booking;
import com.upo.springtest.model.CarModel;

import com.upo.springtest.model.User;
import com.upo.springtest.repository.BookingRepository;
import com.upo.springtest.repository.CarModelRepository;
import com.upo.springtest.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityService {

    private final CarModelRepository carModelRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Autowired
    public SecurityService(CarModelRepository carModelRepository, BookingRepository bookingRepository, UserRepository userRepository) {
        this.carModelRepository = carModelRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    public boolean canAccessBooking(long bookingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        User user = userRepository.findByUsername(authentication.getName());

        return (booking.getEmployee().getUser().getId().equals(user.getId()) || booking.getCustomer().getUser().getId().equals(user.getId()));

    }

    public boolean isActiveModel(long modelId) {
        CarModel carModel = carModelRepository.findById(modelId).orElseThrow();
        return carModel.getActive();
    }

}