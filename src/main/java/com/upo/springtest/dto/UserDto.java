package com.upo.springtest.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    @NotBlank(message = "Pole nie może być puste")
    @Size(max = 60, message = "Przekroczono max. długość (60 znaków)")
    private String firstName;

    @NotBlank(message = "Pole nie może być puste")
    @Size(max = 60, message = "Przekroczono max. długość (60 znaków)")
    private String lastName;

    @NotBlank(message = "Pole nie może być puste")
    @Size(max = 60, message = "Przekroczono max. długość (60 znaków)")
    private String email;

    @NotBlank(message = "Pole nie może być puste")
    @Pattern(regexp = "^\\d{9}$", message = "Nr telefonu to 9 cyfr")
    private String phoneNumber;

    @NotBlank(message = "Pole nie może być puste")
    @Size(min = 6, max = 30, message = "Nazwa użytkownika musi mieć 6-30 znaków")
    private String username;

    @NotBlank(message = "Pole nie może być puste")
    @Size(min = 6, max = 30, message = "Hasło musi mieć 6-30 znaków")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,}$", message = "Hasło musi zawierać co najmniej 1 wielką i małą litere i 1 cyfrę")
    private String password;

    @NotBlank(message = "Pole nie może być puste")
    @Size(min = 6, max = 30, message = "Hasło musi mieć 6-30 znaków")
    private String passwordConfirm;

    @NotBlank(message = "Pole nie może być puste")
    private String city;
    @NotBlank(message = "Pole nie może być puste")
    private String postCode;
    @NotBlank(message = "Pole nie może być puste")
    private String street;
    @NotBlank(message = "Pole nie może być puste")
    private String localNumber;
}
