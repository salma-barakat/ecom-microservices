package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.CartItemDTO;
import com.ecommerce.order.model.CartItem;

public class CartItemMapper {
    public static CartItemDTO toDTO(CartItem cartItem) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setPrice(cartItem.getPrice());
        return cartItemDTO;
    }
}
