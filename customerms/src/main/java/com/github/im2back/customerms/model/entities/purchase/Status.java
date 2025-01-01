package com.github.im2back.customerms.model.entities.purchase;

public enum Status {
	
	DEFINIR_STATUS {
		public Status setStatus(String document) {
			return getStatusByDocument(document);}},
	
	EM_ABERTO,
	PAGO;
		
    public Status setStatus(String document) {
        throw new UnsupportedOperationException("setStatus is not supported for this status");
    }

    private static Status getStatusByDocument(String document) {
        return document.equals("7654321589") ? Status.PAGO : Status.EM_ABERTO;
    }
}
