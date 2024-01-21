package com.upo.springtest.dto;

import com.upo.springtest.enums.Category;
import com.upo.springtest.enums.FuelType;
import com.upo.springtest.enums.TransmitionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CarModelDto {

    @NotBlank(message = "Pole nie może być puste")
    @Size(max = 60, message = "Przekroczono max. długość (60 znaków)")
    private String brand;

    @NotBlank(message = "Pole nie może być puste")
    @Size(max = 60, message = "Przekroczono max. długość (60 znaków)")
    private String model;

    @NotNull(message = "Pole nie może być puste")
    @Min(value = 1, message = "Zła liczba miejsc")
    private int nOfSeats;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    private TransmitionType transmitionType;

    @Enumerated(EnumType.STRING)
    private Category requiredCategory;

    @NotNull(message = "Pole nie może być puste")
    @Min(value = 1, message = "Zła wartość")
    private float price;

    @NotNull(message = "Pole nie może być puste")
    private Boolean active = true;

    private String description;

}
