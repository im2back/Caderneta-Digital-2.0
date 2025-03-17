package com.github.im2back.customerms.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
	void findCustomerById_ShouldReturnCustomerDTOAndHttpStatus200_WhenIdIsValid() throws Exception {
		//ARRANGE
		Long id = 1l;
		
		//ACT + ASSERT
		mockMvc.perform(get("/customers/{id}", id))
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$.id").value(1l))
	    .andReturn();
	}
	
	@Test
	void findCustomerById_ShouldThrowCustomerNotFoundExceptionAndHttpStatu404_WhenIdIsNotFound() throws Exception {
		//ARRANGE
		Long id = 3l;
		
		//ACT + ASSERT
		mockMvc.perform(get("/customers/{id}", id))
	    .andExpect(status().isNotFound())
	    .andReturn();
	}
	
	@Test
	void findCustomerByDocument_ShouldReturnCustomerDTOAndHttpStatus200_WhenDocumentIsValid() throws Exception {
		//ARRANGE
		String document = "00769203213";
		
		//ACT
		mockMvc.perform(get("/customers/document/{document}", document))
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$.document").value("00769203213"))
	    .andReturn();
	}
	
	@Test
	void findCustomerByDocument_ShouldReturnCustomerNotFoundExceptionAndHttpStatus404_WhenDocumentIsNotValid() throws Exception {
		//ARRANGE
		String document = "00769603240";
		
		//ACT
		mockMvc.perform(get("/customers/document/{document}", document))
	    .andExpect(status().isNotFound())
	    .andReturn();
	}

	@Test
	void saveNewCustomer_ShouldReturnCustomerDTOAndHttpStatus201_WhenBodyIsValid() throws Exception {
		//ARRANGE
		RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTONotRegistered();
		String jsonRequest = this.jacksonTesterRegisterCustomerDTO.write(registerCustomerDTO).getJson();		
		
		//ACT
		mockMvc.perform(post("/customers")
		        .content(jsonRequest)
		        .contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isCreated())
		    .andExpect(jsonPath("$.document").value(registerCustomerDTO.document()))
		    .andReturn();
	}
	
	@Test
	void saveNewCustomer_ShouldReturnRegisterValidationExceptionAndHttpStatus422_WhenBusinessException() throws Exception {
		//ARRANGE
		RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTO();
		String jsonRequest = this.jacksonTesterRegisterCustomerDTO.write(registerCustomerDTO).getJson();
			
		//ACT
		mockMvc.perform(post("/customers")
		        .content(jsonRequest)
		        .contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isUnprocessableEntity())
		    .andReturn();
	}
	
	@Test
	void saveNewCustomer_ShouldReturnCustomerDTOAndHttpStatus400_WhenBodyIsBodyNotValid() throws Exception {
		//ARRANGE
		RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTONotValid();
		String jsonRequest = this.jacksonTesterRegisterCustomerDTO.write(registerCustomerDTO).getJson();
		
		//ACT +ASSERT
		mockMvc.perform(post("/customers")
		        .content(jsonRequest)
		        .contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isBadRequest())
		    .andReturn();
	}
	
	@Test
	void deleteCustomerByDocument_ShouldReturnVoidAndHttpStatus200_WhenDocumentIsValid() throws Exception {
		//ARRANGE
		String document = "00769203213";
		
		//ACT + ASSERT
		mockMvc.perform(patch("/customers/{document}", document))
	    .andExpect(status().isNoContent())
	    .andReturn();
	}
	
	@Test
	void deleteCustomerByDocument_ShouldReturnVoidAndHttpStatus404_WhenDocumentIsNotValid() throws Exception {
		//ARRANGE
		String documentNotExistInDataBase = "11869203213";
		
		//ACT + ASSERT
		mockMvc.perform(patch("/customers/{document}", documentNotExistInDataBase))
	    .andExpect(status().isNotFound())
	    .andReturn();
	}
	
	@Test
	void persistPurchaseHistory_ShouldReturnPurchaseHistoryOutDTOAndHttpStatus200_WhenBodyIsValid() throws Exception {
		//ARRANGE
		PurchaseHistoryInDTO purchaseHistoryInDTO = UtilObjectsFactory.createPurchaseHistoryInDTO();
		String jsonRequest = this.jacksonTesterPurchaseHistoryInDTO.write(purchaseHistoryInDTO).getJson();
		
		BigDecimal totalExpected = new BigDecimal(599.7).setScale(2, RoundingMode.HALF_UP);	
		
		//ACT + ASSERT
		mockMvc.perform(post("/customers/purchase-history")
		        .content(jsonRequest)
		        .contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isOk())
		    .andExpect(jsonPath("$.total").value(totalExpected.doubleValue())) //Transformado para double para evitar conflito do jackson
		    .andExpect(jsonPath("$.purchasedProducts.length()").value(3))
		    .andReturn();
	}
	
	@Test
	void persistPurchaseHistory_ShouldReturnPurchaseHistoryOutDTOAndHttpStatus400_WhenBodyIsNotValid() throws Exception {
		//ARRANGE
		PurchaseHistoryInDTO purchaseHistoryInDTO = UtilObjectsFactory.createPurchaseHistoryInDTONotValid();
		String jsonRequest = this.jacksonTesterPurchaseHistoryInDTO.write(purchaseHistoryInDTO).getJson();		
		
		//ACT + ASSERT
		mockMvc.perform(post("/customers/purchase-history")
		        .content(jsonRequest)
		        .contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isBadRequest())
		    .andReturn();
	}
	
	@Test
	void persistPurchaseHistory_ShouldReturnCustomerNotFoundExceptionAndHttpStatus404_WhenDocumentIsNotFound() throws Exception {
		//ARRANGE
		PurchaseHistoryInDTO purchaseHistoryInDTO = UtilObjectsFactory.createPurchaseHistoryInDTODocumentNotValid();
		String jsonRequest = this.jacksonTesterPurchaseHistoryInDTO.write(purchaseHistoryInDTO).getJson();
		
		//ACT
		mockMvc.perform(post("/customers/purchase-history")
		        .content(jsonRequest)
		        .contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isNotFound())
		    .andReturn();
	}
	
	@Test
	void undoPurchase_ShouldReturnVoidAndHttpStatus204_WhenIdIsValid() throws Exception {
		//ARRANGE
		Long id = 1l ;
				
		//ACT
		mockMvc.perform(delete("/customers/purchase-history/{purchaseId}",id))
		    .andExpect(status().isNoContent())
		    .andReturn();
	}
	
	@Test
	void individualPayment_ShouldReturnVoidAndHttpStatus200_WhenIdIsValid() throws Exception {
		//ARRANGE
		Long id = 1l ;
		
		//ACT + ASSERT
		mockMvc.perform(patch("/customers/purchase-history/{purchaseId}",id))
		    .andExpect(status().isOk())
		    .andReturn();
	}
	
	@Test
	void generatePurchaseNote_ShouldReturnVoidAndHttpStatus200_WhenDocumentIsValid() throws Exception {
		//ARRANGE
		String document = "00769203213";
	
		//ACT + ASSERT
		mockMvc.perform(get("/customers/note").param("document", document))
		    .andExpect(status().isOk())
		    .andReturn();
	}
	
	@Test
	void generatePurchaseNote_ShouldReturnVoidAndHttpStatus404_WhenDocumentIsNotFound() throws Exception {
		//ARRANGE
		String document = "0076920123123";
				
		//ACT
		mockMvc.perform(get("/customers/note").param("document", document))
		    .andExpect(status().isNotFound())
		    .andReturn();
	}
	
	@Test
	void clearDebt_ShouldReturnVoidAndHttpStatus200_WhenDocumentIsValid() throws Exception {
		//ARRANGE
		String document = "00769203213";
		
		//ACT + ASSERT
		mockMvc.perform(patch("/customers/purchase-history/document/{document}",document))
		    .andExpect(status().isOk())
		    .andReturn();
	}
	
//	@Test
//	void getMetrics_ShouldReturnVoidAndHttpStatus200_WhenSucess() throws Exception {
//		//ARRANGE
//		
//		//ACT + ASSERT
//		mockMvc.perform(get("/customers/metrics"))
//		    .andExpect(status().isOk())
//		    .andReturn();
//	}	
}
