package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Data
public class OrderItemDTO {
    private UUID id;
    private int quantity;
    private BigDecimal price;
    private UUID productId;
}
