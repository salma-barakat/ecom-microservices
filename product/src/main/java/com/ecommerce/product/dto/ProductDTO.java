package com.ecommerce.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProductDTO {
    private UUID id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Brand is required")
    private String brand;

    private String category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Price must be greater than zero")
    private BigDecimal price;

    private LocalDate releaseDate;
    private Boolean available;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;

    private String imageUrl;

    @UpdateTimestamp
    private Instant imageUpdatedAt;
}
