package com.github.im2back.customerms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.im2back.customerms.model.entities.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByDocument(String document);

	Optional<Customer> findByEmail(String email);
	
	Optional<Customer> findByPhone(String phone);
	
    @Query("SELECT c FROM Customer c JOIN c.purchaseRecord p WHERE p.id = :purchaseId")
    Optional<Customer> findByPurchase(@Param("purchaseId") Long purchaseId);
}
