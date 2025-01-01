package com.github.im2back.validation.entities.product;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@Table(name = "tb_product")
@Entity
public class Product {
	
	public Product(String name, BigDecimal price, String code, Integer quantity, String productUrl) {
		super();
		this.name = name;
		this.price = price;
		this.code = code;
		this.quantity = quantity;
		this.productUrl = productUrl;
	}
	


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "product_name")
	private String name;
	
	@Column(name = "product_price")
	private BigDecimal price;
	
	@Column(name = "product_code",unique = true)
	private String code;
	
	@Column(name = "product_quantity")
	private Integer quantity;
	
	@Column(name = "product_url")
	private String productUrl;
	
	

}
