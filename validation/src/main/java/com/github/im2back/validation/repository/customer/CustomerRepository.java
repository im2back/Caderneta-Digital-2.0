package com.github.im2back.validation.repository.customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.im2back.validation.entities.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	@Query("SELECT c FROM Customer c LEFT JOIN FETCH c.purchaseRecord WHERE c.document = :document")
	Optional<Customer> findByDocument(@Param("document") String document);

}
