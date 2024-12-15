package com.github.im2back.customerms.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.im2back.customerms.exceptions.StandardError;
import com.github.im2back.customerms.exceptions.StandardErrorBeanValidation;
import com.github.im2back.customerms.model.dto.datainput.CustomerDto;
import com.github.im2back.customerms.model.dto.datainput.PurchaseRequestDto;
import com.github.im2back.customerms.model.dto.datainput.UndoPurchaseDto;
import com.github.im2back.customerms.model.dto.dataoutput.DataForMetricsDto;
import com.github.im2back.customerms.model.dto.dataoutput.GetCustomerDto;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseResponseDto;
import com.github.im2back.customerms.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
public class CustomerController {

	
	private final CustomerService service;

	@Operation(summary = "Retorna um GetCustomerDto apartir do ID informado no path")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Retorna Status 200ok e um Dto em caso de sucesso",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = GetCustomerDto.class))),
			@ApiResponse(
				    responseCode = "404",
				    description = "Em caso de ID não encontrado Retorna Status 404 Not Found e uma mensagem tratada do tipo StandardError",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
	})
	
	@GetMapping("/{id}")
	ResponseEntity<GetCustomerDto> findCustomerById(@PathVariable Long id) {
		GetCustomerDto response = service.findCustomerById(id);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Retorna um GetCustomerDto apartir do documento informado como parametro '?document' ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Retorna Status 200ok e um Dto em caso de sucesso",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = GetCustomerDto.class))),
			@ApiResponse(
				    responseCode = "404",
				    description = "Em caso de documento não encontrado Retorna Status 404 Not Found e uma mensagem tratada do tipo StandardError",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
	})
	@GetMapping("/findDocument")
	ResponseEntity<GetCustomerDto> findCustomerByDocument(@RequestParam String document) {
		GetCustomerDto response = service.findCustomerByDocumentOrganizedPurchase(document);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Cria um novo Customer apartir de um CustomerDto recebido no body e retorna um dto do objeto criado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201",description = "Retorna Status 201 e um Dto do objeto criado em caso de sucesso",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = GetCustomerDto.class))),
			@ApiResponse(
				    responseCode = "409",
				    description = "Retorna status 409 Em caso de validação de duplicidade de atributos unicos",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardErrorBeanValidation.class))),
			@ApiResponse(
				    responseCode = "400",
				    description = "Retorna status 400 Em caso de validação do bean validation",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardErrorBeanValidation.class))),
	})
	@PostMapping
	ResponseEntity<GetCustomerDto> saveNewCustomer(@RequestBody @Valid CustomerDto customer,
			UriComponentsBuilder uriBuilder) {
		GetCustomerDto response = service.saveNewCustomer(customer);
		var uri = uriBuilder.path("/customer/{id}").buildAndExpand(response.id()).toUri();
		return ResponseEntity.created(uri).body(response);
	}
	
	@Operation(summary = "Faz a exclusão lógica de um Customer apartir documento informado como parametro '?document'")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204",description = "Retorna Status 204 e um body vazio, após excluir o customer",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Void.class))),
			@ApiResponse(
				    responseCode = "404",
				    description = "Em caso de documento não encontrado Retorna Status 404 Not Found e uma mensagem tratada do tipo StandardError",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
	})
	@DeleteMapping("/deletecustomer")
	ResponseEntity<Void> deleteCustomerById(@RequestParam String document) {
		service.logicalCustomerDeletion(document);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Faz uma registro de uma compra apartir de um PurchaseRequestDto recebido e retorna o dto dessa compra PurchaseResponseDto")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Retorna Status 200 e um Dto do objeto criado em caso de sucesso",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = PurchaseResponseDto.class))),
			@ApiResponse(
				    responseCode = "400",
				    description = "Retorna status 400 Em caso de validação do bean validation",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardErrorBeanValidation.class))),
			@ApiResponse(
				    responseCode = "404",
				    description = "Em caso de documento não encontrado Retorna Status 404 Not Found e uma mensagem tratada do tipo StandardError",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
			})
	@PutMapping
	ResponseEntity<PurchaseResponseDto> purchase(@RequestBody @Valid PurchaseRequestDto dtoRequest) {
		PurchaseResponseDto response = service.purchase(dtoRequest);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Exclui do banco de dados uma compra com base no objeto recebido como parametro UndoPurchaseDto")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Exclui a compra, retorna Status 200 e um corpo vazio",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Void.class))),
			@ApiResponse(
				    responseCode = "404",
				    description = "Em caso de compra não encontrada Retorna Status 404 Not Found e uma mensagem tratada do tipo StandardError",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
	})
	@PutMapping("/undopurchase")
	ResponseEntity<Void> undoPurchase(@RequestBody @Valid UndoPurchaseDto dtoRequest) {
		service.undoPurchase(dtoRequest);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Quita uma compra mudando seu status de EM_ABERTO para PAGO")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "quita a compra, retorna Status 200 e um corpo vazio",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Void.class))),
			@ApiResponse(
				    responseCode = "404",
				    description = "Em caso de compra não encontrada Retorna Status 404 Not Found e uma mensagem tratada do tipo StandardError",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(
				    responseCode = "400",
				    description = "Em caso de validações do beanvalidation retorna status 400 e uma menssagem personalizada do tip oStandardErrorBeanValidation",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardErrorBeanValidation.class))),
	})
	@PutMapping("/payment")
	ResponseEntity<Void> individualPayment(@RequestBody @Valid UndoPurchaseDto dtoRequest) {
		service.individualPayment(dtoRequest);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Gera uma nota das compras com status EM_ABERTO para o documento informado no parametro")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Envia a nota por email, retorna Status 200 e um corpo vazio",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Void.class))),
			@ApiResponse(
				    responseCode = "404",
				    description = "Em caso de Customer não encontrado Retorna Status 404 Not Found e uma mensagem tratada do tipo StandardError",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
	})
	@PutMapping("/note")
	ResponseEntity<Void> generatePurchaseNote(@RequestParam String document) {
		service.generatePurchaseInvoice(document);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Quita todas as compras mudando seu status de EM_ABERTO para PAGO")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "quita aa compras, retorna Status 200 e um corpo vazio",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Void.class))),
			@ApiResponse(
				    responseCode = "404",
				    description = "Em caso de Customer não encontrado Retorna Status 404 Not Found e uma mensagem tratada do tipo StandardError",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
	})
	@DeleteMapping("/cleardebt")
	ResponseEntity<Void> clearDebt(@RequestParam String document) {
		service.clearDebt(document);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "FAz consultas personalizadas no bando de dados e retornas dados para aferição de métricas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Retorna as métricas encapsulados pelo objeto DataForMetricsDto",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = DataForMetricsDto.class))),
	})
	@GetMapping("/metrics")
	ResponseEntity<DataForMetricsDto> getMetrics() {
		DataForMetricsDto response = service.metrics();
		return ResponseEntity.ok(response);
	}
	

}
