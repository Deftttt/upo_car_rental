package com.upo.springtest.model;

import com.upo.springtest.enums.BookingStatus;
import com.upo.springtest.enums.TransmitionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Temporal( TemporalType.TIMESTAMP )
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date pickupDate;

    @Column(nullable = false)
    @Temporal( TemporalType.TIMESTAMP )
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date returnDate;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "pickup_hire_point_id", referencedColumnName = "id")
    private HirePoint pickupLocation;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "return_hire_point_id", referencedColumnName = "id")
    private HirePoint returnLocation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Column(nullable = true, length = 300)
    private String comment;


    @ManyToMany
    @JoinTable(
            name = "booking_additional_cost",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "additional_cost_id")
    )
    private Set<AdditionalCost> additionalCosts;


}
