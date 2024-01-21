package com.upo.springtest.repository;

import com.upo.springtest.model.Customer;
import com.upo.springtest.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c")
    List<Customer> findAllCustomers();

    Customer findCustomerByUserUsername(String username);

    Customer findCustomerByUserId(long userId);
}
