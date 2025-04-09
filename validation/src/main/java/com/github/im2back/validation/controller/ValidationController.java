package com.github.im2back.validation.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.im2back.validation.dto.ProductsPurchaseRequestDto;
import com.github.im2back.validation.exception.StandardError;
import com.github.im2back.validation.service.ValidationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/validations")
@RequiredArgsConstructor
public class ValidationController {

	private final ValidationService validationService;
	
	@Operation(summary = "Faz validações de cliente e estoque")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Retorna Status 200 em caso de sucesso",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Void.class))),
			@ApiResponse(
				    responseCode = "422",
				    description = "Retorna status 422 Em caso de validação de negocio",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
	})
	@PostMapping("/purchase")
	public ResponseEntity<Void> valid(@RequestBody ProductsPurchaseRequestDto dto){
		this.validationService.validPurchase(dto);
		return ResponseEntity.ok().build();
	}
}
