package com.upo.springtest.controller;

import com.upo.springtest.dto.BookingDto;
import com.upo.springtest.dto.BookingUpdateDto;
import com.upo.springtest.enums.BookingStatus;
import com.upo.springtest.model.*;
import com.upo.springtest.service.*;
import com.upo.springtest.util.PageUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static com.upo.springtest.util.Constants.DEFAULT_BOOKINGS_PER_PAGE;

@Slf4j
@Controller
public class BookingController {
    private final BookingService bookingService;
    private final HirePointService hirePointService;
    private final UserService userService;
    private final CarService carService;
    private final EmailService emailService;

    @Autowired
    public BookingController(BookingService bookingService, HirePointService hirePointService, UserService userService, CarService carService, EmailService emailService) {
        this.bookingService = bookingService;
        this.hirePointService = hirePointService;
        this.userService = userService;
        this.carService = carService;
        this.emailService = emailService;
    }


    @GetMapping("/bookACar")
    public ModelAndView bookACar(BookingDto bookingDto, @RequestParam long modelId){
        List<HirePoint> hirePoints = hirePointService.getHirePoints();
        List<AdditionalCost> additionalCosts = bookingService.getAdditionalCosts();
        CarModel carModel = carService.getSingleModel(modelId);

        ModelAndView mav = new ModelAndView("booking/book-car");
        mav.addObject("model", carModel);
        mav.addObject("booking", bookingDto);
        mav.addObject("pickupLocation", hirePoints);
        mav.addObject("returnLocation", hirePoints);
        mav.addObject("additionalCosts", additionalCosts);
        return mav;
    }


    @PostMapping("/bookACar")
    public String bookACar(@Valid BookingDto bookingDto, BindingResult result, Principal principal, RedirectAttributes redirectAttributes) {

        Customer customer = userService.getCustomerByUsername(principal.getName());
        Employee employee = userService.getCustomerServiceEmployeeWithMinBookings();

        bookingService.validateBookingAndHandleErrors(bookingDto, customer, result);

        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            redirectAttributes.addFlashAttribute("errors", allErrors);
            return "redirect:/bookACar?modelId=" + bookingDto.getCarModelId();
        }

        Car car = carService.getAvailableCars(bookingDto).get(0);
        Booking booking = bookingService.addBooking(bookingDto, customer, employee, car);
        redirectAttributes.addFlashAttribute("booking", booking);

        // Email
        String emailBody = emailService.formatEmailContent(booking);
        emailService.sendEmail(booking.getCustomer().getUser().getEmail(), "Potwierdzenie wypożyczenia", emailBody);

        return "redirect:/bookings/success/" + booking.getId();
    }

    @GetMapping("/bookings/success/{bookingId}")
    public ModelAndView bookingSuccess(@PathVariable long bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        float bookingCost = bookingService.calculateBookingCost(booking);
        float totalBookingCost = bookingService.calculateTotalBookingCost(booking);

        ModelAndView mav = new ModelAndView("booking/bookings-success");
        mav.addObject("booking", booking);
        mav.addObject("bookingCost", bookingCost);
        mav.addObject("totalBookingCost", totalBookingCost);
        return mav;
    }


    @GetMapping("/bookings")
    public ModelAndView getBookings(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<Long> employeeId,
            @RequestParam Optional<Long> customerId,
            @RequestParam Optional<Long> carId,
            @RequestParam Optional<BookingStatus> bookingStatus
    ) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(DEFAULT_BOOKINGS_PER_PAGE);

        ModelAndView mav = new ModelAndView("booking/bookings-list");

        Page<Booking> bookingPage;

        if (employeeId.isPresent()) {
            Employee employee = userService.getSingleEmployee(employeeId.get());
            mav.addObject("employee", employee);
            bookingPage = bookingService.getAllBookingsForEmployee(employee, bookingStatus.orElse(null), PageRequest.of(currentPage - 1, pageSize));
        }

        else if (customerId.isPresent()) {
            Customer customer = userService.getSingleCustomer(customerId.get());
            mav.addObject("customer", customer);
            bookingPage = bookingService.getAllBookingsForCustomer(customer, bookingStatus.orElse(null), PageRequest.of(currentPage - 1, pageSize));
        }

        else if (carId.isPresent()) {
            Car car = carService.getSingleCar(carId.get());
            mav.addObject("car", car);
            bookingPage = bookingService.getAllBookingsForCar(car, bookingStatus.orElse(null), PageRequest.of(currentPage - 1, pageSize));
        }

        else {
            bookingPage = bookingService.getAllBookings(bookingStatus.orElse(null), PageRequest.of(currentPage - 1, pageSize));
        }

        PageUtils.addPagesToModelAndView(mav, bookingPage, "bookingPage");
        mav.addObject("bookingStatus", bookingStatus.orElse(null));
        return mav;
    }


    @GetMapping("/bookings/{id}")
    public ModelAndView getBookingDetails(@PathVariable long id){

        Booking booking = bookingService.getBookingDetails(id);
        float bookingCost = bookingService.calculateBookingCost(booking);
        float totalBookingCost = bookingService.calculateTotalBookingCost(booking);

        ModelAndView mav = new ModelAndView("booking/bookings-details");
        mav.addObject("booking", booking);
        mav.addObject("bookingCost", bookingCost);
        mav.addObject("totalBookingCost", totalBookingCost);
        return mav;
    }

    @PostMapping("/bookings/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        Booking canceledBooking = bookingService.cancelBooking(bookingId);
        return "redirect:/account";
    }

    @GetMapping("/bookings/update/{bookingId}")
    public ModelAndView updateBooking(@PathVariable Long bookingId) {
        BookingUpdateDto bookingUpdateDto = bookingService.getBookingUpdateDto(bookingId);
        ModelAndView mav = new ModelAndView("booking/bookings-update");
        mav.addObject("bookingUpdateDto", bookingUpdateDto);

        return mav;
    }

    @PostMapping("/bookings/update/{bookingId}")
    public String updateBooking(@PathVariable Long bookingId, @ModelAttribute BookingUpdateDto bookingUpdateDto, BindingResult result) {
        if (result.hasErrors()) {
            return "booking/bookings-update";
        }
        Booking booking = bookingService.editBooking(bookingUpdateDto, bookingId);
        return "redirect:/bookings/" + booking.getId();
    }






}
