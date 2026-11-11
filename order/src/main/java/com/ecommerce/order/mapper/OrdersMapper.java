package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.OrderDTO;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.Orders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrdersMapper {

    private static final Logger log =
            LoggerFactory.getLogger(OrdersMapper.class);

    public static OrderDTO toOrderDTO(Orders order) {
        return new OrderDTO(
                order.getId(),
                order.getTotalPrice(),
                order.getOrderStatus(),
                order.getOrderItems().stream()
                        .map(OrdersMapper::toOrderItemDTO)
                        .toList(),
                order.getCreatedAt()
        );
    }

    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getProductId()
        );
    }
}
