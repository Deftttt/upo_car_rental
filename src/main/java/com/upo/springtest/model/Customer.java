package com.upo.springtest.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length =  16)
    private String drivingLicenseNumber;

    @Column(nullable = false, length =  16)
    private String cardNumber;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Customer(String drivingLicenseNumber, String cardNumber, User user){
        this.drivingLicenseNumber = drivingLicenseNumber;
        this.cardNumber = cardNumber;
        this.user = user;
    }
}
