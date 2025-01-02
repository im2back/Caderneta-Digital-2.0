package com.github.im2back.customerms.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.im2back.customerms.exceptions.StandardError;
import com.github.im2back.customerms.model.dto.datainput.NewCustomerDTO;
import com.github.im2back.customerms.model.dto.datainput.PurchaseHistoryInDTO;
import com.github.im2back.customerms.model.dto.dataoutput.CustomerDTO;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseHistoryOutDTO;
import com.github.im2back.customerms.model.dto.dataoutput.metrics.DataForMetricsDTO;
import com.github.im2back.customerms.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
	
	private final CustomerService service;

	@Operation(summary = "Busca um Customer no banco e retorna um CustomerDTO apartir do ID informado no path")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Retorna Status 200ok e um Dto em caso de sucesso",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = CustomerDTO.class))),
			@ApiResponse(
				    responseCode = "404",
				    description = "Em caso de ID não encontrado Retorna Status 404 Not Found e uma mensagem tratada do tipo StandardError",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
	})
	
	@GetMapping("/{id}")
	ResponseEntity<CustomerDTO> findCustomerById(@PathVariable Long id) {
		CustomerDTO response = service.findCustomerById(id);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Busca um customer no banco e retorna um CustomerDTO apartir do documento informado no path")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Retorna Status 200ok e um Dto em caso de sucesso",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = CustomerDTO.class))),
			@ApiResponse(
				    responseCode = "404",
				    description = "Em caso de documento não encontrado Retorna Status 404 Not Found e uma mensagem tratada do tipo StandardError",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
	})
	@GetMapping("/document/{document}")
	ResponseEntity<CustomerDTO> findCustomerByDocument(@PathVariable String document) {
		CustomerDTO response = service.findCustomerByDocumentOrganizedPurchase(document);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Cria e salva no banco um novo Customer apartir de um NewCustomerDTO recebido no body e retorna um CustomerDTO do objeto criado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201",description = "Retorna Status 201 e um Dto do objeto criado em caso de sucesso",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = CustomerDTO.class))),
			@ApiResponse(
				    responseCode = "409",
				    description = "Retorna status 409 Em caso de validação de duplicidade de atributos unicos",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(
				    responseCode = "400",
				    description = "Retorna status 400 Em caso de validação do bean validation",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
	})
	@PostMapping
	ResponseEntity<CustomerDTO> saveNewCustomer(@RequestBody @Valid NewCustomerDTO customer,
			UriComponentsBuilder uriBuilder) {
		CustomerDTO response = service.saveNewCustomer(customer);
		var uri = uriBuilder.path("/customer/{id}").buildAndExpand(response.id()).toUri();
		return ResponseEntity.created(uri).body(response);
	}
	
	@Operation(summary = "Faz a exclusão lógica de um Customer apartir documento informado no path")
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
	@PatchMapping("/{document}")
	ResponseEntity<Void> deleteCustomerByDocument(@PathVariable String document) {
		service.logicalCustomerDeletion(document);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Cria um historico de compra no banco de dados apartir de um PurchaseHistoryInDTO recebido no body"
			+ " e retorna um PurchaseHistoryOutDTO desse registro")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Retorna Status 200 e um Dto do objeto criado em caso de sucesso",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = PurchaseHistoryOutDTO.class))),
			@ApiResponse(
				    responseCode = "400",
				    description = "Retorna status 400 Em caso de validação do bean validation",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(
				    responseCode = "404",
				    description = "Em caso de documento não encontrado Retorna Status 404 Not Found e uma mensagem tratada do tipo StandardError",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
			})
	@PostMapping("/purchase-history")
	ResponseEntity<PurchaseHistoryOutDTO> persistPurchaseHistory(@RequestBody @Valid PurchaseHistoryInDTO dtoIn) {
		PurchaseHistoryOutDTO response = service.purchase(dtoIn);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Exclui do banco de dados uma compra registrada incorretamente, com base no purchaseId informado no path")
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
	@PostMapping("/purchase/{purchaseId}/undo")
	ResponseEntity<Void> undoPurchase(@PathVariable Long purchaseId) {
		service.undoPurchase(purchaseId);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Quita uma compra mudando seu status de EM_ABERTO para PAGO com base no purchaseId recebido no path ")
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
				    description = "Em caso de validações do beanvalidation retorna status 400 e uma menssagem personalizada do tipo StandardError",
				    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
				    schema = @Schema(implementation = StandardError.class))),
	})
	@PatchMapping("/purchase/payment/{purchaseId}")
	ResponseEntity<Void> individualPayment(@PathVariable Long purchaseId) {
		service.individualPayment(purchaseId);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Gera uma nota contendo as compras com status EM_ABERTO com base no documento informado no parametro")
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
	@GetMapping("/note")
	ResponseEntity<Void> generatePurchaseNote(@RequestParam String document) {
		service.generatePurchaseInvoice(document);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Quita todas as compras de um Customer mudando seu status de EM_ABERTO para PAGO, como base no document fornecido no parametro")
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
	@PatchMapping("{document}/payments")
	ResponseEntity<Void> clearDebt(@PathVariable String document) {
        service.clearDebt(document);	
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Faz consultas personalizadas no bando de dados e retorna dados para aferição de métricas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Retorna as métricas encapsulados pelo objeto DataForMetricsDto",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = DataForMetricsDTO.class))),
	})
	@GetMapping("/metrics")
	ResponseEntity<DataForMetricsDTO> getMetrics() {
        DataForMetricsDTO response = service.metrics();
        	return ResponseEntity.ok(response);
	}
	

}
