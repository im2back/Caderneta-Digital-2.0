package com.github.im2back.stockms.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import com.github.im2back.stockms.exceptions.StandardError;
import com.github.im2back.stockms.model.dto.inputdata.NewProductToSaveDTO;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItemDTO;
import com.github.im2back.stockms.model.dto.inputdata.UndoPurchaseDTO;
import com.github.im2back.stockms.model.dto.outputdata.ProductDTO;
import com.github.im2back.stockms.model.dto.outputdata.StockUpdateAfterPurchaseResponseDTO;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
public class ProductController { 
	
	private final ProductService service;
	
	@Operation(summary = ("Busca um Product no banco e retorna um ProductDTO com base no ID recebido no path"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "retorna um ProductDto",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = ProductDTO.class))),
			@ApiResponse(
					responseCode = "404",
					description = "retorna um StandardError caso o produto não seja encontrado",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = StandardError.class))),
	})
	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> findProductById(@PathVariable Long id) {
		ProductDTO response =  new ProductDTO(service.findProductById(id));
		return ResponseEntity.ok(response);
	}
	 
	@Operation(summary = ("Busca no banco e retorna um ProductDTO de Product com base no code recebido no parametro"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "retorna um ProductDto",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = ProductDTO.class))),
			@ApiResponse(
					responseCode = "404",
					description = "retorna um StandardError caso o produto não seja encontrado",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = StandardError.class))),
	})
	@GetMapping("/find-code")
	public ResponseEntity<ProductDTO> findProductByCode(@RequestParam String code) {
		Product product = service.findByCode(code);
		return ResponseEntity.ok(new ProductDTO(product));
	}
	
	@Operation(summary = ("Salva no um novo Product no banco apartir de um NewProductToSaveDTO recebido no body  e retorna um ProductDTO"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "retorna um ProductDto do produto criado",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = ProductDTO.class))),
			@ApiResponse(
					responseCode = "400",
					description = "retorna um StandardErrorBeanValidation em caso de erro capturado pelo BeanValidation",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(
					responseCode = "409",
					description = "Retorna um StandardError em caso de código duplicado/conflito na DB",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = StandardError.class))),
	})
	@PostMapping
	public ResponseEntity<ProductDTO> saveNewProduct(@RequestBody @Valid NewProductToSaveDTO dtoIn, UriComponentsBuilder uriBuilder) {
		ProductDTO response = service.saveNewProduct(dtoIn);
		var uri = uriBuilder.path("/product/{id}").buildAndExpand(response.id()).toUri();
		return ResponseEntity.created(uri).body(response);
	}

	@Operation(summary = ("Não retorna body, apenas exclui um Product apartir de um ID informado no path"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "204",
					description = "Não retorna corpo, apenas status 204 após a exclusão",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Void.class))),
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
		service.deleProductById(id);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = ("Atualiza o estoque apartir de um List<PurchasedItemDTO> recebido no body e retorna um StockUpdateAfterPurchaseResponseDTO"
			+ " após atualizar o estoque"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Retorna um StockUpdateAfterPurchaseResponseDTO(dto da compra) após atualizar o estoque e deduzir os produtos comprados",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = StockUpdateAfterPurchaseResponseDTO.class))),
			@ApiResponse(
					responseCode = "400",
					description = "Retorna um StandardError em caso de exeção durante as validações de compra",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(
					responseCode = "404",
					description = "Retorna um StandardError caso um dos produtos contido na lista de compras, não seja localizado no BD",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = StandardError.class))),
	})
	@PostMapping("/update-after-purchase")
	public ResponseEntity<List<StockUpdateAfterPurchaseResponseDTO>> updateStock(
			@RequestBody @NotEmpty(message = "Input movie list cannot be empty.") @Valid List<PurchasedItemDTO> dtoIn) {
	
		List<StockUpdateAfterPurchaseResponseDTO> stockUpdateResponseDTOList = service.updateQuantityProductsAfterPurchase(dtoIn);
		return ResponseEntity.ok().body(stockUpdateResponseDTOList);
	}
	
	@Operation(summary = ("Retorna um corpo vazio e status 200 após desfazer uma compra"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Retorna apenas o status 200 e um corpo vazio após desfazer uma compra",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Void.class))),
			@ApiResponse(
					responseCode = "400",
					description = "Retorna um StandardErrorBeanValidation em caso de exeção durante as validações do BeanValidation",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(
					responseCode = "404",
					description = "Retorna um StandardError caso um dos produto não seja localizado no BD",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = StandardError.class))),
	})
	@PutMapping("/undo-purchase")
	public ResponseEntity<Void> undoPurchase(@RequestBody @Valid UndoPurchaseDTO dtoIn) {
		service.undoIndividualPurchase(dtoIn);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = ("Recebe um ProductDTO e atualiza os dados de um produto apartir deste"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Retorna apenas o status 200 e um corpo vazio após atualizar um produto",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Void.class))),
			@ApiResponse(
					responseCode = "400",
					description = "Retorna um StandardError em caso de exeção durante as validações do BeanValidation",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(
					responseCode = "404",
					description = "Retorna um StandardError caso um dos produto não seja localizado no BD",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = StandardError.class))),
	})
	@PutMapping("/update")
	public ResponseEntity<Void> updateProduct(@RequestBody @Valid ProductDTO dtoIn) {
		service.updateProduct(dtoIn);
		return ResponseEntity.ok().build();
	}

}
