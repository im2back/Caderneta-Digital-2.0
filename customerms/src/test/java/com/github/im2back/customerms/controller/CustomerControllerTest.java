package com.github.im2back.customerms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

import com.github.im2back.customerms.model.dto.datainput.PurchaseHistoryInDTO;
import com.github.im2back.customerms.model.dto.datainput.RegisterCustomerDTO;
import com.github.im2back.customerms.model.dto.dataoutput.CustomerDTO;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseHistoryOutDTO;
import com.github.im2back.customerms.model.dto.dataoutput.metrics.DataForMetricsDTO;
import com.github.im2back.customerms.service.CustomerService;
import com.github.im2back.customerms.util.CustomerFactory;
import com.github.im2back.customerms.util.ExceptionFactory;
import com.github.im2back.customerms.util.UtilObjectsFactory;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CustomerControllerTest {
	
	@MockBean
	private CustomerService service;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JacksonTester<RegisterCustomerDTO> jacksonTesterRegisterCustomerDTO;
	
	@Autowired
	private JacksonTester<PurchaseHistoryInDTO> jacksonTesterPurchaseHistoryInDTO;

	@Test
	void findCustomerByIdShouldReturnCustomerDTOAndHttpStatus200WhenIdIsValid() throws Exception {
		//ARRANGE
		Long id = 1l;
		CustomerDTO customer = CustomerFactory.createCustomerDTO();
		BDDMockito.when(this.service.findCustomerById(id)).thenReturn(customer);
		
		//ACT
		mockMvc.perform(get("/customers/{id}", id))
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$.id").value(customer.id()))
	    .andReturn();

		//ASSERT
		verify(service).findCustomerById(1l);	
	}
	
	@Test
	void findCustomerByIdShouldThrowCustomerNotFoundExceptionAndHttpStatu404WhenIdIsNotFound() throws Exception {
		//ARRANGE
		Long id = 1l;
		doThrow(ExceptionFactory.createCustomerNotFoundException()).when(this.service).findCustomerById(id);
		
		
		//ACT
		mockMvc.perform(get("/customers/{id}", id))
	    .andExpect(status().isNotFound())
	    .andReturn();

		//ASSERT
		verify(service).findCustomerById(1l);	
	}
	
	@Test
	void findCustomerByDocumentShouldReturnCustomerDTOAndHttpStatus200WhenDocumentIsValid() throws Exception {
		//ARRANGE
		String document = "00769203213";
		CustomerDTO customer = CustomerFactory.createCustomerDTO();
		BDDMockito.when(this.service.findCustomerWithUnpaidPurchases(document)).thenReturn(customer);
		
		//ACT
		mockMvc.perform(get("/customers/document/{document}", document))
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$.document").value(customer.document()))
	    .andReturn();

		//ASSERT
		verify(service).findCustomerWithUnpaidPurchases("00769203213");	
	}
	
	@Test
	void findCustomerByDocumentShouldReturnCustomerNotFoundExceptionAndHttpStatus404WhenDocumentIsNotValid() throws Exception {
		//ARRANGE
		String document = "00769203213";
		CustomerDTO customer = CustomerFactory.createCustomerDTO();
		BDDMockito.when(this.service.findCustomerWithUnpaidPurchases(document)).thenReturn(customer);
		doThrow(ExceptionFactory.createCustomerNotFoundException()).when(this.service).findCustomerWithUnpaidPurchases(document);
		
		//ACT
		mockMvc.perform(get("/customers/document/{document}", document))
	    .andExpect(status().isNotFound())
	    .andReturn();

		//ASSERT
		verify(service).findCustomerWithUnpaidPurchases("00769203213");	
	}

	@Test
	void saveNewCustomerShouldReturnCustomerDTOAndHttpStatus201WhenBodyIsValid() throws Exception {
		//ARRANGE
		CustomerDTO customerDTO = CustomerFactory.createCustomerDTO();
		RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTO();
		String jsonRequest = this.jacksonTesterRegisterCustomerDTO.write(registerCustomerDTO).getJson();
		
		BDDMockito.when(this.service.saveNewCustomer(any())).thenReturn(customerDTO);
		
		//ACT
		mockMvc.perform(post("/customers")
		        .content(jsonRequest)
		        .contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isCreated())
		    .andExpect(jsonPath("$.document").value(customerDTO.document()))
		    .andReturn();

		//ASSERT
		verify(service).saveNewCustomer(registerCustomerDTO);	
	}
	
	@Test
	void saveNewCustomerShouldReturnRegisterValidationExceptionAndHttpStatus422WhenBusinessException() throws Exception {
		//ARRANGE
		CustomerDTO customerDTO = CustomerFactory.createCustomerDTO();
		RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTO();
		String jsonRequest = this.jacksonTesterRegisterCustomerDTO.write(registerCustomerDTO).getJson();
		
		BDDMockito.when(this.service.saveNewCustomer(any())).thenReturn(customerDTO);
		doThrow(ExceptionFactory.createCustomerRegisterValidationException()).when(this.service).saveNewCustomer(any());
		
		//ACT
		mockMvc.perform(post("/customers")
		        .content(jsonRequest)
		        .contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isUnprocessableEntity())
		    .andReturn();

		//ASSERT
		verify(service).saveNewCustomer(registerCustomerDTO);	
	}
	
	@Test
	void saveNewCustomerShouldReturnCustomerDTOAndHttpStatus400WhenBodyIsBodyNotValid() throws Exception {
		//ARRANGE
		CustomerDTO customerDTO = CustomerFactory.createCustomerDTO();
		RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTONotValid();
		String jsonRequest = this.jacksonTesterRegisterCustomerDTO.write(registerCustomerDTO).getJson();
		
		BDDMockito.when(this.service.saveNewCustomer(any())).thenReturn(customerDTO);
		
		//ACT +ASSERT
		mockMvc.perform(post("/customers")
		        .content(jsonRequest)
		        .contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isBadRequest())
		    .andReturn();
	}
	
	@Test
	void deleteCustomerByDocumentShouldReturnVoidAndHttpStatus200WhenDocumentIsValid() throws Exception {
		//ARRANGE
		String document = "00769203213";
		doNothing().when(this.service).logicalCustomerDeletion(document);
		
		//ACT
		mockMvc.perform(patch("/customers/{document}", document))
	    .andExpect(status().isNoContent())
	    .andReturn();

		//ASSERT
		verify(service).logicalCustomerDeletion("00769203213");	
	}
	
	@Test
	void deleteCustomerByDocument_ShouldReturnVoidAndHttpStatus404_WhenDocumentIsNotValid() throws Exception {
		//ARRANGE
		String document = "00769203213";
		doThrow(ExceptionFactory.createCustomerNotFoundException()).when(service).logicalCustomerDeletion(document);
		
		//ACT
		mockMvc.perform(patch("/customers/{document}", document))
	    .andExpect(status().isNotFound())
	    .andReturn();

		//ASSERT
		verify(service).logicalCustomerDeletion("00769203213");	
	}
	
	@Test
	void persistPurchaseHistory_ShouldReturnPurchaseHistoryOutDTOAndHttpStatus200_WhenBodyIsValid() throws Exception {
		//ARRANGE
		PurchaseHistoryInDTO purchaseHistoryInDTO = UtilObjectsFactory.createPurchaseHistoryInDTO();
		String jsonRequest = this.jacksonTesterPurchaseHistoryInDTO.write(purchaseHistoryInDTO).getJson();
		PurchaseHistoryOutDTO purchaseHistoryOutDTO = UtilObjectsFactory.createPurchaseHistoryOutDTO();
		
		BDDMockito.when(this.service.registerPurchase(any())).thenReturn(purchaseHistoryOutDTO);
		BigDecimal totalExpected = new BigDecimal(599.7).setScale(2, RoundingMode.HALF_UP);
		
		
		//ACT
		mockMvc.perform(post("/customers/purchase-history")
		        .content(jsonRequest)
		        .contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isOk())
		    .andExpect(jsonPath("$.total").value(totalExpected.doubleValue())) //Transformado para double para evitar conflito do jackson
		    .andReturn();

		//ASSERT
		verify(service).registerPurchase(purchaseHistoryInDTO);	
	}
	
	@Test
	void persistPurchaseHistory_ShouldReturnPurchaseHistoryOutDTOAndHttpStatus400_WhenBodyIsNotValid() throws Exception {
		//ARRANGE
		PurchaseHistoryInDTO purchaseHistoryInDTO = UtilObjectsFactory.createPurchaseHistoryInDTONotValid();
		String jsonRequest = this.jacksonTesterPurchaseHistoryInDTO.write(purchaseHistoryInDTO).getJson();		
		
		//ACT + ACT
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
				
		doThrow(ExceptionFactory.createCustomerNotFoundException()).when(this.service).registerPurchase(purchaseHistoryInDTO);
		
		//ACT
		mockMvc.perform(post("/customers/purchase-history")
		        .content(jsonRequest)
		        .contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isNotFound())
		    .andReturn();

		//ASSERT
		verify(service).registerPurchase(purchaseHistoryInDTO);	
	}
	
	@Test
	void undoPurchase_ShouldReturnVoidAndHttpStatus204_WhenIdIsValid() throws Exception {
		//ARRANGE
		Long id = 10l ;
				
		//ACT
		mockMvc.perform(delete("/customers/purchase-history/{purchaseId}",id))
		    .andExpect(status().isNoContent())
		    .andReturn();

		//ASSERT
		verify(service).undoPurchase(10l);	
	}
	
	@Test
	void individualPayment_ShouldReturnVoidAndHttpStatus200_WhenIdIsValid() throws Exception {
		//ARRANGE
		Long id = 10l ;
		doNothing().when(this.service).individualPayment(id);		
		
		//ACT
		mockMvc.perform(patch("/customers/purchase-history/{purchaseId}",id))
		    .andExpect(status().isOk())
		    .andReturn();

		//ASSERT
		verify(this.service).individualPayment(10l);	
	}
	
	@Test
	void generatePurchaseNote_ShouldReturnVoidAndHttpStatus200_WhenDocumentIsValid() throws Exception {
		//ARRANGE
		String document = "00769203213";
		
		doNothing().when(this.service).generatePurchaseInvoice(document);		
		
		//ACT
		mockMvc.perform(get("/customers/note").param("document", document))
		    .andExpect(status().isOk())
		    .andReturn();

		//ASSERT
		verify(this.service).generatePurchaseInvoice(document);
	}
	
	@Test
	void generatePurchaseNote_ShouldReturnVoidAndHttpStatus404_WhenDocumentIsNotFound() throws Exception {
		//ARRANGE
		String document = "00769203213";
			
		doThrow(ExceptionFactory.createCustomerNotFoundException()).when(this.service).generatePurchaseInvoice(document);
		
		//ACT
		mockMvc.perform(get("/customers/note").param("document", document))
		    .andExpect(status().isNotFound())
		    .andReturn();

		//ASSERT
		verify(this.service).generatePurchaseInvoice(document);
	}
	
	@Test
	void clearDebt_ShouldReturnVoidAndHttpStatus200_WhenDocumentIsValid() throws Exception {
		//ARRANGE
		String document = "00769203213";
			
		doNothing().when(this.service).clearDebt(document);
		
		//ACT
		mockMvc.perform(patch("/customers/purchase-history/document/{document}",document))
		    .andExpect(status().isOk())
		    .andReturn();

		//ASSERT
		verify(this.service).clearDebt(document);
	}
		
	@Test
	void clearDebt_ShouldReturnVoidAndHttpStatus404_WhenDocumentIsNotFound() throws Exception {
		//ARRANGE
		String document = "00769203213";
			
		doThrow(ExceptionFactory.createCustomerNotFoundException()).when(this.service).clearDebt(document);
		
		//ACT
		mockMvc.perform(patch("/customers/purchase-history/document/{document}",document))
		    .andExpect(status().isNotFound())
		    .andReturn();

		//ASSERT
		verify(this.service).clearDebt(document);
	}
	
	@Test
	void getMetrics_ShouldReturnVoidAndHttpStatus200_WhenSucess() throws Exception {
		//ARRANGE
		DataForMetricsDTO dataForMetricsDTO = UtilObjectsFactory.createDataForMetricsDTO();	
		BDDMockito.when(this.service.metrics()).thenReturn(dataForMetricsDTO);
		
		//ACT
		mockMvc.perform(get("/customers/metrics"))
		    .andExpect(status().isOk())
		    .andReturn();

		//ASSERT
		verify(this.service).metrics();
	}	
}
