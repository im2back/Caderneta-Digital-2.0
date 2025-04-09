package com.github.im2back.stockms.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.stockms.model.dto.inputdata.NewProductDTO;
import com.github.im2back.stockms.model.dto.inputdata.ProductMassiveReplenishmentDTO;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItemDTO;
import com.github.im2back.stockms.model.dto.inputdata.UndoPurchaseDTO;
import com.github.im2back.stockms.model.dto.outputdata.ProductDTO;
import com.github.im2back.stockms.utils.ProductFactoryTest;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
class ProductControllerIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Test
	@DisplayName("Given a valid id, when finding product by id, then return ProductDTO")
	void givenValidId_whenFindingProductById_thenReturnProductDTO() throws Exception {
	    // ARRANGE
	    Long id = 1L;
	    
	    // ACT + ASSERT
	    mockMvc.perform(get("/products/{id}", id))
	           .andExpect(status().isOk())
	           .andExpect(jsonPath("$.id").value(id))
	           .andReturn();
	}

	@Test
	@DisplayName("Given a non-existing product id, when finding product by id, then throw ProductNotFoundException")
	void givenNonExistingProductId_whenFindingProductById_thenThrowProductNotFoundException() throws Exception {
	    //ARRANGE
	    Long id = 50L;
	    
	    //ACT + ASSERT
	    mockMvc.perform(get("/products/{id}", id))
	           .andExpect(status().isNotFound())
	           .andReturn();
	}
	
	@Test
	@DisplayName("Given a valid product code, when finding product by code, then return ProductDTO")
	void givenValidProductCode_whenFindingProductByCode_thenReturnProductDTO() throws Exception {
	    // ARRANGE
	    String code = "001";
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(get("/products/code/{code}", code))
	           .andExpect(status().isOk())
	           .andExpect(jsonPath("$.code").value(code))
	           .andReturn();
	}

	@Test
	@DisplayName("Given a non-existing product code, when finding product by code, then throw ProductNotFoundException")
	void givenNonExistingProductCode_whenFindingProductByCode_thenThrowProductNotFoundException() throws Exception {
	    // ARRANGE
	    String code = "0045";
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(get("/products/code/{code}", code))
	                .andExpect(status().isNotFound())
	                .andReturn();
	}

	@Test
	@DisplayName("Given an invalid request body, when saving new product, then throw MethodArgumentNotValidException")
	void givenInvalidRequestBody_whenSavingNewProduct_thenThrowMethodArgumentNotValidException() throws Exception {
	    // ARRANGE
	    NewProductDTO newProductDTO = ProductFactoryTest.createNewProductDTOInvalid();
	    String json = this.mapper.writeValueAsString(newProductDTO);
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(post("/products")
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isBadRequest())
	        .andReturn();
	}

	@Test
	@DisplayName("Given a valid request body, when saving new product, then return ProductDTO")
	void givenValidRequestBody_whenSavingNewProduct_thenReturnProductDTO() throws Exception {
	    // ARRANGE
	    NewProductDTO newProductDTO = ProductFactoryTest.createNewProductToSaveDTOValidCode005Quantity10();
	    String json = this.mapper.writeValueAsString(newProductDTO);
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(post("/products")
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isCreated())
	        .andExpect(jsonPath("$.code").value(newProductDTO.code()))
	        .andReturn();
	}

	@Test
	@DisplayName("Given an existing product code, when saving a new product, then throw DataIntegrityViolationException")
	void givenExistingProductCode_whenSavingNewProduct_thenThrowDataIntegrityViolationException() throws Exception {
	    // ARRANGE
	    NewProductDTO newProductDTO = ProductFactoryTest.createNewProductToSaveDTOValidCode001Quantity10();
	    String json = this.mapper.writeValueAsString(newProductDTO);
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(post("/products")
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(jsonPath("$.error").value("Data Integrity Violation"))
	        .andExpect(status().isConflict())
	        .andReturn();
	}
	
	@Test
	@DisplayName("Given a valid product id, when deleting product, then return void and HTTP status 204")
	void givenValidProductId_whenDeletingProduct_thenReturnVoidAndHttpStatus204() throws Exception {
	    // ARRANGE
	    Long id = 1L;
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(delete("/products/{id}", id))
	                .andExpect(status().isNoContent())
	                .andReturn();
	}

	@Test
	@DisplayName("Given a valid request body, when updating stock after purchase, then return list updated stock response DTO")
	void givenValidRequestBody_whenUpdatingStockAfterPurchase_thenReturnListUpdatedStockResponseDTO() throws Exception {
	    // ARRANGE
	    List<PurchasedItemDTO> dtoIn = ProductFactoryTest.createListPurchasedItemDTO();
	    String json = this.mapper.writeValueAsString(dtoIn);
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(put("/products")
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.length()").value(dtoIn.size()))
	        .andReturn();
	}

//  AMPARADO PELO MÓDULO DE VALIDAÇÕES	
//	@Test
//	@DisplayName("Should throw ProductNotFoundException when product is not found")
//	void updateStockAfterPurchase_ShouldReturnProductNotFoundException_WhenProductNotFound() throws Exception {
//		//ARRANGE
//		List<PurchasedItemDTO> dtoIn = ProductFactoryTest.createListPurchasedItemDTONotFoundCode();
//		
//		String json = this.mapper.writeValueAsString(dtoIn);
//				
//		//ACT + ASSERT
//		this.mockMvc.perform(put("/products").content(json).contentType(MediaType.APPLICATION_JSON))
//		.andExpect(status().isNotFound())
//		.andReturn();
//	}
	
	@Test
	@DisplayName("Given a valid product code and valid undo purchase DTO, when restocking individual product, then return void and HTTP status 200")
	void givenValidProductCodeAndValidUndoPurchaseDTO_whenRestockingIndividualProduct_thenReturnVoidAndHttpStatus200() throws Exception {
	    // ARRANGE
	    String productCode = "003";
	    UndoPurchaseDTO dto = new UndoPurchaseDTO(3);
	    String json = this.mapper.writeValueAsString(dto);
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(patch("/products/{productCode}/undo", productCode)
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andReturn();
	}
	
	@Test
	@DisplayName("Given an invalid undo purchase DTO, when restocking individual product, then return HTTP status 400")
	void givenInvalidUndoPurchaseDTO_whenRestockingIndividualProduct_thenReturnHttpStatus400() throws Exception {
	    // ARRANGE
	    String productCode = "001";
	    UndoPurchaseDTO dto = new UndoPurchaseDTO(null);
	    String json = this.mapper.writeValueAsString(dto);
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(patch("/products/{productCode}/undo", productCode)
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isBadRequest())
	        .andReturn();
	}

	@Test
	@DisplayName("Given a non-existing product code, when restocking individual product, then return HTTP status 404")
	void givenNonExistingProductCode_whenRestockingIndividualProduct_thenReturnHttpStatus404() throws Exception {
	    // ARRANGE
	    String productCode = "0020";
	    UndoPurchaseDTO dto = new UndoPurchaseDTO(3);
	    String json = this.mapper.writeValueAsString(dto);
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(patch("/products/{productCode}/undo", productCode)
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andReturn();
	}
	
	@Test
	@DisplayName("Given a valid massive replenishment request, when restock is performed, then return list of MassiveReplenishmentResponseDTO")
	void givenValidMassiveReplenishmentRequest_whenRestockIsPerformed_thenReturnListOfMassiveReplenishmentResponseDTO() throws Exception {
	    // ARRANGE
	    List<ProductMassiveReplenishmentDTO> dtoIn = new ArrayList<>();
	    dtoIn.add(new ProductMassiveReplenishmentDTO(ProductFactoryTest.createProductValidCode001Quantity10(), 20));
	    dtoIn.add(new ProductMassiveReplenishmentDTO(ProductFactoryTest.createProductValidCode003Quantity10(), 20));
	    
	    String json = this.mapper.writeValueAsString(dtoIn);
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(put("/products/restock")
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.length()").value(dtoIn.size()))
	        .andExpect(jsonPath("$[0].quantityReplenished").value(20))
	        .andExpect(jsonPath("$[1].quantityReplenished").value(20))
	        .andReturn();
	}

	@Test
	@DisplayName("Given an invalid request body, when performing massive replenishment in stock, then throw MethodArgumentNotValidException")
	void givenInvalidRequestBody_whenMassiveReplenishmentInStock_thenThrowMethodArgumentNotValidException() throws Exception {
	    // ARRANGE
	    List<ProductMassiveReplenishmentDTO> dtoIn = new ArrayList<>();
	    dtoIn.add(new ProductMassiveReplenishmentDTO("", null, "", null));
	    String json = this.mapper.writeValueAsString(dtoIn);
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(put("/products/restock")
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isBadRequest())
	        .andReturn();
	}

	@Test
	@DisplayName("Given a valid product update request, when updating product, then return void and HTTP status 200")
	void givenValidProductUpdateRequest_whenUpdatingProduct_thenReturnVoidAndHttpStatus200() throws Exception {
	    // ARRANGE
	    Long id = 1L;
	    ProductDTO productDTO = ProductFactoryTest.createProductDTOValidCode001Quantity10ForUpdate();
	    String json = this.mapper.writeValueAsString(productDTO);
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(patch("/products/{id}", id)
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andReturn();
	}

	@Test
	@DisplayName("Given a non-existing product id, when updating product, then return ProductNotFoundException and HTTP status 404")
	void givenNonExistingProductId_whenUpdatingProduct_thenReturnProductNotFoundExceptionAndHttpStatus404() throws Exception {
	    // ARRANGE
	    Long id = 30L;
	    ProductDTO productDTO = ProductFactoryTest.createProductDTOValidCode001Quantity10ForUpdate();
	    String json = this.mapper.writeValueAsString(productDTO);
	    
	    // ACT + ASSERT
	    this.mockMvc.perform(patch("/products/{id}", id)
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andReturn();
	}

}
