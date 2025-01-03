package com.github.im2back.validation.entities.customer;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_purchase")
@Entity

public class PurchaseRecord {
	
	public PurchaseRecord(String productName, BigDecimal productprice, String productCode, Instant purchaseDate,
			Integer quantity, Status status,Customer customer) {
		super();
		this.productName = productName;
		this.productprice = productprice;
		this.productCode = productCode;
		this.purchaseDate = purchaseDate;
		this.customer = customer;
		this.quantity = quantity;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "product_price")
	private BigDecimal productprice;
	
	@Column(name = "product_code")
    private String productCode;
	
	@Column(name = "purchase_date")
	private Instant purchaseDate;
	
	@Column(name = "product_quantity")
	private Integer quantity;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status")
	private Status status;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	


}