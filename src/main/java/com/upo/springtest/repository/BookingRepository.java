package com.upo.springtest.repository;

import com.upo.springtest.enums.BookingStatus;
import com.upo.springtest.model.Booking;
import com.upo.springtest.model.CarModel;
import com.upo.springtest.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    List<Booking> findAllBookingsByEmployeeId(long employeeId);

    List<Booking> findAllBookingsByCustomerId(long customerId);

    List<Booking> findAllBookingsByCarId(long carId);

    List<Booking> findAllBookingsByEmployeeIdAndBookingStatus(long employeeId, BookingStatus bookingStatus);

    List<Booking> findAllBookingsByCustomerIdAndBookingStatus(long customerId, BookingStatus bookingStatus);

    List<Booking> findAllBookingsByCarIdAndBookingStatus(long carId, BookingStatus bookingStatus);

    List<Booking> findAllBookingsByBookingStatus(BookingStatus bookingStatus);

}
