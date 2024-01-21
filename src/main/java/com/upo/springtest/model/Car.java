package com.upo.springtest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.upo.springtest.enums.BookingStatus;
import com.upo.springtest.enums.CarStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.awt.print.Book;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length =  10)
    private String registrationNumber;
    @Column(precision=7)
    private Double latitude;
    @Column(precision=7)
    private Double longitude;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "car_model_id", referencedColumnName = "id")
    private CarModel carModel;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;


}
