package com.github.im2back.validation.repository.stock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.im2back.validation.entities.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	
	@Query("SELECT p FROM Product p WHERE p.code IN :codes")
    List<Product> findByCodes(@Param("codes") List<String> codes);
	

}
