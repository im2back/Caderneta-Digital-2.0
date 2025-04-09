package com.github.im2back.validation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.validation.dto.ProductsPurchaseRequestDto;
import com.github.im2back.validation.dto.PurchasedItem;


@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class ValidationControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@DisplayName("Given a valid purchase request, when validating purchase, then return HTTP status OK")
	void givenValidPurchaseRequest_whenValidatingPurchase_thenReturnHttpStatusOK() throws Exception {
	    // ARRANGE
	    List<PurchasedItem> items = List.of(new PurchasedItem("001", 2));
	    ProductsPurchaseRequestDto dto = new ProductsPurchaseRequestDto("00769203213", items);
	    String json = this.mapper.writeValueAsString(dto);

	    // ACT + ASSERT
	    this.mockMvc.perform(post("/validations/purchase")
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andReturn();
	}

	@Test
	@DisplayName("Given a purchase request with invalid quantity, when validating purchase, then return HTTP status 422")
	void givenPurchaseRequestWithInvalidQuantity_whenValidatingPurchase_thenReturnHttpStatus422() throws Exception {
	    // ARRANGE
	    List<PurchasedItem> items = List.of(new PurchasedItem("001", 200));
	    ProductsPurchaseRequestDto dto = new ProductsPurchaseRequestDto("00769203213", items);
	    String json = this.mapper.writeValueAsString(dto);

	    // ACT + ASSERT
	    this.mockMvc.perform(post("/validations/purchase")
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isUnprocessableEntity())
	        .andReturn();
	}

	@Test
	@DisplayName("Given a non-existent customer document, when validating purchase, then return HTTP status 422")
	void givenNonExistentCustomerDocument_whenValidatingPurchase_thenReturnHttpStatus422() throws Exception {
	    // ARRANGE
	    List<PurchasedItem> items = List.of(new PurchasedItem("001", 2));
	    ProductsPurchaseRequestDto dto = new ProductsPurchaseRequestDto("00769203215", items);
	    String json = this.mapper.writeValueAsString(dto);

	    // ACT
	    this.mockMvc.perform(post("/validations/purchase")
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isUnprocessableEntity())
	        .andReturn();
	}


}
