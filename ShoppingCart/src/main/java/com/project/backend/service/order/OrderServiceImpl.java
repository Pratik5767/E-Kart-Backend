package com.project.backend.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.enums.OrderStatus;
import com.project.backend.exception.ResourceNotFoundException;
import com.project.backend.model.Cart;
import com.project.backend.model.Order;
import com.project.backend.model.OrderItem;
import com.project.backend.model.Product;
import com.project.backend.repository.OrderRepository;
import com.project.backend.repository.ProductRepository;
import com.project.backend.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final ICartService cartService;

	@Override
	public Order placeOrder(Long userId) {
		Cart cart = cartService.getCartByUserId(userId);
		
		Order order = createOrder(cart);
		List<OrderItem> orderItems = createOrderItems(order, cart);
		order.setOrderItems(new HashSet<>(orderItems));
		order.setTotalAmount(calculateTotalAmount(orderItems));
		Order savedOrder = orderRepository.save(order);
		cartService.clearCart(cart.getId());
		return savedOrder;
	}
	
	private Order createOrder(Cart cart) {
		Order order = new Order();
		order.setUser(cart.getUser());
		order.setOrderStatus(OrderStatus.PENDING);
		order.setOrderDate(LocalDate.now());
		return order;
	}

	private List<OrderItem> createOrderItems(Order order, Cart cart) {
		return cart.getItems().stream().map(cartItem -> {
			Product product = cartItem.getProduct();
			product.setInventory(product.getInventory() - cartItem.getQuantity());
			productRepository.save(product);
			return new OrderItem(order, product, cartItem.getQuantity(), cartItem.getUnitPrice());
		}).toList();
	}

	private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
		return orderItems.stream().map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public Order getOrder(Long orderId) {
		return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
	}
	
	@Override
	public List<Order> getUserOrders(Long userId) {
		return orderRepository.findByUserId(userId);
	}

}