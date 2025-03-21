package com.github.im2back.orchestrator.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.im2back.orchestrator.config.FeignConfig;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.out.PurchaseHistoryDTO;

import jakarta.validation.Valid;

@FeignClient(name = "customerms",configuration = FeignConfig.class, path = "/customers")
public interface CustomerClient {

	@PostMapping("/purchase-history")
	ResponseEntity<PurchaseHistoryResponseDTO> persistPurchaseHistory(@RequestBody @Valid PurchaseHistoryDTO dtoRequest);
	
}
