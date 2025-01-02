package com.github.im2back.validation.entities.customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_customer")
@Entity
public class Customer {
	
	public Customer(String name, String document, String email, String phone, boolean isActive, Address address) {
		super();
		this.name = name;
		this.document = document;
		this.email = email;
		this.phone = phone;
		this.isActive = isActive;
		this.address = address;
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "customer_name")
	private String name;
	
	private String document;
	
	private String email;
	
	@Column(unique = true)
	private String phone;
	
	@Column(name = "is_active", nullable = true)
	private boolean isActive;
	
	@Embedded
	private Address address;
	
	@OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.EAGER)
	private List<PurchaseRecord> purchaseRecord = new ArrayList<>();
	
	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		
		for(PurchaseRecord p: this.purchaseRecord) {
			if(p.getStatus().toString().equals("EM_ABERTO")) {
				total = total.add(p.getProductprice().multiply(new BigDecimal(p.getQuantity())));
			}
		}
		return total;
	}

}