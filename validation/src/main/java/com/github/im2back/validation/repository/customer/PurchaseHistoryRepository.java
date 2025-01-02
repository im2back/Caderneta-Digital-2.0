package com.github.im2back.validation.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.im2back.validation.entities.customer.PurchaseRecord;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseRecord, Long> {

}
