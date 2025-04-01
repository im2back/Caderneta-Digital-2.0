package com.github.im2back.orchestrator.controller.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.service.orchestrator.OrchestratorService;
import com.github.im2back.orchestrator.util.PurchaseTestFactory;

@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@WebMvcTest(OrchestratorControllerImplV1.class)
class OrchestratorControllerImplV1Test {
	
	@MockBean
	private OrchestratorService orchestratorService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private JacksonTester<PurchaseRequestDTO> purchaseRequestDTOJackson;
	@Autowired
	private JacksonTester<PurchaseHistoryResponseDTO> purchaseHistoryResponseDTOJackson;
	
	@Test
	@DisplayName("Should process purchase successfully and return expected response")
	void orchestratePurchase_ShouldProcessPurchaseSuccessfully_WhenPurchaseRequestIsValid() throws Exception {
		//ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		PurchaseHistoryResponseDTO purchaseHistoryResponseDTO  = PurchaseTestFactory.createPurchaseHistoryResponseDTOSuccessful();
		
		String jsonRequest = this.purchaseRequestDTOJackson.write(purchaseRequestDTO).getJson();
		String jsonExpected = 	this.purchaseHistoryResponseDTOJackson.write(purchaseHistoryResponseDTO).getJson();
		
		BDDMockito.when(orchestratorService.orchestratePurchase(purchaseRequestDTO)).thenReturn(purchaseHistoryResponseDTO);
		
		//ACT
		var response = mockMvc.perform(post("/v1/orchestrator/purchase")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)).andExpect(status().isOk())
				.andReturn().getResponse();
		
		//ASSERT
		assertThat(response.getContentAsString()).isEqualTo(jsonExpected);
	}
}
