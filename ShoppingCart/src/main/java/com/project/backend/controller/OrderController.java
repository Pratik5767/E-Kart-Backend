package com.project.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.dto.OrderDto;
import com.project.backend.exception.ResourceNotFoundException;
import com.project.backend.model.Order;
import com.project.backend.response.ApiResponse;
import com.project.backend.service.order.IOrderService;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

	private final IOrderService orderService;
	
	@PostMapping("/order")
	public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
		try {
			Order order = orderService.placeOrder(userId);
			OrderDto orderDto = orderService.convertToDto(order);
			return ResponseEntity.ok(new ApiResponse("Order placed successfully", orderDto));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error!", e.getMessage()));
		}
	}
	
	@GetMapping("/order/{orderId}/order")
	public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
		try {
			OrderDto order = orderService.getOrder(orderId);
			return ResponseEntity.ok(new ApiResponse("Success!", order));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error!", e.getMessage()));
		}
	}
	
	@GetMapping("/order/{userId}/order")
	public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
		try {
			List<OrderDto> orders = orderService.getUserOrders(userId);
			return ResponseEntity.ok(new ApiResponse("Success!", orders));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error!", e.getMessage()));
		}
	}
}