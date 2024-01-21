package com.upo.springtest.service;

import com.upo.springtest.dto.BookingDto;
import com.upo.springtest.dto.BookingUpdateDto;
import com.upo.springtest.enums.BookingStatus;
import com.upo.springtest.enums.Category;
import com.upo.springtest.model.*;
import com.upo.springtest.repository.AdditionalCostRepository;
import com.upo.springtest.repository.BookingRepository;
import com.upo.springtest.repository.EmployeeRepository;
import com.upo.springtest.repository.UserRepository;
import com.upo.springtest.util.Constants;
import com.upo.springtest.util.DrivingLicenseValidator;
import com.upo.springtest.util.PageUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final AdditionalCostRepository additionalCostRepository;
    private final ModelMapper modelMapper;
    private final CarService carService;


    @Autowired
    public BookingService(BookingRepository bookingRepository, AdditionalCostRepository additionalCostRepository, ModelMapper modelMapper, UserRepository userRepository, EmployeeRepository employeeRepository, CarService carService) {
        this.bookingRepository = bookingRepository;
        this.additionalCostRepository = additionalCostRepository;
        this.modelMapper = modelMapper;
        this.carService = carService;
    }

    public Booking addBooking(BookingDto bookingDto, Customer customer, Employee employee, Car car) {
        Booking booking = Booking.builder()
                .pickupDate(bookingDto.getPickupDate())
                .returnDate(bookingDto.getReturnDate())
                .pickupLocation(bookingDto.getPickupLocation())
                .returnLocation(bookingDto.getReturnLocation())
                .customer(customer)
                .additionalCosts(bookingDto.getAdditionalCosts())
                .employee(employee)
                .car(car)
                .bookingStatus(BookingStatus.ACTIVE)
                .build();
        return bookingRepository.save(booking);
    }

    public Booking editBooking(BookingUpdateDto bookingUpdateDto, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setBookingStatus(bookingUpdateDto.getBookingStatus());
        booking.setComment(bookingUpdateDto.getComment());

        return bookingRepository.save(booking);
    }

    public Booking addBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setBookingStatus(BookingStatus.CANCELED);

        return bookingRepository.save(booking);
    }


    public Booking getBookingById(long id){
        return bookingRepository.findById(id).orElseThrow();
    }

    public BookingUpdateDto getBookingUpdateDto(long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        return modelMapper.map(booking, BookingUpdateDto.class);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public Page<Booking> getAllBookings(BookingStatus bookingStatus, Pageable pageable) {

        List<Booking> list;
        if (bookingStatus != null) {
            list = bookingRepository.findAllBookingsByBookingStatus(bookingStatus);
        } else {
            list = bookingRepository.findAll();
        }
        List<Booking> filteredList = PageUtils.getPageContent(list, pageable);

        return new PageImpl<>(filteredList, pageable, list.size());
    }

    @PreAuthorize("hasAuthority('ADMIN') or #employee.user.username == authentication.name")
    public Page<Booking> getAllBookingsForEmployee(Employee employee, BookingStatus bookingStatus, Pageable pageable){
        List<Booking> list;
        if (bookingStatus != null) {
            list = bookingRepository.findAllBookingsByEmployeeIdAndBookingStatus(employee.getId(), bookingStatus);
        } else {
            list = bookingRepository.findAllBookingsByEmployeeId(employee.getId());
        }

        List<Booking> filteredList = PageUtils.getPageContent(list, pageable);

        return new PageImpl<>(filteredList, pageable, list.size());
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE') or #customer.user.username == authentication.name")
    public Page<Booking> getAllBookingsForCustomer(Customer customer, BookingStatus bookingStatus, Pageable pageable){

        List<Booking> list;
        if (bookingStatus != null) {
            list = bookingRepository.findAllBookingsByCustomerIdAndBookingStatus(customer.getId(), bookingStatus);
        } else {
            list = bookingRepository.findAllBookingsByCustomerId(customer.getId());
        }

        List<Booking> filteredList = PageUtils.getPageContent(list, pageable);

        return new PageImpl<>(filteredList, pageable, list.size());
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public Page<Booking> getAllBookingsForCar(Car car, BookingStatus bookingStatus, Pageable pageable){
        List<Booking> list;
        if (bookingStatus != null) {
            list = bookingRepository.findAllBookingsByCarIdAndBookingStatus(car.getId(), bookingStatus);
        } else {
            list = bookingRepository.findAllBookingsByCarId(car.getId());
        }
        List<Booking> filteredList = PageUtils.getPageContent(list, pageable);

        return new PageImpl<>(filteredList, pageable, list.size());
    }

    @PreAuthorize("hasAuthority('ADMIN') or  @securityService.canAccessBooking(#bookingId)")
    public Booking getBookingDetails(long bookingId){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        return booking;

    }

    public List<AdditionalCost> getAdditionalCosts(){
        return additionalCostRepository.findAll();
    }



    public float calculateBookingCost(Booking booking) {
        LocalDateTime pickupDateTime = booking.getPickupDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime returnDateTime = booking.getReturnDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Duration duration = Duration.between(pickupDateTime, returnDateTime);
        long numberOfDays = duration.toDays();
        if (duration.toHoursPart() > 0 || duration.toMinutesPart() > 0) {
            numberOfDays++;
        }

        Car car = booking.getCar();
        CarModel carModel = car.getCarModel();
        float modelPrice = carModel.getPrice();

        return numberOfDays * modelPrice;
    }

    public float calculateTotalBookingCost(Booking booking) {
        float bookingCost = calculateBookingCost(booking);
        float additionalCostsSum = 0.0f;

        for (AdditionalCost additionalCost : booking.getAdditionalCosts()) {
            additionalCostsSum += additionalCost.getPrice();
        }

        return bookingCost + additionalCostsSum;
    }

    public void validateBookingAndHandleErrors(BookingDto bookingDto, Customer customer, BindingResult result) {
        if (bookingDto.getPickupDate().toInstant().isBefore(Instant.now()) ||
                bookingDto.getReturnDate().toInstant().isBefore(bookingDto.getPickupDate().toInstant())) {
            result.rejectValue("pickupDate", null, "Błędnie podana data wypożyczenia!");
        }

        long days = ChronoUnit.DAYS.between(bookingDto.getPickupDate().toInstant(), bookingDto.getReturnDate().toInstant());
        if (days > Constants.MAX_RENT_DAYS) {
            result.rejectValue("returnDate", null, String.format("Przekroczono maksymalny okres wypożyczenia ( %d dni )!", Constants.MAX_RENT_DAYS));
        }

        List<Category> customerCategories = DrivingLicenseValidator.getDrivingCategories(customer.getDrivingLicenseNumber());
        Category carRequiredCategory = carService.getSingleModel(bookingDto.getCarModelId()).getRequiredCategory();
        if (!customerCategories.contains(carRequiredCategory)) {
            result.rejectValue("carModelId", null, String.format("Brak wymaganych uprawnień do wypożyczenia samochodu! ( %s )", carRequiredCategory));
        }

        List<Car> cars = carService.getAvailableCars(bookingDto);
        if (cars.isEmpty()) {
            result.rejectValue("pickupDate", null, "Dla wybranego terminu podany model jest niedostępny!");
        }

    }


}
