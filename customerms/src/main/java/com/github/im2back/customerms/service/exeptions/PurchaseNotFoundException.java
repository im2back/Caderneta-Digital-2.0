package com.github.im2back.customerms.service.exeptions;

public class PurchaseNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public PurchaseNotFoundException(String msg) {
	super(msg);
	}
}
