package com.upo.springtest.dto;

import com.upo.springtest.enums.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BookingUpdateDto {

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Size(max = 300, message = "Przekroczono max. długość (300 znaków)")
    private String comment;
}
