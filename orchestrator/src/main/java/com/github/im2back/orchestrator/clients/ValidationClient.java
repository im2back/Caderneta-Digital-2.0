package com.github.im2back.orchestrator.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.im2back.orchestrator.config.FeignConfig;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;


@FeignClient(name = "validation", configuration = FeignConfig.class)
public interface ValidationClient {

    @PostMapping("/validations/purchase")
    ResponseEntity<Void> valid(@RequestBody PurchaseRequestDTO dto);   
}


