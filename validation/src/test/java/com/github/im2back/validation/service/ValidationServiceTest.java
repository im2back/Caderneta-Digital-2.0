package com.github.im2back.validation.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.im2back.validation.dto.ProductsPurchaseRequestDto;
import com.github.im2back.validation.dto.PurchasedItem;
import com.github.im2back.validation.entities.customer.Address;
import com.github.im2back.validation.entities.customer.Customer;
import com.github.im2back.validation.entities.product.Product;
import com.github.im2back.validation.repository.customer.CustomerRepository;
import com.github.im2back.validation.repository.stock.ProductRepository;
import com.github.im2back.validation.util.ProductFactoryTest;
import com.github.im2back.validation.validations.customer.PurchaseValidationsCustomer;
import com.github.im2back.validation.validations.exceptions.PurchaseValidationException;
import com.github.im2back.validation.validations.stock.PurchaseValidationsStock;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PurchaseValidationsStock stockValid;

    @Mock
    private PurchaseValidationsCustomer customerValid;

    @Spy
    private List<PurchaseValidationsStock> stockPurchaseValidations = new ArrayList<>();

    @Spy
    private List<PurchaseValidationsCustomer> customerPurchaseValidations = new ArrayList<>();

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        // Injeta manualmente no construtor
        validationService = new ValidationService(
            customerRepository,
            productRepository,
            stockPurchaseValidations,
            customerPurchaseValidations
        );
    }

    @Test
    @DisplayName("Should call validations without throwing exception")
    void validPurchase_shouldReturnVoid_whenNoValidationIsThrown() {
        // Arrange
        Address address = new Address("nome da rua", "06", "complemento");
        Customer customer = new Customer("Jefferson Souza", "00769203213", "jeff@gmail.com", "989144501", true, address);
        List<Product> products = ProductFactoryTest.createProductListValid();
        List<PurchasedItem> items = List.of(new PurchasedItem("001", 2));

        ProductsPurchaseRequestDto dto = new ProductsPurchaseRequestDto("00769203213", items);

        BDDMockito.when(customerRepository.findByDocument(dto.document())).thenReturn(Optional.of(customer));
        BDDMockito.when(productRepository.findByCodes(anyList())).thenReturn(products);

        // Adiciona os mocks corretos nas listas corretas
        stockPurchaseValidations.add(stockValid);
        customerPurchaseValidations.add(customerValid);

        // Act + Assert
        assertDoesNotThrow(() -> validationService.validPurchase(dto));

        // Verifica se os métodos `valid` foram realmente chamados
        verify(stockValid).valid(dto, products);
        verify(customerValid).valid(customer);
    }
    
    @Test
    @DisplayName("should throw PurchaseValidationException when a stock business rule is violated")
    void validPurchase_shouldThrowPurchaseValidationException_whenStockValidationFails() {
        // ARRANGE
        List<Product> products = ProductFactoryTest.createProductListValid();
        List<PurchasedItem> items = List.of(new PurchasedItem("001", 2));

        ProductsPurchaseRequestDto dto = new ProductsPurchaseRequestDto("00769203213", items);

        BDDMockito.when(productRepository.findByCodes(anyList())).thenReturn(products);
        
        this.stockPurchaseValidations.add(this.stockValid);
        this.customerPurchaseValidations.add(this.customerValid);
        
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add("teste");
        
        doThrow(new PurchaseValidationException(errorMessages)).when(this.stockValid).valid(dto, products);

        // ACT + ASSERT
        Assertions.assertThrows(PurchaseValidationException.class, () -> this.validationService.validPurchase(dto) );
     
        verify(this.stockValid).valid(dto, products);
    }
    
    @Test
    @DisplayName("should throw PurchaseValidationException when a customer business rule is violated")
    void validPurchase_shouldThrowPurchaseValidationException_whenCustomerValidationFails() {
        // ARRANGE
        Address address = new Address("nome da rua", "06", "complemento");
        Customer customer = new Customer("Jefferson Souza", "00769203213", "jeff@gmail.com", "989144501", true, address);
    	
        List<Product> products = ProductFactoryTest.createProductListValid();
        List<PurchasedItem> items = List.of(new PurchasedItem("001", 2));

        ProductsPurchaseRequestDto dto = new ProductsPurchaseRequestDto("00769203213", items);

        BDDMockito.when(productRepository.findByCodes(anyList())).thenReturn(products);
        BDDMockito.when(customerRepository.findByDocument(dto.document())).thenReturn(Optional.of(customer));
        
        this.stockPurchaseValidations.add(this.stockValid);
        this.customerPurchaseValidations.add(this.customerValid);
        
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add("teste");
        
        doThrow(new PurchaseValidationException(errorMessages)).when(this.customerValid).valid(customer);

        // ACT + ASSERT
        Assertions.assertThrows(PurchaseValidationException.class, () -> this.validationService.validPurchase(dto) );
     
        verify(this.stockValid).valid(dto, products);
        verify(this.customerValid).valid(customer);
    }
}
