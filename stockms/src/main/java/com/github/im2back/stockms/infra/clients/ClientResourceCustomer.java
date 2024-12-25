package com.github.im2back.stockms.infra.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.im2back.stockms.model.dto.inputdata.UndoPurchaseDto;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseRegister;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseResponseDto;

import jakarta.validation.Valid;

@FeignClient(name = "customerClient", url = "https://192.168.1.111:8080", path = "/customers")
public interface ClientResourceCustomer {

	
	@PutMapping
	ResponseEntity<PurchaseResponseDto> purchase(@RequestBody PurchaseRegister dtoRequest);
	
	@PutMapping("/undopurchase")
	ResponseEntity<Void> undoPurchase(@RequestBody @Valid UndoPurchaseDto dtoRequest);
}
