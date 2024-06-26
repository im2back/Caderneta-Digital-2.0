package com.github.im2back.customerms.model.entities.purchase;

import java.time.Instant;

import com.github.im2back.customerms.model.entities.customer.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "product_code")
    private Integer productCode;
	
	@Column(name = "purchase_date")
	private Instant purchaseDate;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

}