package com.demo.vedinfotech.controller;

import com.demo.vedinfotech.dto.ProductRequest;
import com.demo.vedinfotech.entity.Activity;
import com.demo.vedinfotech.entity.ProductEntity;
import com.demo.vedinfotech.exception.ResourceNotFoundException;
import com.demo.vedinfotech.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductEntity> handleProductRequest(@RequestBody ProductRequest request) {
        ProductEntity product;
        switch (request.getActivity()) {
            case CREATE:
                product = productService.createProduct(request.getProduct(), request.getActivity());
                return ResponseEntity.ok(product);
            case UPDATE:
                Long id = request.getProduct().getId();
                if (id != null) {
                    product = productService.updateProduct(id, request.getProduct(), request.getActivity());
                } else {
                    product = productService.createProduct(request.getProduct(), Activity.CREATE);
                }
                return ResponseEntity.ok(product);
            case DELETE:
                id = request.getProduct().getId();
                if (id != null) {
                    productService.deleteProduct(id, request.getActivity());
                    return ResponseEntity.noContent().build();
                } else {
                    throw new ResourceNotFoundException("Product id must be provided for delete operation", "FAILED", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "The request must include a valid product ID for deletion");
                }
            default:
                return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProduct(@PathVariable Long id) {
        Optional<ProductEntity> product = productService.getProduct(id);
        return product.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id, "FAILED", HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "Product with the specified ID does not exist"));
    }

}
