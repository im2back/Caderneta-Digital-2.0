package com.github.im2back.customerms.model.entities.customer;

import java.util.ArrayList;
import java.util.List;

import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
	
	private String name;
	
	@Column(unique = true)
	private String document;
	
	@Column(unique = true)
	private String email;
	
	@Column(unique = true)
	private String phone;
	
	private boolean isActive;
	
	@Embedded
	private Address address;
	
	@OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
	private List<PurchaseRecord> purchaseRecord = new ArrayList<>();
	
	

}