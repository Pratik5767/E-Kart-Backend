package com.project.backend.service.cart;

import java.math.BigDecimal;

import com.project.backend.model.Cart;
import com.project.backend.model.User;

public interface ICartService {
	
	Cart getCart(Long id);
	
	void clearCart(Long id);
	
	BigDecimal getTotalPrice(Long id);

	Cart getCartByUserId(Long userId);

	Cart intializeNewCart(User user);

}