package com.github.im2back.stockms.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.github.im2back.stockms.controller.util.Util;
import com.github.im2back.stockms.model.dto.inputdata.ProductRegister;
import com.github.im2back.stockms.model.dto.inputdata.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.dto.inputdata.UndoPurchaseDto;
import com.github.im2back.stockms.model.dto.outputdata.ProductDto;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseResponseDto;
import com.github.im2back.stockms.service.ProductService;

@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@WebMvcTest(ProductController.class)
class ProductControllerTest {
	
	@MockBean
	private ProductService service;

	@Autowired
	MockMvc mvc;
	
	@Autowired
	private JacksonTester<ProductDto> productDtoJackson;
	
	@Autowired
	private JacksonTester<ProductRegister> productRegisterJackson;
	
	@Autowired
	private JacksonTester<ProductsPurchaseRequestDto> productsPurchaseRequestDtoJackson;
	
	@Autowired
	private JacksonTester<PurchaseResponseDto> purchaseResponseDtoJackson;
	
	@Autowired
	private JacksonTester<UndoPurchaseDto> undoPurchaseDtoJackson;
	
	@Test
	@DisplayName("Deveria retornar um ProductDto com base no id informado")
	void findProductById() throws Exception {
		//ARRANGE
		Long id = 1l;
		BDDMockito.when(service.findProductById(id)).thenReturn(Util.product);
		
		//ACT
		var response = mvc.perform(get("/product/{id}",id))
				.andExpect(status().isOk())
				.andReturn().getResponse();
		
		var responseBody = productDtoJackson.parseObject(response.getContentAsString());
		
		//ASSERT
		Assertions.assertEquals(1l, responseBody.id(),"O id do dto retornado pelo endpoint deve ser igual ao recebido no path");
		verify(service,times(1)).findProductById(1l);	
	}


	@Test
	@DisplayName("Deveria retornar um ProductDto com base no codigo informado")
	void findProductByCode() throws Exception {
		//ARRANGE
		String code = "001";
		BDDMockito.when(service.findByCode(code)).thenReturn(Util.product);
		
		//ACT
		var response = mvc.perform(get("/product/code").param("code", code))
				.andExpect(status().isOk())
				.andReturn().getResponse();
		
		var responseBody = productDtoJackson.parseObject(response.getContentAsString());
		
		//ASSERT
		Assertions.assertEquals("001", responseBody.code(),"O codigo do dto retornado pelo endpoint deve ser igual ao recebido no path");
		verify(service,times(1)).findByCode("001");
	}	

	@Test
	@DisplayName("Deveria salvar um novo produto apartir de um dto recebido")
	void saveNewProduct() throws Exception {
		//ARRANGE
		String jsonRequest = this.productRegisterJackson.write(Util.productRegister).getJson();
		BDDMockito.when(service.saveNewProduct(Util.productRegister)).thenReturn(Util.ProductDto);
		
		//ACT
		var response = mvc.perform(post("/product").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isCreated())
				.andReturn().getResponse();
		
		var responseBody = this.productDtoJackson.parseObject(response.getContentAsString());
		//ASSERT
		Assertions.assertEquals(Util.productRegister.quantity(), responseBody.quantity(),
				"A quantidade cadastrada deve ser igual a requisitada no dto de entrada");
		
		Assertions.assertEquals(Util.productRegister.code(), responseBody.code(),
				"O código do produto casdastrado deve ser igual ao requisitado no dto de entrada");
		
		verify(service,times(1)).saveNewProduct(Util.productRegister);
	}
		
	@Test
	@DisplayName("Deveria excluir um produto do banco de dados com base no id recebido")
	void deleteProductById() throws Exception {
		//ARRANGE
		Long id = 1l;
		BDDMockito.doNothing().when(service).deleProductById(id);
		
		//ACT
		 mvc.perform(delete("/product/{id}",id))
				.andExpect(status().isNoContent())
				.andReturn().getResponse();	
		
		//ASSERT
		verify(service,times(1)).deleProductById(id);
	}
		
	@Test
	@DisplayName("Deveria atualizar o Estoque apartir de um DTO de compra e retornar um dto da compra")
	void updateStock() throws Exception {
		//ARRANGE
		String jsonRequest = this.productsPurchaseRequestDtoJackson.write(Util.productsPurchaseRequestDto).getJson();
		BDDMockito.when(service.updateStock(Util.productsPurchaseRequestDto)).thenReturn(Util.purchaseResponseDto);
		
		//ACT
		var response = mvc.perform(post("/product/purchase").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn().getResponse();
		
		var responseBody = this.purchaseResponseDtoJackson.parseObject(response.getContentAsString());
		
		//ASSERT
		verify(service,times(1)).updateStock(Util.productsPurchaseRequestDto);
		Assertions.assertEquals(new BigDecimal(1000), responseBody.total(),
				"O dto de resposta deveria conter o mesmo total retornado pela classe de serviço");				
	}
		
	
	@Test
	@DisplayName("Desfaz uma compra e retorna o produto para o estoque")
	void undoPurchase() throws Exception {
		//ARRANGE
		String jsonRequest = this.undoPurchaseDtoJackson.write(Util.undoPurchaseDto).getJson();
		BDDMockito.doNothing().when(service).undoPurchase(Util.undoPurchaseDto);
		
		//ACT
		 mvc.perform(put("/product/undopurchase").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn().getResponse();
				
		//ASSERT
		verify(service,times(1)).undoPurchase(Util.undoPurchaseDto);
	}

	
	@Test
	@DisplayName("Atualiza os dados de um produto apartir de um dto recebido")
	void updateProduct() throws Exception {
		//ARRANGE
		String jsonRequest = this.productDtoJackson.write(Util.ProductDto).getJson();
		BDDMockito.doNothing().when(service).updateProduct(Util.ProductDto);
		
		//ACT
		mvc.perform(put("/product/update").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
		.andExpect(status().isOk())
		.andReturn().getResponse();
		
		//ASSERT
		verify(service,times(1)).updateProduct(Util.ProductDto);
	}

}
