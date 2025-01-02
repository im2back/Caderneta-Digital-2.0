package com.github.im2back.validation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.im2back.validation.dto.ProductsPurchaseRequestDto;
import com.github.im2back.validation.service.ValidationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/validations")
@RequiredArgsConstructor
public class ValidationController {

	private final ValidationService validationService;
	
	@PostMapping("/purchase")
	public ResponseEntity<Void> valid(@RequestBody ProductsPurchaseRequestDto dto){
		validationService.validPurchase(dto);
		
		return ResponseEntity.ok().build();
	}
}
