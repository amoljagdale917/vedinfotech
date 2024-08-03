package com.demo.vedinfotech.controller;

import com.demo.vedinfotech.dto.ApiResponse;
import com.demo.vedinfotech.dto.ProductRequest;
import com.demo.vedinfotech.entity.Activity;
import com.demo.vedinfotech.entity.ProductEntity;
import com.demo.vedinfotech.exception.ErrorDetails;
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
    public ResponseEntity<ApiResponse<?>> handleProductRequest(@RequestBody ProductRequest request) {
        try {
            ApiResponse<?> response;
            Activity activity = request.getActivity();
            if (activity == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse<>(
                                "failed",
                                HttpStatus.BAD_REQUEST.value(),
                                "Invalid activity type",
                                HttpStatus.BAD_REQUEST,
                                null
                        )
                );
            }

            switch (activity) {
                case CREATE:
                    response = productService.createProduct(request.getProduct(), request.getActivity());
                    return new ResponseEntity<>(response, response.getHttpStatus());
                case UPDATE:
                    Long id = request.getProduct().getId();
                    response = productService.updateProduct(id, request.getProduct(), request.getActivity());
                    return new ResponseEntity<>(response, response.getHttpStatus());
                case DELETE:
                    ProductEntity product = request.getProduct();
                    if (product != null && product.getId() != null) {
                        response = productService.deleteProduct(product.getId(), request.getActivity());
                        return new ResponseEntity<>(response, response.getHttpStatus());
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                new ApiResponse<>(
                                        "failed",
                                        HttpStatus.BAD_REQUEST.value(),
                                        "Product id must be provided for delete operation",
                                        HttpStatus.BAD_REQUEST,
                                        null
                                )
                        );
                    }
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ApiResponse<>(
                                    "failed",
                                    HttpStatus.BAD_REQUEST.value(),
                                    "Invalid activity type",
                                    HttpStatus.BAD_REQUEST,
                                    null
                            )
                    );
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(
                    new ApiResponse<>(
                            e.getStatus(),
                            e.getStatusCode(),
                            e.getMessage(),
                            e.getHttpStatus(),
                            null
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(
                            "failed",
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "An unexpected error occurred",
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            null
                    )
            );
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<ProductEntity>>> getProduct(@PathVariable Long id) {
        try {
            ApiResponse<Optional<ProductEntity>> response = productService.getProduct(id);
            return new ResponseEntity<>(response, response.getHttpStatus());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(
                    new ApiResponse<>(
                            e.getStatus(),
                            e.getStatusCode(),
                            e.getMessage(),
                            e.getHttpStatus(),
                            Optional.empty()  // Ensure this is Optional.empty()
                    )
            );
        }
    }

}
