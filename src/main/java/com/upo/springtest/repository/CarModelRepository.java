package com.upo.springtest.repository;

import com.upo.springtest.enums.BookingStatus;
import com.upo.springtest.enums.TransmitionType;
import com.upo.springtest.model.Booking;
import com.upo.springtest.model.CarModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {

    List<CarModel> findAllCarModelsByTransmitionType(TransmitionType transmitionType, Sort sort);

    List<CarModel> findAll(Specification<CarModel> spec, Sort sort);
}
