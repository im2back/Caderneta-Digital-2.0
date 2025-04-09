package com.github.im2back.stockms.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.im2back.stockms.model.dto.inputdata.NewProductDTO;
import com.github.im2back.stockms.model.dto.inputdata.ProductMassiveReplenishmentDTO;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItemDTO;
import com.github.im2back.stockms.model.dto.inputdata.UndoPurchaseDTO;
import com.github.im2back.stockms.model.dto.outputdata.ProductDTO;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.repositories.ProductRepository;
import com.github.im2back.stockms.service.exceptions.ProductNotFoundException;
import com.github.im2back.stockms.utils.ExceptionFactory;
import com.github.im2back.stockms.utils.ProductFactoryTest;

import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	
	@Mock
	private ProductRepository repository;
	
	@InjectMocks
	private ProductService productService;
	
	@Captor
	private ArgumentCaptor<List<String>> listStringCaptor;
	
	@Captor
	private  ArgumentCaptor<Collection<Product>> listProductCaptor;
	
	@Captor
	private  ArgumentCaptor<Product> productCaptor;
	
	@Test
	void findProductById_ShoudReturnProduc_WhenIdIsFound() {
		//ARRANGE
		Long id = 1l;
		Optional<Product> productOptional = Optional.ofNullable(ProductFactoryTest.createProductValidCode001Quantity10());
		BDDMockito.when(this.repository.findById(id)).thenReturn(productOptional);
		
		//ACT
		var response = this.productService.findProductById(id);
		
		//ASSERT
		verify(this.repository).findById(1l);
		Assertions.assertEquals(id, response.id());
	}
	
	@Test
	void findProductById_ShoudThrowProductNotFoundException_WhenIdIsNotFound() {
		//ARRANGE
		Long id = 1l;
		doThrow(ExceptionFactory.createProductNotFoundException()).when(this.repository).findById(id);
		
		//ACT
		Assertions.assertThrows(ProductNotFoundException.class, () -> this.productService.findProductById(id));
		
		//ASSERT
		verify(this.repository).findById(1l);
	}
	
	@Test
	void findProductByCode_ShoudReturnProduc_WhenIdIsFound() {
		//ARRANGE
		String code = "001";
		Optional<Product> productOptional = Optional.ofNullable(ProductFactoryTest.createProductValidCode001Quantity10());
		BDDMockito.when(this.repository.findByCode(code)).thenReturn(productOptional);
		
		//ACT
		var response = this.productService.findProductByCode(code);
		
		//ASSERT
		verify(this.repository).findByCode("001");
		Assertions.assertEquals(code, response.code());
	}
	
	@Test
	void findProductByCode_ShoudThrowProductNotFoundException_WhenIdIsNotFound() {
		//ARRANGE
		String code = "070";
		doThrow(ExceptionFactory.createProductNotFoundException()).when(this.repository).findByCode(code);
		
		//ACT
		Assertions.assertThrows(ProductNotFoundException.class, () -> this.productService.findProductByCode(code));
		
		//ASSERT
		verify(this.repository).findByCode("070");
	}
	
	@Test
	void saveNewProduct_ShouldReturnProductSaved_WhenRequestIsValid() {
		//ARRANGE
		NewProductDTO newProductToSaveDTO = ProductFactoryTest.createNewProductToSaveDTOValidCode001Quantity10();
		Product productValidExcludeID = ProductFactoryTest.createValidProductWithoutId(); 
		Product product = ProductFactoryTest.createProductValidCode001Quantity10();
		BDDMockito.when(this.repository.save(productValidExcludeID)).thenReturn(product);
		
		//ACT
		 var response = this.productService.saveNewProduct(newProductToSaveDTO);
		
		//ASSERT
		verify(this.repository).save(productValidExcludeID);
		Assertions.assertEquals(newProductToSaveDTO.code(), response.code());
		Assertions.assertEquals(newProductToSaveDTO.quantity(), response.quantity());
	}
	
	@Test
	void saveNewProduct_ShouldThrowConstraintViolationException_WhenCodeAlreadyExists() {
		//ARRANGE
		NewProductDTO newProductToSaveDTO = ProductFactoryTest.createNewProductToSaveDTOValidCode001Quantity10();
		Product productValidExcludeID = ProductFactoryTest.createValidProductWithoutId(); 
		
		doThrow(ExceptionFactory.createConstraintViolationException()).when(this.repository).save(productValidExcludeID);
		
		//ACT
		Assertions.assertThrows(ConstraintViolationException.class, () -> this.productService.saveNewProduct(newProductToSaveDTO));
	
		//ASSERT
		verify(this.repository).save(productValidExcludeID);
	}
	
	@Test
	void deleProductById_ShouldReturnVoid_WhenIdIsValid() {
		//ARRANGE
		Long id = 1l;
		
		//ACT
		this.productService.deleProductById(id);
		
		//ASSERT
		verify(this.repository).deleteById(1l);
	}
	
	@Test
	void deleProductById_ShouldThrowException_WhenIdIsNotFound() {
		//ARRANGE

		
		//ACT

		
		//ASSERT
		
	}
	
	@Test
	void updateQuantityProductsAfterPurchase_ShouldThrowException_WhenIdNotFound() {
		//ARRANGE
		List<PurchasedItemDTO> purchasedItemDTOs = ProductFactoryTest.createListPurchasedItemDTO();
		List<Product> products = ProductFactoryTest.createProductListValid();
		BDDMockito.when(this.repository.findByCodes(anyList())).thenReturn(products);
		
		
		List<Product> productsSaved = ProductFactoryTest.simulateSavedProductList();
		BDDMockito.when(this.repository.saveAll(any())).thenReturn(productsSaved);
		
		//ACT
		var response = this.productService.updateQuantityProductsAfterPurchase(purchasedItemDTOs);
		
		//ASSERT
		verify(this.repository).findByCodes(this.listStringCaptor.capture());
		List<String> codesListCaptured = this.listStringCaptor.getValue();
		Assertions.assertTrue(codesListCaptured.containsAll(List.of("001","003")), "Checks if the retrieved products are the same as the received ones");
		
		verify(this.repository).saveAll(this.listProductCaptor.capture());
		Collection<Product> capturedCollection = this.listProductCaptor.getValue();
		List<Product> productsCaptured = new ArrayList<>(capturedCollection);
		productsCaptured.forEach(t -> {
			purchasedItemDTOs.forEach(p -> {
				Assertions.assertTrue(p.code().equals("003") || p.code().equals("001"), "Checks if the saved products match the received ones");
				Assertions.assertEquals(5, t.getQuantity(), "Checks if the product was saved with the updated quantity");
			});
		});
		
		response.forEach(t -> {
			purchasedItemDTOs.forEach(p -> {
				Assertions.assertTrue(p.code().equals("003") || p.code().equals("001"),"Checks if the returned data contains all the purchased products");
				Assertions.assertEquals(5,t.quantity(),"Checks if the returned product has the updated quantity"
					);
			});
		});
	}
	
	@Test
	void undoIndividualPurchase_ShouldReturnVoid_WhenSucess() {
		//ARRANGE
		UndoPurchaseDTO dto = new UndoPurchaseDTO(10);
		String code = "001";
		Optional<Product> product = Optional.ofNullable(ProductFactoryTest.createProductValidCode001Quantity10());
		BDDMockito.when(this.repository.findByCode(code)).thenReturn(product);
		
		//ACT
		this.productService.undoIndividualPurchase(dto,code);
		
		//ASSERT
		verify(this.repository).findByCode("001");
		
		verify(this.repository).save(this.productCaptor.capture());
		Product productCaptured = this.productCaptor.getValue();
		Assertions.assertEquals(20, productCaptured.getQuantity());
	}
	
	@Test
	void undoIndividualPurchase_ShoulThrowProductNotFoundException_WhenProductCodeNotFound() {
		//ARRANGE
		UndoPurchaseDTO dto = new UndoPurchaseDTO(10);
		String code = "005";
		
		doThrow(ExceptionFactory.createProductNotFoundException()).when(this.repository).findByCode(code);
		
		//ACT
		Assertions.assertThrows(ProductNotFoundException.class, () -> this.productService.undoIndividualPurchase(dto,code));
		
		//ASSERT
		verify(this.repository).findByCode("005");
	}	
	
	@Test
	void updateProduct_ShoulReturnVoid_WhenProductDTOIsValid() {
		//ARRANGE
		ProductDTO productDTO = ProductFactoryTest.createProductDTOValidCode001Quantity10ForUpdate();
		Long id= 1l;
		
		Optional<Product> product =  Optional.ofNullable(ProductFactoryTest.createProductValidCode001Quantity10());
		BDDMockito.when(this.repository.findById(productDTO.id())).thenReturn(product);
		
		//ACT
		this.productService.updateProduct(productDTO,id);
		
		//ASSERT
		verify(this.repository).findById(id);
		
		verify(this.repository).save(this.productCaptor.capture());
		Product productCaptured = this.productCaptor.getValue();
		Assertions.assertEquals("Nome Atualizado", productCaptured.getName(),"Verifica o produto salvo teve seu nome atualizado");
		Assertions.assertEquals(100, productCaptured.getQuantity(),"Verifica o produto salvo teve a quantidade atualizada");
		Assertions.assertEquals(id, productCaptured.getId(),"Verifica o produto salvo corresponde ao recebido");	
	}
	
	@Test
	void updateProduct_ShoulThrowProductNotFoundException_WhenIdNotFound() {
		//ARRANGE
		Long id= 1l;
		ProductDTO productDTO = ProductFactoryTest.createProductDTOValidCode001Quantity10ForUpdate();
		doThrow(ExceptionFactory.createProductNotFoundException()).when(this.repository).findById(productDTO.id());	
		//ACT
		Assertions.assertThrows(ProductNotFoundException.class, () -> this.productService.updateProduct(productDTO,id));
		
		
		//ASSERT
		verify(this.repository).findById(id);
	}
	
	@Test
	void massiveReplenishment_ShoulReturnListMassiveReplenishmentResponseDTO_WhenIdNotFound() {
		//ARRANGE
		List<ProductMassiveReplenishmentDTO> list = new ArrayList<>();
		list.add(new ProductMassiveReplenishmentDTO(ProductFactoryTest.createProductValidCode001Quantity10(),3));
		list.add(new ProductMassiveReplenishmentDTO(ProductFactoryTest.createProductValidCode003Quantity10(),3));
		
		BDDMockito.when(this.repository.findByCodes(anyList())).thenReturn(ProductFactoryTest.createProductListValid());
		
		//ACT
		this.productService.massiveReplenishment(list);
		
		
		//ASSERT
		verify(this.repository).findByCodes(this.listStringCaptor.capture());
		List<String> codes = this.listStringCaptor.getValue();
		Assertions.assertTrue(codes.containsAll(List.of("001", "003")), "Checks if the fetched products are compatible with the ones received");
	
		verify(this.repository).saveAll(this.listProductCaptor.capture());		
		List<Product> productList = new ArrayList<>(this.listProductCaptor.getValue());
		productList.forEach(t -> {
			Assertions.assertTrue(t.getCode().equals("001") || t.getCode().equals("003"), "Checks if the saved products are the same as the ones provided");
			Assertions.assertEquals(13, t.getQuantity(), "Checks if the quantity was restored");
		});
	}
	
}
