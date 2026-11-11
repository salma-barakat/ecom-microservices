package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductDTO;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping({""})
    public ResponseEntity<List<ProductDTO>> getProducts(){
        List<ProductDTO> productDTOS =
                productService.getProducts().stream().map(ProductMapper::toDTO).toList();
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @GetMapping("/{prodId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID prodId){
        Product product = productService.getProductById(prodId);
        ProductDTO productDTO = ProductMapper.toDTO(product);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping("/{prodId}/image")
    public ResponseEntity<byte[]> getProductImgById(
            @PathVariable UUID prodId,
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
        Product p = productService.getProductById(prodId);
        String eTag = "\"" + p.getImageUpdatedAt().toEpochMilli() + "\"";
        if (eTag.equals(ifNoneMatch)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        String imgType = p.getImageType(); // may be null
        if (imgType == null || imgType.isBlank()) {
            imgType = MediaType.IMAGE_JPEG_VALUE;
        }
        System.out.println("Image type = " + p.getImageType());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imgType))
                .eTag(eTag)
                .body(p.getImageData());
    }

    @PostMapping(
        value = "/",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> addProduct(
            @Valid @RequestPart("product") Product product,
            @RequestPart("imageFile") MultipartFile imageFile
    ){
        Product resProduct = productService.addProduct(product, imageFile);
        return new ResponseEntity<>(ProductMapper.toDTO(resProduct), HttpStatus.CREATED);
    }


//    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> addProduct(
//            @RequestPart("product") Product product,
//            HttpServletRequest request
//    ) throws Exception {
//
//        request.getParts().forEach(part ->
//                System.out.println(
//                        "Part name: " + part.getName() +
//                                ", Size: " + part.getSize()
//                )
//        );
//
//        return ResponseEntity.ok("Test");
//    }



    @PatchMapping("/{prodId}")
    public ResponseEntity<?> updateProductFields(
            @PathVariable UUID prodId,
            @Valid @RequestBody ProductDTO prodDTO
    ) {
        System.out.println("getting prodDTO: " + prodDTO);
        Product updatedProduct = productService.updateProductFields(prodId, prodDTO);
        return ResponseEntity.ok(ProductMapper.toDTO(updatedProduct));
    }

    @PutMapping(
        value = "/{prodId}/img",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> updateProduct(
            @PathVariable UUID prodId,
            @RequestParam("imageFile") MultipartFile imageFile
    ){
        Product updatedProduct = productService.updateProductImg(prodId, imageFile);
        return ResponseEntity.ok(ProductMapper.toDTO(updatedProduct));
    }

    @DeleteMapping("/{prodId}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID prodId){
        productService.deleteProduct(prodId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String keyword){
        List<ProductDTO> productDTOS = productService.searchProducts(keyword);
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }
}
