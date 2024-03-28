package com.nagarro.customerservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.nagarro.customerservice.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
