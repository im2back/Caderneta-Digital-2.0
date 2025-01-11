package com.github.im2back.orchestrator.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.im2back.orchestrator.config.FeignConfig;
import com.github.im2back.orchestrator.dto.in.MassiveReplenishmentResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchasedItem;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@FeignClient(name = "stockms", configuration = FeignConfig.class, path ="/products" )
public interface StockClient {
	
	@PutMapping
	public ResponseEntity<List<StockUpdateResponseDTO>> updateStockAfterPurchase(@RequestBody @Valid List<PurchasedItem> dto);

	@PutMapping("/restock")
	public ResponseEntity<List<MassiveReplenishmentResponseDTO>> massiveReplenishmentInStock(@RequestBody
			@NotEmpty(message = "Input movie list cannot be empty.") @Valid List<StockUpdateResponseDTO> dtoIn);
}
