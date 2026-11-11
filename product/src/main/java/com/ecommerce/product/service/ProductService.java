package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductDTO;
import com.ecommerce.product.exception.ImageProcessingException;
import com.ecommerce.product.exception.ProductNotFoundException;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repo.ProductRepo;
import com.ecommerce.product.util.PropertyCopyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    ProductRepo repo;

    private static final Logger log =
            LoggerFactory.getLogger(ProductService.class);

    public List<Product> getProducts(){
        log.info("Fetching all products");
        return repo.findAll();
    }

    public Product getProductById(UUID productId) {
        log.info("Fetching product with id {}", productId);
        return repo.findById(productId).orElseThrow(
                ()-> {
                    log.warn("Product with id {} not found", productId);
                    return new ProductNotFoundException("Product not found with id " + productId);
                });
    }

    public Product addProduct(Product product, MultipartFile imageFile) {
        log.info("Adding new product with name: {}", product.getName());
        log.info("product data is: {}", product);
        try{
            product.setImageName(imageFile.getOriginalFilename());
            product.setImageType(imageFile.getContentType());
            product.setImageData(imageFile.getBytes());
            product.setImageUpdatedAt(Instant.now());
            return repo.save(product);
        } catch (IOException e) {
            log.error("Failed to read and save image for product with name: {}", product.getName(), e);
            throw new ImageProcessingException(
                    "Failed to read and save image file, getting exception: "+e);
        }
    }

    public Product updateProductFields(UUID productId, ProductDTO productDTO) {
        log.info("Inside updateProduct: fetching product with id {}", productId);
        Product existingProd = getProductById(productId);
        if(productDTO != null){
            log.info("Inside updateProduct: updating product with id {}", productId);
            PropertyCopyUtils.copyNonNullProperties(productDTO, existingProd);
        }
        return repo.save(existingProd);
    }

    public Product updateProductImg(UUID productId, MultipartFile imageFile) {
        log.info("Inside updateProductImage: fetching product with id {}", productId);
        Product existingProd = getProductById(productId);
        try{
            if(imageFile != null && !imageFile.isEmpty()){
                existingProd.setImageName(imageFile.getOriginalFilename());
                existingProd.setImageType(imageFile.getContentType());
                existingProd.setImageData(imageFile.getBytes());
                existingProd.setImageUpdatedAt(Instant.now());
                log.info("ImageFIle is not null: setting image data");
            }
            return repo.save(existingProd);

        } catch (Exception e) {
            log.error("Failed to read and save image for product with id {}", productId, e);
            throw new ImageProcessingException(
                    "Failed to read and update image file, getting exception: "+e);
        }
    }

    public void deleteProduct(UUID productId){
        log.info("Inside deleteProduct: fetching product with id {}", productId);
        Product existingProduct = getProductById(productId);
        log.info("Deleting product with data {}", existingProduct);
        repo.deleteById(productId);
    }

    public List<ProductDTO> searchProducts(String keyword) {
        log.info("Inside searchProducts: fetching product with keyword {}", keyword);
        try {
            return repo.searchProducts(keyword).stream()
                    .map(ProductMapper::toDTO).toList();
        } catch (Exception e) {
            log.error("Failed to search product with keyword: {}", keyword, e);
            throw new RuntimeException(e);
        }
    }
}
