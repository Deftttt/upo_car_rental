package com.upo.springtest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class AdditionalCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length =  80)
    private String name;
    @Column(nullable = false, length = 200)
    private String description;
    @Column(nullable = false)
    private Double price;

    @ManyToMany(mappedBy = "additionalCosts")
    private List<Booking> bookings = new ArrayList<>();

}
