package com.github.im2back.customerms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.im2back.customerms.model.entities.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
