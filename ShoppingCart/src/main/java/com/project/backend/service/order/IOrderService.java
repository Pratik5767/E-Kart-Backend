package com.project.backend.service.order;

import java.util.List;

import com.project.backend.dto.OrderDto;
import com.project.backend.model.Order;

public interface IOrderService {
	
	Order placeOrder(Long userId);
	
	OrderDto getOrder(Long orderId);

	List<OrderDto> getUserOrders(Long userId);

}