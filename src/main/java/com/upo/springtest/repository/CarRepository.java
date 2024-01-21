package com.upo.springtest.repository;

import com.upo.springtest.model.Car;
import com.upo.springtest.model.CarModel;
import com.upo.springtest.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

    @Query("SELECT c FROM Car c")
    List<Car> findAllCars();

    Car findFirstByCarModelId(long modelId);

    List<Car> findAllCarsByCarModelId(long modelId);


    @Query("SELECT DISTINCT c FROM Car c " +
            "WHERE c.carModel = :carModel " +
            "AND c.carStatus = 'IN_USE' " +
            "AND NOT EXISTS (" +
            "    SELECT 1 FROM Booking b " +
            "    WHERE b.car = c " +
            "    AND b.bookingStatus = 'ACTIVE' " +
            "    AND (:date BETWEEN b.pickupDate AND b.returnDate OR :date2 BETWEEN b.pickupDate AND b.returnDate" +
            "    OR b.pickupDate BETWEEN :date AND :date2 OR b.returnDate BETWEEN :date AND :date2)" +
            ")")
    List<Car> findAvailableCarsForModelAndDateRange(
            @Param("carModel") CarModel carModel,
            @Param("date") Date pickupDate,
            @Param("date2") Date returnDate
    );

}
