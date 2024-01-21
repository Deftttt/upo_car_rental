package com.upo.springtest.repository;


import com.upo.springtest.model.Customer;
import com.upo.springtest.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e")
    List<Employee> findAllEmployees();

    @Query("SELECT e FROM Employee e " +
            "LEFT JOIN Booking b ON e.id = b.employee.id " +
            "WHERE e.position = 'CUSTOMER_SERVICE'" +
            "GROUP BY e.id " +
            "ORDER BY COUNT(b) ASC")
    List<Employee> findCustomerServiceEmployeeWithMinBookings();

    Employee findEmployeeByUserUsername(String username);

}