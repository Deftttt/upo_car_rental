package com.upo.springtest.model;

import com.upo.springtest.enums.EmployeePosition;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private double salary;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeePosition position;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Employee(EmployeePosition position, User user, double salary) {
        this.position = position;
        this.user = user;
        this.salary = salary;
    }
}

