package com.upo.springtest.service;

import com.upo.springtest.dto.BookingDto;
import com.upo.springtest.dto.CarDto;
import com.upo.springtest.dto.CarModelDto;
import com.upo.springtest.enums.CarStatus;
import com.upo.springtest.enums.FuelType;
import com.upo.springtest.enums.TransmitionType;
import com.upo.springtest.model.Booking;
import com.upo.springtest.model.Car;
import com.upo.springtest.model.CarModel;
import com.upo.springtest.model.Employee;
import com.upo.springtest.repository.BookingRepository;
import com.upo.springtest.repository.CarModelRepository;
import com.upo.springtest.repository.CarRepository;
import com.upo.springtest.repository.specification.CarModelSpecification;
import com.upo.springtest.util.Constants;
import com.upo.springtest.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarModelRepository carModelRepository;
    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private final BookingRepository bookingRepository;

    @Autowired
    public CarService(CarModelRepository carModelRepository, CarRepository carRepository, ModelMapper modelMapper, BookingRepository bookingRepository) {
        this.carModelRepository = carModelRepository;
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
        this.bookingRepository = bookingRepository;
    }

    // Car

    public List<Car> getCars() {
        return carRepository.findAllCars();
    }

    public Car getSingleCar(long id) {
        return carRepository.findById(id).orElseThrow();
    }


    public Car addCar(CarDto carDto) {
        CarModel model = carModelRepository.findById(carDto.getCarModelId()).orElseThrow();
        Car car = Car.builder()
                .registrationNumber(carDto.getRegistrationNumber())
                .carModel(model)
                .carStatus(carDto.getCarStatus())
                .latitude(Constants.BASE_LATITUDE)
                .longitude(Constants.BASE_LONGITUDE)
                .build();
        return carRepository.save(car);
    }


    public List<Car> getAvailableCars(BookingDto bookingDto) {
        CarModel model = getSingleModel(bookingDto.getCarModelId());

        List<Car> cars = carRepository.findAvailableCarsForModelAndDateRange(model, bookingDto.getPickupDate(), bookingDto.getReturnDate());
        return cars;
    }

    public CarDto getSingleCarDto(long carId) {
        Car car = carRepository.findById(carId).orElseThrow();
        return modelMapper.map(car, CarDto.class);
    }

    public Car editCar(CarDto carDto, long carId) {
        Car car = carRepository.findById(carId).orElseThrow();
        CarModel model = carModelRepository.findById(carDto.getCarModelId()).orElseThrow();
        car.setRegistrationNumber(carDto.getRegistrationNumber());
        car.setCarModel(model);
        car.setCarStatus(carDto.getCarStatus());

        return carRepository.save(car);
    }

    public void deleteCar(long carId) {
        List<Booking> bookings = bookingRepository.findAllBookingsByCarId(carId);
        if (bookings.isEmpty()) {
            carRepository.deleteById(carId);
        } else {
            Car car = carRepository.findById(carId).orElseThrow();
            car.setCarStatus(CarStatus.OUT_OF_SERVICE);
            carRepository.save(car);
        }
    }



    public List<CarModel> getCarModels() {
        return carModelRepository.findAll();
    }


    public CarModel addCarModel(CarModelDto carModelDto) {
        CarModel model = modelMapper.map(carModelDto, CarModel.class);
        return carModelRepository.save(model);
    }

    public CarModel editCarModel(CarModelDto carModelDto, long modelId) {
        CarModel model = carModelRepository.findById(modelId).orElseThrow();
        model.setBrand(carModelDto.getBrand());
        model.setModel(carModelDto.getModel());
        model.setNOfSeats(carModelDto.getNOfSeats());
        model.setFuelType(carModelDto.getFuelType());
        model.setTransmitionType(carModelDto.getTransmitionType());
        model.setRequiredCategory(carModelDto.getRequiredCategory());
        model.setPrice(carModelDto.getPrice());
        model.setDescription(carModelDto.getDescription());
        model.setActive(carModelDto.getActive());

        return carModelRepository.save(model);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE') or (#id != null and @securityService.isActiveModel(#id))")
    public CarModel getSingleModel(long id) {
        return carModelRepository.findById(id).orElseThrow();
    }


    public void deleteCarModel(long carModelId) {
        List<Car> cars = carRepository.findAllCarsByCarModelId(carModelId);
        if (cars.isEmpty()) {
            carModelRepository.deleteById(carModelId);
        } else {
            CarModel model = carModelRepository.findById(carModelId).orElseThrow();
            model.setActive(false);
            carModelRepository.save(model);
        }

    }

    public CarModelDto getSingleModelDto(long modelId) {
        CarModel carModel = carModelRepository.findById(modelId).orElseThrow();
        return modelMapper.map(carModel, CarModelDto.class);
    }


    @PreAuthorize("#nonActive == null or #nonActive == false or hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    public Page<CarModel> getCarModelsPaginated(String order, TransmitionType transmitionType, FuelType fuelType, Boolean nonActive, Pageable pageable) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Specification<CarModel> spec = CarModelSpecification.hasTransmitionType(transmitionType)
                .and(CarModelSpecification.hasFuelType(fuelType)
                        .and(CarModelSpecification.isNonActive(nonActive)));

        List<CarModel> list = carModelRepository.findAll(spec, Sort.by(direction, "price"));
        List<CarModel> filteredList = PageUtils.getPageContent(list, pageable);
        return new PageImpl<>(filteredList, pageable, list.size());
    }


}
