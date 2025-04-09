package com.github.im2back.validation.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.validation.dto.ProductsPurchaseRequestDto;
import com.github.im2back.validation.dto.PurchasedItem;
import com.github.im2back.validation.service.ValidationService;
import com.github.im2back.validation.validations.exceptions.PurchaseValidationException;

@SuppressWarnings("removal")
@AutoConfigureMockMvc
@WebMvcTest(ValidationController.class)
class ValidationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private ValidationService validationService;

	@Test
	@DisplayName("Should return HTTP status 200 when purchase validation passes")
	void validPurchase_shouldReturnOk_whenPurchaseIsValid() throws Exception {
		// ARRANGE
		List<PurchasedItem> items = List.of(new PurchasedItem("001", 2));
		ProductsPurchaseRequestDto dto = new ProductsPurchaseRequestDto("00769203213", items);
		String json = this.mapper.writeValueAsString(dto);

		doNothing().when(this.validationService).validPurchase(dto);

		// ACT
		this.mockMvc.perform(post("/validations/purchase").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		// ASSERT
		verify(this.validationService).validPurchase(dto);
	}

	@Test
	@DisplayName("Should return HTTP status 422 when purchase validation fails")
	void validPurchase_shouldReturnUnprocessableEntity_whenPurchaseValidationFails() throws Exception {
		// ARRANGE
		List<PurchasedItem> items = List.of(new PurchasedItem("001", 2));
		ProductsPurchaseRequestDto dto = new ProductsPurchaseRequestDto("00769203213", items);
		String json = this.mapper.writeValueAsString(dto);

		List<String> messages = new ArrayList<>(Arrays.asList("Erro1", "Erro2"));

		doThrow(new PurchaseValidationException(messages)).when(this.validationService).validPurchase(dto);

		// ACT
		this.mockMvc.perform(post("/validations/purchase").content(json).contentType(MediaType.APPLICATION_JSON))
				// ASSERT: Expect HTTP status 422 (Unprocessable Entity)
				.andExpect(status().isUnprocessableEntity()).andReturn();

		// ASSERT
		verify(this.validationService).validPurchase(dto);
	}

}
