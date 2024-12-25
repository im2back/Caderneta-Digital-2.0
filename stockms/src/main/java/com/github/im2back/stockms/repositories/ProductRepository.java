package com.github.im2back.stockms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.im2back.stockms.model.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	Optional<Product> findByCode(String code);
	
	@Query("SELECT p FROM Product p WHERE p.code IN :codes")
    List<Product> findByCodes(@Param("codes") List<String> codes);
	
	@Modifying
	@Query("UPDATE Product p SET p.quantity = :product.quantity WHERE p.code = :product.code")
	int updateProductsByCodes(@Param("product") List<Product> product);
	
	

}
