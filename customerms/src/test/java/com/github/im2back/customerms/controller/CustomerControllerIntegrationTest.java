package com.github.im2back.customerms.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.github.im2back.customerms.model.dto.datainput.PurchaseHistoryInDTO;
import com.github.im2back.customerms.model.dto.datainput.RegisterCustomerDTO;
import com.github.im2back.customerms.util.CustomerFactory;
import com.github.im2back.customerms.util.UtilObjectsFactory;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
class CustomerControllerIntegrationTest {
	

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JacksonTester<RegisterCustomerDTO> jacksonTesterRegisterCustomerDTO;
	
	@Autowired
	private JacksonTester<PurchaseHistoryInDTO> jacksonTesterPurchaseHistoryInDTO;

	@Test
	@DisplayName("Given a valid id, when finding customer by id, then return CustomerDTO and HTTP status 200")
	void givenValidId_whenFindingCustomerById_thenReturnCustomerDTOAndHttpStatus200() throws Exception {
	    // ARRANGE
	    Long id = 1L;
	    
	    // ACT + ASSERT
	    mockMvc.perform(get("/customers/{id}", id))
	           .andExpect(status().isOk())
	           .andExpect(jsonPath("$.id").value(1L))
	           .andReturn();
	}
	
	@Test
	@DisplayName("Given a non-existing id, when finding customer by id, then return HTTP status 404")
	void givenNonExistingId_whenFindingCustomerById_thenReturnHttpStatus404() throws Exception {
	    // ARRANGE
	    Long id = 3L;
	    
	    // ACT + ASSERT
	    mockMvc.perform(get("/customers/{id}", id))
	           .andExpect(status().isNotFound())
	           .andReturn();
	}

	@Test
	@DisplayName("Given a valid document, when finding customer by document, then return CustomerDTO and HTTP status 200")
	void givenValidDocument_whenFindingCustomerByDocument_thenReturnCustomerDTOAndHttpStatus200() throws Exception {
	    // ARRANGE
	    String document = "00769203213";
	    
	    // ACT
	    mockMvc.perform(get("/customers/document/{document}", document))
	           .andExpect(status().isOk())
	           .andExpect(jsonPath("$.document").value("00769203213"))
	           .andReturn();
	}

	@Test
	@DisplayName("Given an invalid document, when finding customer by document, then return HTTP status 404")
	void givenInvalidDocument_whenFindingCustomerByDocument_thenReturnHttpStatus404() throws Exception {
	    // ARRANGE
	    String document = "00769603240";
	    
	    // ACT
	    mockMvc.perform(get("/customers/document/{document}", document))
	           .andExpect(status().isNotFound())
	           .andReturn();
	}

	@Test
	@DisplayName("Given a valid request body, when saving new customer, then return CustomerDTO and HTTP status 201")
	void givenValidRequestBody_whenSavingNewCustomer_thenReturnCustomerDTOAndHttpStatus201() throws Exception {
	    // ARRANGE
	    RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTONotRegistered();
	    String jsonRequest = this.jacksonTesterRegisterCustomerDTO.write(registerCustomerDTO).getJson();
	    
	    // ACT
	    mockMvc.perform(post("/customers")
	            .content(jsonRequest)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isCreated())
	        .andExpect(jsonPath("$.document").value(registerCustomerDTO.document()))
	        .andReturn();
	}

	@Test
	@DisplayName("Given a valid registration DTO that triggers a business exception, when saving a new customer, then return RegisterValidationException and HTTP status 422")
	void givenValidRegistrationDTO_whenSavingNewCustomerAndBusinessExceptionOccurs_thenReturnRegisterValidationExceptionAndHttpStatus422() throws Exception {
	    // ARRANGE
	    RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTO();
	    String jsonRequest = this.jacksonTesterRegisterCustomerDTO.write(registerCustomerDTO).getJson();
	    
	    // ACT
	    mockMvc.perform(post("/customers")
	            .content(jsonRequest)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isUnprocessableEntity())
	        .andReturn();
	}

	@Test
	@DisplayName("Given an invalid registration DTO, when saving a new customer, then return HTTP status 400")
	void givenInvalidRegistrationDTO_whenSavingNewCustomer_thenReturnHttpStatus400() throws Exception {
	    // ARRANGE
	    RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTONotValid();
	    String jsonRequest = this.jacksonTesterRegisterCustomerDTO.write(registerCustomerDTO).getJson();
	    
	    // ACT + ASSERT
	    mockMvc.perform(post("/customers")
	            .content(jsonRequest)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isBadRequest())
	        .andReturn();
	}

	@Test
	@DisplayName("Given a valid document, when deleting customer by document, then return void and HTTP status 204")
	void givenValidDocument_whenDeletingCustomerByDocument_thenReturnVoidAndHttpStatus204() throws Exception {
	    // ARRANGE
	    String document = "00769203213";
	    
	    // ACT + ASSERT
	    mockMvc.perform(patch("/customers/{document}", document))
	           .andExpect(status().isNoContent())
	           .andReturn();
	}

	@Test
	@DisplayName("Given a non-existing document, when deleting customer by document, then return void and HTTP status 404")
	void givenNonExistingDocument_whenDeletingCustomerByDocument_thenReturnVoidAndHttpStatus404() throws Exception {
	    // ARRANGE
	    String documentNotExistInDataBase = "11869203213";
	    
	    // ACT + ASSERT
	    mockMvc.perform(patch("/customers/{document}", documentNotExistInDataBase))
	           .andExpect(status().isNotFound())
	           .andReturn();
	}

	@Test
	@DisplayName("Given a valid purchase history request body, when persisting purchase history, then return PurchaseHistoryOutDTO and HTTP status 200")
	void givenValidPurchaseHistoryRequestBody_whenPersistingPurchaseHistory_thenReturnPurchaseHistoryOutDTOAndHttpStatus200() throws Exception {
	    // ARRANGE
	    PurchaseHistoryInDTO purchaseHistoryInDTO = UtilObjectsFactory.createPurchaseHistoryInDTO();
	    String jsonRequest = this.jacksonTesterPurchaseHistoryInDTO.write(purchaseHistoryInDTO).getJson();
	    
	    BigDecimal totalExpected = new BigDecimal(599.7).setScale(2, RoundingMode.HALF_UP);
	    
	    // ACT + ASSERT
	    mockMvc.perform(post("/customers/purchase-history")
	            .content(jsonRequest)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.total").value(totalExpected.doubleValue())) // Transform to double to avoid Jackson conflicts
	        .andExpect(jsonPath("$.purchasedProducts.length()").value(3))
	        .andReturn();
	}

	@Test
	@DisplayName("Given an invalid purchase history request body, when persisting purchase history, then return HTTP status 400")
	void givenInvalidPurchaseHistoryRequestBody_whenPersistingPurchaseHistory_thenReturnHttpStatus400() throws Exception {
	    // ARRANGE
	    PurchaseHistoryInDTO purchaseHistoryInDTO = UtilObjectsFactory.createPurchaseHistoryInDTONotValid();
	    String jsonRequest = this.jacksonTesterPurchaseHistoryInDTO.write(purchaseHistoryInDTO).getJson();
	    
	    // ACT + ASSERT
	    mockMvc.perform(post("/customers/purchase-history")
	            .content(jsonRequest)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isBadRequest())
	        .andReturn();
	}

	@Test
	@DisplayName("Given a non-existing document, when persisting purchase history, then return HTTP status 404")
	void givenNonExistingDocument_whenPersistingPurchaseHistory_thenReturnHttpStatus404() throws Exception {
	    // ARRANGE
	    PurchaseHistoryInDTO purchaseHistoryInDTO = UtilObjectsFactory.createPurchaseHistoryInDTODocumentNotValid();
	    String jsonRequest = this.jacksonTesterPurchaseHistoryInDTO.write(purchaseHistoryInDTO).getJson();
	    
	    // ACT
	    mockMvc.perform(post("/customers/purchase-history")
	            .content(jsonRequest)
	            .contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andReturn();
	}

	@Test
	@DisplayName("Given a valid purchase id, when undoing purchase, then return void and HTTP status 204")
	void givenValidPurchaseId_whenUndoingPurchase_thenReturnVoidAndHttpStatus204() throws Exception {
	    // ARRANGE
	    Long id = 1L;
	    
	    // ACT
	    mockMvc.perform(delete("/customers/purchase-history/{purchaseId}", id))
	           .andExpect(status().isNoContent())
	           .andReturn();
	}

	@Test
	@DisplayName("Given a valid purchase id, when processing individual payment, then return void and HTTP status 200")
	void givenValidPurchaseId_whenProcessingIndividualPayment_thenReturnVoidAndHttpStatus200() throws Exception {
	    // ARRANGE
	    Long id = 1L;
	    
	    // ACT + ASSERT
	    mockMvc.perform(patch("/customers/purchase-history/{purchaseId}", id))
	           .andExpect(status().isOk())
	           .andReturn();
	}

	@Test
	@DisplayName("Given a valid document, when generating purchase note, then return void and HTTP status 200")
	void givenValidDocument_whenGeneratingPurchaseNote_thenReturnVoidAndHttpStatus200() throws Exception {
	    // ARRANGE
	    String document = "00769203213";
	    
	    // ACT + ASSERT
	    mockMvc.perform(get("/customers/note").param("document", document))
	           .andExpect(status().isOk())
	           .andReturn();
	}

	@Test
	@DisplayName("Given an invalid document, when generating purchase note, then return void and HTTP status 404")
	void givenInvalidDocument_whenGeneratingPurchaseNote_thenReturnVoidAndHttpStatus404() throws Exception {
	    // ARRANGE
	    String document = "0076920123123";
	    
	    // ACT
	    mockMvc.perform(get("/customers/note").param("document", document))
	           .andExpect(status().isNotFound())
	           .andReturn();
	}

	@Test
	@DisplayName("Given a valid document, when clearing debt, then return void and HTTP status 200")
	void givenValidDocument_whenClearingDebt_thenReturnVoidAndHttpStatus200() throws Exception {
	    // ARRANGE
	    String document = "00769203213";
	    
	    // ACT + ASSERT
	    mockMvc.perform(patch("/customers/purchase-history/document/{document}", document))
	           .andExpect(status().isOk())
	           .andReturn();
	}

	
//	@Test
//	@DisplayName("Given metrics are available, when fetching metrics, then return HTTP status 200")
//	void givenMetricsAvailable_whenFetchingMetrics_thenReturnHttpStatus200() throws Exception {
//	    // ARRANGE
//
//	    // ACT + ASSERT
//	    mockMvc.perform(get("/customers/metrics"))
//	           .andExpect(status().isOk())
//	           .andReturn();
//	}
	
}
