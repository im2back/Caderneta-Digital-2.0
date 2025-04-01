package com.github.im2back.stockms.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.stockms.model.dto.inputdata.NewProductDTO;
import com.github.im2back.stockms.model.dto.inputdata.ProductMassiveReplenishmentDTO;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItemDTO;
import com.github.im2back.stockms.model.dto.inputdata.UndoPurchaseDTO;
import com.github.im2back.stockms.model.dto.outputdata.MassiveReplenishmentResponseDTO;
import com.github.im2back.stockms.model.dto.outputdata.ProductDTO;
import com.github.im2back.stockms.model.dto.outputdata.UpdatedStockResponseDTO;
import com.github.im2back.stockms.service.ProductService;
import com.github.im2back.stockms.utils.ExceptionFactory;
import com.github.im2back.stockms.utils.ProductFactoryTest;


@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
class ProductControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;

	private ObjectMapper mapper = new ObjectMapper();
	
	@Test
	@DisplayName("Should return ProductDTO when id is valid")
	void findProductById_ShouldReturnProductDTO_WhenIdIsValid() throws Exception {
		//ARRANGE
		Long id = 1l;
		ProductDTO productDTO = ProductFactoryTest.createProductDTOValidCode001Quantity10();
		BDDMockito.when(this.service.findProductById(id)).thenReturn(productDTO);
		
		//ACT
		this.mockMvc.perform(get("/products/{id}",id))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(id))
			.andReturn();
		
		//ASSERT
		verify(this.service).findProductById(1l);
	}
	
	@Test
	@DisplayName("Should throw ProductNotFoundException when id is not found")
	void findProductById_ShouldReturnProductNotFoundException_WhenIdNotFound() throws Exception {
		//ARRANGE
		Long id = 1l;
		doThrow(ExceptionFactory.createProductNotFoundException()).when(this.service).findProductById(id);
		
		//ACT
		this.mockMvc.perform(get("/products/{id}",id))
			.andExpect(status().isNotFound())
			.andReturn();
		
		//ASSERT
		verify(this.service).findProductById(1l);
		
	}
	
	@Test
	@DisplayName("Should return ProductDTO when code is valid")
	void findProductByCode_ShouldReturnProductDTO_WhenCodeIsValid() throws Exception {
		//ARRANGE
		String code = "001";
		ProductDTO productDTO = ProductFactoryTest.createProductDTOValidCode001Quantity10();
		BDDMockito.when(this.service.findProductByCode(code)).thenReturn(productDTO);
		
		//ACT
		this.mockMvc.perform(get("/products/code/{code}",code))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(code))
			.andReturn();
		
		//ASSERT
		verify(this.service).findProductByCode(code);
	}
	
	@Test
	@DisplayName("Should throw ProductNotFoundException when code is not found")
	void findProductByCode_ShouldReturnProductNotFoundException_WhenCodeNotFound() throws Exception {
		//ARRANGE
		String code = "0045";
		doThrow(ExceptionFactory.createProductNotFoundException()).when(this.service).findProductByCode(code);
		
		//ACT
		this.mockMvc.perform(get("/products/code/{code}",code))
			.andExpect(status().isNotFound())
			.andReturn();
		
		//ASSERT
		verify(this.service).findProductByCode(code);
	}
	
	@Test
	@DisplayName("Should throw MethodArgumentNotValidException when request body is invalid")
	void saveNewProduct_ShouldThrowMethodArgumentNotValidException_WhenRequestBodyIsInvalid() throws Exception {
		//ARRANGE
		NewProductDTO newProductDTO = ProductFactoryTest.createNewProductDTOInvalid();
		String json = this.mapper.writeValueAsString(newProductDTO);
		
		//ACT
		this.mockMvc.perform(post("/products").content(json).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn();
		
	}
	
	@Test
	@DisplayName("Should return ProductDTO when request body is valid")
	void saveNewProduct_ShouldReturnProductDTO_WhenRequestBodyIsValid() throws Exception {
		//ARRANGE
		NewProductDTO newProductDTO = ProductFactoryTest.createNewProductToSaveDTOValidCode005Quantity10();
		String json = this.mapper.writeValueAsString(newProductDTO);
		ProductDTO productDTO = ProductFactoryTest.createProductDTOValidCode005Quantity10();
		BDDMockito.when(this.service.saveNewProduct(newProductDTO)).thenReturn(productDTO);
		
		//ACT
		this.mockMvc.perform(post("/products").content(json).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.code").value(newProductDTO.code()))
			.andReturn();
	}
	
	@Test
	@DisplayName("Should throw DataIntegrityViolationException when code already exists")
	void saveNewProduct_ShouldThrowDataIntegrityViolationException_WhenCodeAlreadyExists() throws Exception {
		//ARRANGE
		NewProductDTO newProductDTO = ProductFactoryTest.createNewProductToSaveDTOValidCode001Quantity10();
		String json = this.mapper.writeValueAsString(newProductDTO);
		
		doThrow(ExceptionFactory.dataIntegrityViolationException()).when(this.service).saveNewProduct(newProductDTO);

		//ACT
		
		this.mockMvc.perform(post("/products").content(json).contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("Data Integrity Violation"))
			.andExpect(status().isConflict())
			.andDo(print());
		
		verify(this.service).saveNewProduct(newProductDTO);
	}
	
	@Test
	@DisplayName("Should return void when product deletion is successful")
	void deleteProductById_ShoudReturnVoid_WhenSucess() throws Exception {
		//ARRANGE
		Long id = 1l;
		
		doNothing().when(this.service).deleProductById(id);
		 
		//ACT
		this.mockMvc.perform(delete("/products/{id}",id))
		.andExpect(status().isNoContent())
		.andReturn();
		
		//ASSERT
		verify(this.service).deleProductById(id);
	}
	
	@Test
	@DisplayName("Should return list updated stock response DTO when body is valid")
	void updateStockAfterPurchase_ShouldReturnListUpdatedStockResponseDTO_WhenBodyIsValid() throws Exception {
		//ARRANGE
		List<PurchasedItemDTO> dtoIn = ProductFactoryTest.createListPurchasedItemDTO();
		String json = this.mapper.writeValueAsString(dtoIn);
		
		List<UpdatedStockResponseDTO> list = new ArrayList<>();
		list.add(new UpdatedStockResponseDTO(ProductFactoryTest.createProductValidCode001Quantity10(),5 ));
		list.add(new UpdatedStockResponseDTO(ProductFactoryTest.createProductValidCode003Quantity10(),5 ));
		
		BDDMockito.when(this.service.updateQuantityProductsAfterPurchase(dtoIn)).thenReturn(list);
		
		//ACT
		this.mockMvc.perform(put("/products").content(json).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(dtoIn.size()))
		.andReturn();
		
		//ASSERT
		verify(this.service).updateQuantityProductsAfterPurchase(dtoIn);
	}
	
	@Test
	@DisplayName("Should throw ProductNotFoundException when product is not found")
	void updateStockAfterPurchase_ShouldReturnProductNotFoundException_WhenProductNotFound() throws Exception {
		//ARRANGE
		List<PurchasedItemDTO> dtoIn = ProductFactoryTest.createListPurchasedItemDTO();
		String json = this.mapper.writeValueAsString(dtoIn);
		
		List<UpdatedStockResponseDTO> list = new ArrayList<>();
		list.add(new UpdatedStockResponseDTO(ProductFactoryTest.createProductValidCode001Quantity10(),5 ));
		list.add(new UpdatedStockResponseDTO(ProductFactoryTest.createProductValidCode003Quantity10(),5 ));
		
		doThrow(ExceptionFactory.createProductNotFoundException()).when(this.service).updateQuantityProductsAfterPurchase(dtoIn);
		
		
		//ACT
		this.mockMvc.perform(put("/products").content(json).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andDo(print());
		
		//ASSERT
		verify(this.service).updateQuantityProductsAfterPurchase(dtoIn);
	}
	
	@Test
	@DisplayName("Should return void when individual product restock is successful")
	void restockIndividualProduct_ShouldReturnVoid_WhenSuccess() throws Exception {
		//ARRANGE
		String productCode = "003";
		UndoPurchaseDTO dto = new UndoPurchaseDTO(3);
		String json = this.mapper.writeValueAsString(dto);
		
		//ACT
		this.mockMvc.perform(patch("/products/{productCode}/undo",productCode).content(json).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
		
		//ASSERT
			verify(this.service).undoIndividualPurchase(dto, productCode);
	}
	
	@Test
	@DisplayName("Should throw MethodArgumentNotValidException when individual product restock body is not valid")
	void restockIndividualProduct_ShouldThrowMethodArgumentNotValidException_WhenBodyNotValid() throws Exception {
		//ARRANGE
		String productCode = "001";
		UndoPurchaseDTO dto = new UndoPurchaseDTO(null);
		String json = this.mapper.writeValueAsString(dto);
		
		//ACT
		this.mockMvc.perform(patch("/products/{productCode}/undo",productCode).content(json).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn();
	}
	
	@Test
	@DisplayName("Should throw ProductNotFoundException when product code is not found")
	void restockIndividualProduct_ShouldReturnProductNotFoundException_WhenProductCodeNotFound() throws Exception {
		//ARRANGE
		String productCode = "0020";
		UndoPurchaseDTO dto = new UndoPurchaseDTO(3);
		String json = this.mapper.writeValueAsString(dto);
		
		doThrow(ExceptionFactory.createProductNotFoundException()).when(this.service).undoIndividualPurchase(dto, productCode);
		
		//ACT
		this.mockMvc.perform(patch("/products/{productCode}/undo",productCode).content(json).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn();
		
		//ASSERT
			verify(this.service).undoIndividualPurchase(dto, productCode);
	}
	
	@Test
	@DisplayName("Should return list of MassiveReplenishmentResponseDTO when restock is successful")
	void massiveReplenishmentInStock_ShouldReturnListMassiveReplenishmentResponseDTO_WhenRestockIsSuccessful() throws Exception {
		//ARRANGE
		List<ProductMassiveReplenishmentDTO> dtoIn = new ArrayList<>();
		dtoIn.add(new ProductMassiveReplenishmentDTO(ProductFactoryTest.createProductValidCode001Quantity10(), 20));
		dtoIn.add(new ProductMassiveReplenishmentDTO(ProductFactoryTest.createProductValidCode003Quantity10(), 20));
		
		List<MassiveReplenishmentResponseDTO> responseList = new ArrayList<>();
		responseList.add(new MassiveReplenishmentResponseDTO(ProductFactoryTest.createProductValidCode001Quantity10(), 20));
		responseList.add(new MassiveReplenishmentResponseDTO(ProductFactoryTest.createProductValidCode003Quantity10(), 20));
		
		BDDMockito.when(this.service.massiveReplenishment(dtoIn)).thenReturn(responseList);
		
		String json = this.mapper.writeValueAsString(dtoIn);
	
		//ACT
		this.mockMvc.perform(put("/products/restock").content(json).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(dtoIn.size()))
			.andExpect(jsonPath("$[0].quantityReplenished").value(20))
			.andExpect(jsonPath("$[1].quantityReplenished").value(20))
			.andDo(print());
		
		//ASSERT
		verify(this.service).massiveReplenishment(dtoIn);
	}
	
	@Test
	@DisplayName("Should return MethodArgumentNotValidException when request not valid")
	void massiveReplenishmentInStock_ShouldReturnMethodArgumentNotValidException_BodyIsInvalid() throws Exception {
		//ARRANGE
		List<ProductMassiveReplenishmentDTO> dtoIn = new ArrayList<>();
		dtoIn.add(new ProductMassiveReplenishmentDTO("", null, "", null));
			
		String json = this.mapper.writeValueAsString(dtoIn);
	
		//ACT
		this.mockMvc.perform(put("/products/restock").content(json).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}
	
	@Test
	@DisplayName("Should return void when product update is successful")
	void updateProduct_ShouldReturnVoid_WhenUpdateIsSuccessful() throws Exception {
		//ARRANGE
		Long id = 1l;
		
		ProductDTO productDTO = ProductFactoryTest.createProductDTOValidCode001Quantity10ForUpdate();
		String json = this.mapper.writeValueAsString(productDTO);
		doNothing().when(this.service).updateProduct(productDTO, id);
		
		//ACT
		this.mockMvc.perform(patch("/products/{id}",id).content(json).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();	
		
		//ASSERT
		verify(this.service).updateProduct(productDTO,id);
	}
	
	@Test
	@DisplayName("Should return void when product update is successful")
	void updateProduct_ShouldReturnProductNotFoundException_WhenIdNotFound() throws Exception {
		//ARRANGE
		Long id = 30l;
		
		ProductDTO productDTO = ProductFactoryTest.createProductDTOValidCode001Quantity10ForUpdate();
		String json = this.mapper.writeValueAsString(productDTO);
		
		doThrow(ExceptionFactory.createProductNotFoundException()).when(this.service).updateProduct(productDTO, id);
		
		//ACT
		this.mockMvc.perform(patch("/products/{id}",id).content(json).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn();	
		
		//ASSERT
		verify(this.service).updateProduct(productDTO,id);
	}
	
}
