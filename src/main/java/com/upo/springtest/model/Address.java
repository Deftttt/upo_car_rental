package com.upo.springtest.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length =  60)
    private String city;
    @Column(nullable = false, length =  60)
    private String street;
    @Column(nullable = false, length =  10)
    private String localNumber;
    @Column(nullable = false, length =  10)
    private String postCode;

    public Address(String city, String street, String localNumber, String postCode) {
        this.city = city;
        this.street = street;
        this.localNumber = localNumber;
        this.postCode = postCode;
    }
}
