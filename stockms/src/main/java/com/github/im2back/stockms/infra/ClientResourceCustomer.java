package com.github.im2back.stockms.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.im2back.stockms.model.dto.outputdata.PurchaseRegister;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseResponseDto;

@FeignClient(name = "customerClient", url = "http://localhost:8080", path = "/customer")
public interface ClientResourceCustomer {

	
	@PutMapping
	ResponseEntity<PurchaseResponseDto> purchase(@RequestBody PurchaseRegister dtoRequest);
}
