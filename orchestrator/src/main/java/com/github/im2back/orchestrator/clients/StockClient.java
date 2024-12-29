package com.github.im2back.orchestrator.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.im2back.orchestrator.config.FeignConfig;
import com.github.im2back.orchestrator.dto.in.PurchasedItem;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;

import jakarta.validation.Valid;

@FeignClient(name = "stockms", configuration = FeignConfig.class)
public interface StockClient {
	
	@PostMapping("products/update-after-purchase")
	public ResponseEntity<List<StockUpdateResponseDTO>> updateStock(@RequestBody @Valid List<PurchasedItem> dto);
}
