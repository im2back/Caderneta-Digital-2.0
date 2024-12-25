package com.github.im2back.stockms.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.github.im2back.stockms.infra.clients.ClientResourceCustomer;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseResponseDto;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.repositories.ProductRepository;
import com.github.im2back.stockms.service.util.Util;
import com.github.im2back.stockms.validation.purchasevalidations.PurchaseValidations;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	
	@Mock
	private ProductRepository repository;

	@Mock
	private ClientResourceCustomer clientResourceCustomer;
	
	@Spy
	private List<PurchaseValidations> purchaseValidations = new ArrayList<>();
	
	@Mock
	private PurchaseValidations valid1;
	
	@Mock
	private PurchaseValidations valid2;
	
	@InjectMocks
	private ProductService service;
	
	@Captor
	private ArgumentCaptor<Product> productCaptor;

	@Test
	@DisplayName("Deveria acessar o banco de dados e retornar um Product apartir do id recebido no parametro")
	void findProductById() {
		//ARRANGE
		Long id = 1l;
		BDDMockito.when(repository.findById(id)).thenReturn(Util.productOptional);
		
		//ACT
		var response = service.findProductById(id);
		
		//ASSERT
		verify(repository,times(1)).findById(id);
		Assertions.assertEquals(1l, response.getId(),"O ID do produto retornado deve ser igual ao id do parametro");	
	}
	
	
	@Test
	@DisplayName("Deveria acessar o banco de dados e retornar um Product apartir do codigo recebido no parametro")
	void findByCode() {
		//ARRANGE
		String code = "001";
		BDDMockito.when(repository.findByCode(code)).thenReturn(Util.productOptional);
		
		//ACT
		var response = service.findByCode(code);
		
		//ASSERT
		verify(repository,times(1)).findByCode(code);
		Assertions.assertEquals("001", response.getCode(),"O produto retornado deve ter o codigo igual ao codigo do parametro");		
	}
	
	
	
	@Test
	@DisplayName("Deveria salvar um novo produto no banco de dados apartir de um dto")
	void saveNewProduct() {
		//ARRANGE
		BDDMockito.when(repository.save(any(Product.class))).thenReturn(Util.product);
		Product productSave = new Product(Util.productRegister.name(),
				Util.productRegister.price(), Util.productRegister.code(),
				Util.productRegister.quantity(), Util.productRegister.productUrl());
		
		//ACT
		var response = service.saveNewProduct(Util.productRegister);
		
		//ASSERT
		
		verify(repository,times(1)).save(productSave);
		Assertions.assertEquals(Util.productRegister.quantity(), response.quantity(),
				"O objeto retornado,pós casdastro, deve conter os mesmos atributos do dto de entrada(quantity).");
		
		Assertions.assertEquals(Util.productRegister.code(), response.code(),
				"O objeto retornado,pós casdastro, deve conter os mesmos atributos do dto de entrada(code).");	
	}
	
	@Test
	@DisplayName("Deveria deletar um produto do banco de dados")
	void deleProductById() {
		//arrange
		Long id = 1l;
		
		//act
		service.deleProductById(id);
		
		//assert
		verify(repository,times(1)).deleteById(1l);
	}
	
	
	@Test
	@DisplayName("Recebe um DTO de compra e atualiza o estoque, subtraindo os produtos vendidos")
	void updateStock() {
		//arrange
		this.purchaseValidations.add(valid1);
		this.purchaseValidations.add(valid2);
		List<String> codesList = List.of("001");
		List<Product> productList = new ArrayList<>();
		productList.add(Util.productOptional.get());
		BDDMockito.when(repository.findByCodes(codesList)).thenReturn(productList);
		ResponseEntity<PurchaseResponseDto> responseMethod = ResponseEntity.ok(Util.PurchaseResponseDto);
		BDDMockito.when(clientResourceCustomer.purchase(Util.purchaseRegister)).thenReturn(responseMethod);
		
		//act
		var response = service.updateQuantityProductsAfterPurchase(Util.productsPurchaseRequestDto);
		
		//assert
		BDDMockito.then(valid1).should().valid(Util.productsPurchaseRequestDto,productList);
		BDDMockito.then(valid2).should().valid(Util.productsPurchaseRequestDto,productList);
		
		verify(repository,times(1)).findByCode("001");
		
		BDDMockito.then(repository).should().save(productCaptor.capture());
		Assertions.assertEquals(90, productCaptor.getValue().getQuantity(), 
				"A quantidade em estoque do produto deve ter sido atualizada, deduzindo a quantidade comprada");
		
		Assertions.assertTrue(Util.PurchaseResponseDto.customerName().equalsIgnoreCase(response.customerName()),
				"O nome de usuario contido no retorno do método deve ser igual ao retornado pela requisição do client");
		
		Assertions.assertEquals(new BigDecimal(1000) , response.total(),
				"O TOTAL contido no retorno do método deve ser igual ao retornado pela requisição do client");	
	}
	
	@Test
	@DisplayName("Recebe um DTO e atualiza o estoque, adicionando de volta ao estoque os produtos da compra desfeita")
	void undoPurchase() {
		//arrange
		BDDMockito.when(repository.findByCode("001")).thenReturn(Util.productUndoPurchaseOptional);
		
		//act
		service.undoIndividualPurchase(Util.undoPurchaseDto);
		
		//assert
		BDDMockito.then(repository).should().save(productCaptor.capture());
		
		Assertions.assertEquals(10, productCaptor.getValue().getQuantity(),
				"Deveria incrementar a quantidade do produto de acordo com o dto recebido");
	
		verify(clientResourceCustomer,times(1)).undoPurchase(Util.undoPurchaseDto);
	}
	
}
