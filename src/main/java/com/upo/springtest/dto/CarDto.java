package com.upo.springtest.dto;

import com.upo.springtest.enums.CarStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CarDto {

    @NotBlank(message = "Pole nie może być puste")
    @Size(max = 9, message = "Przekroczono max. długość (9)")
    private String registrationNumber;

    @NotNull(message = "Pole nie może być puste")
    private Long carModelId;

    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;

}
