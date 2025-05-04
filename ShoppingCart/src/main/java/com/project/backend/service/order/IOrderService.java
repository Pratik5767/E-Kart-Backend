package com.project.backend.service.order;

import com.project.backend.model.Order;

public interface IOrderService {
	
	Order placeOrder(Long userId);
	
	Order getOrder(Long orderId);

}