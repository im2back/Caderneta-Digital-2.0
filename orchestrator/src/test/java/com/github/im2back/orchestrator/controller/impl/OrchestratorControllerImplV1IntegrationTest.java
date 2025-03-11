package com.github.im2back.orchestrator.controller.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.util.PurchaseTestFactory;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class OrchestratorControllerImplV1IntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JacksonTester<PurchaseRequestDTO> purchaseRequestDTOJackson;
	
	@Autowired
	private JacksonTester<PurchaseHistoryResponseDTO> purchaseHistoryResponseDTOJackson;

	@Test
	void guivenNoAvailableProductsWhenValidatingStockThenReturnHttpStatus422() throws Exception {
		// GUIVEN
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createInvalidPurchaseRequestDTO();
		String jsonRequest = this.purchaseRequestDTOJackson.write(purchaseRequestDTO).getJson();
		
		// WHEN
		var response = mockMvc.perform(post("/v1/orchestrator/purchase").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andReturn().getResponse();

		// THEN
		Assertions.assertEquals(422, response.getStatus(),"Quantidade de produtos indisponivel");
	}
	
	@Test
	void guivenCpfNotRegisteredWhenValidatingCustomerThenReturnHttpStatus422() throws Exception {
		// GUIVEN
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createInvalidPurchaseRequestDTOUserNotExist();
		String jsonRequest = this.purchaseRequestDTOJackson.write(purchaseRequestDTO).getJson();
			
		// WHEN
		var response = mockMvc.perform(post("/v1/orchestrator/purchase").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andReturn().getResponse();

		// THEN
		Assertions.assertEquals(422, response.getStatus(),"User Not Found");
	}
	
	@Test
	void guivenProductCodeNotRegisteredWhenValidatingStockThenReturnHttpStatus422() throws Exception {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createInvalidPurchaseRequestDTOProductNotExist();
		String jsonRequest = this.purchaseRequestDTOJackson.write(purchaseRequestDTO).getJson();
		
		// ACT
		var response = mockMvc.perform(post("/v1/orchestrator/purchase").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)).andReturn().getResponse();

		// ASSERT
		Assertions.assertEquals(422, response.getStatus(),"Product Not Found");
	}
	
	@Test
	void guivenUserInactiveWhenValidatingCustomeThenReturnHttpStatus422() throws Exception {
		// GUIVEN
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createInvalidPurchaseRequestDTOUserInactive();
		
		String jsonRequest = this.purchaseRequestDTOJackson.write(purchaseRequestDTO).getJson();
		
		// WHEN
		var response = mockMvc
				.perform(post("/v1/orchestrator/purchase").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andReturn().getResponse();

		// THEN
		Assertions.assertEquals(422, response.getStatus(),"User is inactive");
	}
	
	@Test
	void guivenPurchaseWhenSucefullThenReturnPurchaseDtoAndHttpStatus200() throws Exception {
		// GUIVEN
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		PurchaseHistoryResponseDTO purchaseHistoryResponseDTO = PurchaseTestFactory
				.createPurchaseHistoryResponseDTOSuccessful();

		String jsonRequest = this.purchaseRequestDTOJackson.write(purchaseRequestDTO).getJson();
		String jsonExpected = this.purchaseHistoryResponseDTOJackson.write(purchaseHistoryResponseDTO).getJson();
		
		// WHEN
		var response = mockMvc
				.perform(post("/v1/orchestrator/purchase").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andReturn().getResponse();

		// THEN
		Assertions.assertEquals(200, response.getStatus(),"Purchase Finished");
	   assertThat(response.getContentAsString()).isEqualTo(jsonExpected);
	}

}
