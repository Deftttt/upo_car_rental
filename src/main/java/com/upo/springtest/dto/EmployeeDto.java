package com.upo.springtest.dto;

import com.upo.springtest.enums.EmployeePosition;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDto {

    @NotNull(message = "Pole nie może być puste")
    @Min(value = 1, message = "Zła wartość")
    private double salary;

    @Enumerated(EnumType.STRING)
    private EmployeePosition position;
}
