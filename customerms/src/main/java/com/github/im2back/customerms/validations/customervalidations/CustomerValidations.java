package com.github.im2back.customerms.validations.customervalidations;

import com.github.im2back.customerms.dto.datainput.RegisterCustomerDTO;

public interface CustomerValidations {

	void valid(RegisterCustomerDTO requestDto);
}
