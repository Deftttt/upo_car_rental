package com.upo.springtest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.upo.springtest.enums.Category;
import com.upo.springtest.enums.FuelType;
import com.upo.springtest.enums.TransmitionType;
import com.upo.springtest.util.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length =  60)
    private String brand;
    @Column(nullable = false, length =  60)
    private String model;
    @Column(nullable = false)
    private int nOfSeats;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransmitionType transmitionType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category requiredCategory;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = true, length =  300)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "carModel", cascade=CascadeType.ALL)
    private List<Car> cars;

    @OneToMany(mappedBy = "carModel", cascade=CascadeType.ALL)
    private List<Image> images;

    @Transient
    public String getImageDirectory(){
        return "/model-images/" + id + '/';
    }
}
