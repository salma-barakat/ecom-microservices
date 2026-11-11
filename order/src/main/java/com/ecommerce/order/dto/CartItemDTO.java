package com.ecommerce.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CartItemDTO {
    private UUID id;
    private int quantity;
    private BigDecimal price;
}
