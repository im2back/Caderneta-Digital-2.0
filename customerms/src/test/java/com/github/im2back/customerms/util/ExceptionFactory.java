package com.github.im2back.customerms.util;

import java.util.ArrayList;
import java.util.List;

import com.github.im2back.customerms.service.exeptions.CustomerNotFoundException;
import com.github.im2back.customerms.validations.exceptions.CustomerRegisterValidationException;

public class ExceptionFactory {

	
	public static CustomerNotFoundException createCustomerNotFoundException() {
		return new CustomerNotFoundException("msg");
	}
	public static CustomerRegisterValidationException createCustomerRegisterValidationException() {
		List<String> msgs = new ArrayList<>();
		return new CustomerRegisterValidationException(msgs);
	}
	
	
}
