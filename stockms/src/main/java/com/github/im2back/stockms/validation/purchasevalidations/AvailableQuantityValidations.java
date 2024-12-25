package com.github.im2back.stockms.validation.purchasevalidations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.im2back.stockms.model.dto.inputdata.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItem;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.validation.exceptions.PurchaseValidationException;

@Component

public class AvailableQuantityValidations implements PurchaseValidations {


	@Override
	public void valid(ProductsPurchaseRequestDto requestDto,List<Product> produtosEncontrados) {

		List<String> errorMessages = new ArrayList<>();
		List<PurchasedItem> purchasedItem = requestDto.purchasedItems();
		List<String> productsCodeList = new ArrayList<>();
		requestDto.purchasedItems().forEach(t -> productsCodeList.add(t.code()));

		// Verifica se os produtos comprados existem (por código)
		for (Product p : produtosEncontrados) {
			productsCodeList.remove(p.getCode());
		}
		productsCodeList.forEach(t -> errorMessages.add("Product Not found for code:" + t));

		
		
		// Verifica se os produtos tem quantidade disponivel em estoque
		for (Product p : produtosEncontrados) {

			purchasedItem.forEach(t -> {
				if (t.code().equals(p.getCode())) {

					if (p.getQuantity() < t.quantity()) {
						errorMessages
								.add("Quantidade do produto " + p.getName() + " não disponível. Quantidade desejada: "
										+ t.quantity() + ", Quantidade em estoque: " + p.getQuantity());
					}

				}
			});

		}

		if (!errorMessages.isEmpty()) {
			throw new PurchaseValidationException(errorMessages);
		}
	}

}
