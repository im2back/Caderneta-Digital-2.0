package com.github.im2back.orchestrator.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.github.im2back.orchestrator.dto.in.PurchasedItem;

import jakarta.validation.Valid;

@FeignClient(name = "stockms")
public interface StockClient {
	
	@PostMapping("products/update-after-purchas")
	public ResponseEntity<Void> updateStock(@RequestBody @Valid List<PurchasedItem> dto);
}
