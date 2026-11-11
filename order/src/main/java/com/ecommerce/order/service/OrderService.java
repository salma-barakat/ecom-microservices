package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderDTO;
import com.ecommerce.order.mapper.OrdersMapper;
import com.ecommerce.order.model.*;
import com.ecommerce.order.repo.OrderRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepo orderRepo;
    private final CartService cartService;
    private static final Logger log =
            LoggerFactory.getLogger(OrderService.class);

    @Transactional
    public OrderDTO createOrder(UUID userId) {
        log.info("createOrder for userId = {}", userId);
//        Users user = cartService.getUser(userId);
        List<CartItem> cartItems = cartService.getCartItems(userId);

        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Orders order = new Orders();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(OrderStatus.CONFIRMED);

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null, item.getProductId(), item.getPrice(), item.getQuantity(), order)
                ).toList();
        order.setOrderItems(orderItems);

        Orders savedOrder = orderRepo.saveAndFlush(order);
        log.info("savedOrder = {}", savedOrder);
        cartService.clearCart(userId);
        OrderDTO orderDTO = OrdersMapper.toOrderDTO(savedOrder);
        log.info("orderDTO = {}", orderDTO);
        return orderDTO;


    }
}
