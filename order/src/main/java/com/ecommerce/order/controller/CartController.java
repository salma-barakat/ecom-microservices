package com.ecommerce.order.controller;

import com.ecommerce.order.dto.AddToCartRequest;
import com.ecommerce.order.dto.CartItemDTO;
import com.ecommerce.order.mapper.CartItemMapper;
import com.ecommerce.order.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartItemDTO> addItemToCart(
            @RequestHeader("X-User-ID") UUID userId,
            @Valid @RequestBody AddToCartRequest cartItemRequest
    ){
        CartItemDTO cartItem = cartService.addItem(userId, cartItemRequest);
        return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
    }

    @DeleteMapping("items/{productId}")
    public ResponseEntity<?> removeItemFromCart(
            @RequestHeader("X-User-ID") UUID userId,
            @PathVariable("productId") UUID productId
    ){
        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCartItems(
            @RequestHeader("X-User-ID") UUID userId
    ){
        List<CartItemDTO> cartItems =
                cartService.getCartItems(userId).stream()
                .map(CartItemMapper::toDTO)
                .toList();
        return ResponseEntity.ok(cartItems);
    }
}
