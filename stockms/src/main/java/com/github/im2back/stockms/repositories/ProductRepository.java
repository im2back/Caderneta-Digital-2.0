package com.github.im2back.stockms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.im2back.stockms.model.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
