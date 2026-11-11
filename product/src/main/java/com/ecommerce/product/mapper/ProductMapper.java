package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.ProductDTO;
import com.ecommerce.product.model.Product;

public class ProductMapper {
    public static ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setAvailable(product.getAvailable());
        dto.setReleaseDate(product.getReleaseDate());
        dto.setImageUrl("/products/" + product.getId() + "/image");
        dto.setImageUpdatedAt(product.getImageUpdatedAt());
        return dto;
    }
}
