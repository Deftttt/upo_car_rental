package com.upo.springtest.dto;

import com.upo.springtest.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@Data
@Getter
@Setter
public class BookingDto {

    @NotNull(message = "Pole nie może być puste")
    private long carModelId;

    @NotNull
    @Temporal( TemporalType.TIMESTAMP )
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date pickupDate;

    @NotNull
    @Temporal( TemporalType.TIMESTAMP )
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date returnDate;

    @NotNull
    private HirePoint pickupLocation;
    @NotNull
    private HirePoint returnLocation;


    private Set<AdditionalCost> additionalCosts;
}
