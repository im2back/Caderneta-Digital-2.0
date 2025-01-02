package com.github.im2back.customerms.validations.customervalidations;

import com.github.im2back.customerms.model.dto.datainput.NewCustomerDTO;

public interface CustomerValidations {

	void valid(NewCustomerDTO requestDto);
}
