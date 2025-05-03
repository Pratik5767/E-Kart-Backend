package com.project.backend.service.cart;

import java.math.BigDecimal;

import com.project.backend.model.Cart;

public interface ICartService {
	
	Cart getCart(Long id);
	
	void clearCart(Long id);
	
	BigDecimal getTotalPrice(Long id);

	Long intializeNewCart();

}