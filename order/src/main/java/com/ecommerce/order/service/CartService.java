package com.ecommerce.order.service;

import com.ecommerce.order.dto.AddToCartRequest;
import com.ecommerce.order.dto.CartItemDTO;
//import com.ecommerce.order.exception.InsufficientQuantityException;
//import com.ecommerce.order.exception.ProductNotFoundException;
//import com.ecommerce.order.exception.UserNotFoundException;
import com.ecommerce.order.mapper.CartItemMapper;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repo.CartItemRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepo cartItemRepo;
//    private final ProductRepo productRepo;
//    private final UserRepo userRepo;
    private static final Logger log =
            LoggerFactory.getLogger(CartService.class);

    public CartItemDTO addItem(UUID userId, AddToCartRequest cartItemRequest) {
        UUID productId = cartItemRequest.getProductId();
        log.info("Adding item: Get product by id {}", productId);
//        Product product = getProduct(productId);
//        if (product.getQuantity() < cartItemRequest.getQuantity()){
//            log.info("Product quantity is {} while required is {}", product.getQuantity(), cartItemRequest.getQuantity());
//            throw new InsufficientQuantityException("Product quantity less than requested quantity");
//        }
//
//        log.info("Adding item: Get user by id {}", userId);
//        Users user = getUser(userId);
//
        log.info("Adding item: checking if item already exists");
        Optional<CartItem> existingItem = cartItemRepo.findByUserIdAndProductId(userId, productId);
        CartItem cartItem;

        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            log.info("Adding item: item already exists with id {}", cartItem.getId());
            int updatedQuantity = cartItem.getQuantity() + cartItemRequest.getQuantity();
            BigDecimal updatedPrice = BigDecimal.valueOf(1000.00);
//            BigDecimal updatedPrice = product.getPrice().multiply(BigDecimal.valueOf(updatedQuantity));
            cartItem.setQuantity(updatedQuantity);
            cartItem.setPrice(updatedPrice);
            log.info("Adding item: saving updated quantity: {}, price: {}", updatedQuantity, updatedPrice);

        } else{
            cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            int itemQuantity = cartItemRequest.getQuantity();
            BigDecimal itemPrice = BigDecimal.valueOf(1000.00);
//            BigDecimal itemPrice = product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity()));
            cartItem.setQuantity(itemQuantity);
            cartItem.setPrice(itemPrice);
            log.info("Adding item: saving new item with quantity: {} and price: {}", itemQuantity, itemPrice);
        }

        try{
            return CartItemMapper.toDTO(cartItemRepo.save(cartItem));

        } catch(Exception e){
            log.error("Error in saving item in DB: {}", e.toString());
            throw new RuntimeException("Error in saving item in DB");
        }

    }

    @Transactional
    public void removeItemFromCart(UUID userId, UUID productId) {
//        Product product = getProduct(productId);
//        Users user = getUser(userId);
        Optional<CartItem> cartItem = cartItemRepo.findByUserIdAndProductId(userId, productId);
        if (cartItem.isPresent()) {
            log.info("Removing item from CartItem with id {}", cartItem.get().getId());
            cartItemRepo.delete(cartItem.get());
        }
        log.info("CartItem doesn't exist for user with id {}", userId);
    }

    public List<CartItem> getCartItems(UUID userId) {
//        Users user = getUser(userId);
        return cartItemRepo.findByUserId(userId);

    }

//    public Product getProduct(long productId) {
//        return productRepo.findById(productId)
//                .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + productId));
//    }
//
//    public Users getUser(String userId) {
//        return userRepo.findById(Long.parseLong(userId))
//                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
//    }

    public void clearCart(UUID userId) {
//        Users user = getUser(userId);
        cartItemRepo.deleteByUserId(userId);
    }

}
