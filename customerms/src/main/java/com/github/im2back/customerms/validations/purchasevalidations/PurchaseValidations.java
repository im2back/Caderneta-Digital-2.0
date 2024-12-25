package com.github.im2back.customerms.validations.purchasevalidations;

import com.github.im2back.customerms.model.dto.datainput.PurchaseRequestDto;
import com.github.im2back.customerms.model.entities.customer.Customer;

public interface PurchaseValidations {

	void valid(PurchaseRequestDto dtoRequest,Customer customer);
}
