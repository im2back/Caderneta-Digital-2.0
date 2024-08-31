package com.github.im2back.customerms.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

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

import com.github.im2back.customerms.controller.util.Util;
import com.github.im2back.customerms.model.dto.datainput.CustomerDto;
import com.github.im2back.customerms.model.dto.datainput.PurchaseRequestDto;
import com.github.im2back.customerms.model.dto.datainput.UndoPurchaseDto;
import com.github.im2back.customerms.model.dto.dataoutput.DailyTotal;
import com.github.im2back.customerms.model.dto.dataoutput.DataForMetricsDto;
import com.github.im2back.customerms.model.dto.dataoutput.GetCustomerDto;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseResponseDto;
import com.github.im2back.customerms.service.CustomerService;

@AutoConfigureMockMvc
@WebMvcTest(CustomerController.class)
@AutoConfigureJsonTesters
class CustomerControllerTest {
	
	@MockBean
	private CustomerService service;

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private JacksonTester<GetCustomerDto> getCustomerDtoJackson;
	
	@Autowired
	private JacksonTester<CustomerDto> customerDtoJackson;
	
	@Autowired
	private JacksonTester<PurchaseRequestDto> purchaseRequestDtoJackson;
	
	@Autowired
	private JacksonTester<PurchaseResponseDto> purchaseResponseDtoJackson;
	
	@Autowired
	private JacksonTester<UndoPurchaseDto> undoPurchaseDtoJackson;
	
	@Autowired
	private JacksonTester<DataForMetricsDto> dataForMetricsDtoJackson;

	
	
	@Test
	@DisplayName("Deveria retornar um o dto de Customer GetCustomerDto com base no id informado")
	void findCustomerById() throws Exception {
		
		//ARRANGE
		Long id = 1l;	
		BDDMockito.when(service.findCustomerById(id)).thenReturn(Util.getCustomerDto);
		
		//ACT
		var response = mvc.perform(get("/customer/{id}",id)).
				andExpect(status().isOk()).
				andReturn().getResponse();
		
		//ASSERT
		Assertions.assertEquals(id, getCustomerDtoJackson.parseObject(response.getContentAsString()).id()  , 
				"Deveria retonar um customer de id igual igual ao fornecido");
	}
	
	@Test
	@DisplayName("Deveria retornar um o dto de Customer GetCustomerDto com base no documento informado")
	void findCustomerByDocument() throws Exception {
		
		//ARRANGE
		String document = "123456789";	
		BDDMockito.when(service.findCustomerByDocumentOrganizedPurchase(document)).thenReturn(Util.getCustomerDto);
		
		//ACT
		var response = mvc.perform(get("/customer/findDocument").param("document", document)).
				andExpect(status().isOk()).
				andReturn().getResponse();
		
		//ASSERT
		Assertions.assertEquals(document, getCustomerDtoJackson.parseObject(response.getContentAsString()).document(), 
				"Deveria retonar um customer de id igual igual ao fornecido");
	}
	
	@Test
	@DisplayName("Deveria cadastrar um customer apartir CustomerDto de um retornar um GetCustomerDto ")
	void saveNewCustomer() throws Exception {
		
		//ARRANGE
		String jsonRequest = customerDtoJackson.write(Util.customerDto).getJson();
		BDDMockito.when(service.saveNewCustomer(Util.customerDto)).thenReturn(Util.getCustomerDto);
		
		//ACT
		var response = mvc.perform(post("/customer").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).
				andExpect(status().isCreated()).
				andReturn().getResponse();

		//ASSERT
		var parseResponse = getCustomerDtoJackson.parseObject(response.getContentAsString());
		Assertions.assertEquals("123456789", parseResponse.document(),
				"O documento do dto retornado deveria ser igual ao informado no cadastro");
		
		Assertions.assertEquals("jefferson", parseResponse.name().toLowerCase(),
				"O nome do dto retornado deveria ser igual ao informado no cadastro");
	}
		
	@Test
	@DisplayName("Deveria excluir um usuario e retornar status 201")
	void deleteCustomerById() throws Exception {
		
		//ARRANGE
		String document = "123456789";
		BDDMockito.doNothing().when(service).logicalCustomerDeletion(document);
		
		//ACT
		 mvc.perform(delete("/customer/deletecustomer").param("document", document)).
		andExpect(status().isNoContent());
	}
		
	@Test
	@DisplayName("Deveria retornar um PurchaseResponseDto")
	void test() throws Exception {
		
		//ARRANGE
		String jsonRequest = this.purchaseRequestDtoJackson.write(Util.purchaseRequestDto).getJson();
		BDDMockito.when(service.purchase(Util.purchaseRequestDto)).thenReturn(Util.purchaseResponseDto);
		//ACT
		
		var response = mvc.perform(put("/customer").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).
				andExpect(status().isOk())
				.andReturn().getResponse();
		
		PurchaseResponseDto responseParse =purchaseResponseDtoJackson.parseObject(response.getContentAsString());
		
		//ASSERT
		Assertions.assertEquals(new BigDecimal(100), responseParse.total(),
				"O atributo total da resposta deveria ser 100");
		Assertions.assertEquals("jefferson", responseParse.customerName()
				,"o nome deve ser igual a jefferson");
	}
	
	@Test
	@DisplayName("Deveria desfazer uma compra e retornar status 200ok")
	void undoPurchase() throws Exception {
		
		//ARRANGE
		var jsonRequest = this.undoPurchaseDtoJackson.write(Util.undoPurchaseDto).getJson();
		BDDMockito.doNothing().when(service).undoPurchase(Util.undoPurchaseDto);
		
		//ACT + ASSERT
		 mvc.perform(put("/customer/undopurchase").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).
				andExpect(status().isOk());
	}
	
	
	@Test
	@DisplayName("Deveria quitar um pagamento individual e retornar status 200ok")
	void individualPayment() throws Exception {
		
		//ARRANGE
		var jsonRequest = this.undoPurchaseDtoJackson.write(Util.undoPurchaseDto).getJson();
		BDDMockito.doNothing().when(service).undoPurchase(Util.undoPurchaseDto);
		
		//ACT + ASSERT
		 mvc.perform(put("/customer/payment").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).
				andExpect(status().isOk());
	}
		
	@Test
	@DisplayName("Deveria gerar uma nota e devolver status 200ok")
	void generatePurchaseNote() throws Exception {
		
		//ARRANGE
		String document = "123456789";
		BDDMockito.doNothing().when(service).generatePurchaseInvoice(document);
		
		//ACT + ASSERT
		 mvc.perform(put("/customer/note").param("document", document)).
				andExpect(status().isOk());
	}
		
	@Test
	@DisplayName("Deveria quitar todos as vendas em aberto e retornar wstatus 200ok")
	void clearDebt() throws Exception {
		
		//ARRANGE
		String document = "123456789";
		BDDMockito.doNothing().when(service).clearDebt(document);
			
		//ACT + ASSERT
		 mvc.perform(delete("/customer/cleardebt").param("document", document)).
				andExpect(status().isOk());
	}
		
	
	@Test
	@DisplayName("Deveria retornar um DataForMetricsDto contendo as métricas")
	void getMetrics() throws Exception {
		
		//ARRANGE
		BDDMockito.when(service.metrics()).thenReturn(Util.dataForMetrics);
		
		//ACT
		var response = mvc.perform(get("/customer/metrics")).
				andExpect(status().isOk())
				.andReturn().getResponse();
		
		var parseResponse = this.dataForMetricsDtoJackson.parseObject(response.getContentAsString());

		 // ASSERT
	    Assertions.assertEquals(1200.00, parseResponse.totalValueForLastMonth());
	    Assertions.assertEquals(600.00, parseResponse.partialValueForCurrentMonth());
	    Assertions.assertEquals(250.00, parseResponse.partialValueForCurrentDay());
	    Assertions.assertEquals(100.00, parseResponse.totalOutstandingAmount());

	    List<DailyTotal> expectedDailyTotals = Util.dataForMetrics.dataGraphicSevenDays();
	    List<DailyTotal> actualDailyTotals = parseResponse.dataGraphicSevenDays();
	    
	    // Verificando o tamanho da lista
	    Assertions.assertEquals(expectedDailyTotals.size(), actualDailyTotals.size());
	    
	    // Verificando cada DailyTotal
	    for (int i = 0; i < expectedDailyTotals.size(); i++) {
	        Assertions.assertEquals(expectedDailyTotals.get(i).purchaseDate(), actualDailyTotals.get(i).purchaseDate());
	        Assertions.assertEquals(expectedDailyTotals.get(i).totalValue(), actualDailyTotals.get(i).totalValue());
	    }
		
	}

	
}
