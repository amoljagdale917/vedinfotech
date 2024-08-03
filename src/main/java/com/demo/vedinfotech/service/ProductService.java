package com.demo.vedinfotech.service;

import com.demo.vedinfotech.dto.ApiResponse;
import com.demo.vedinfotech.entity.Activity;
import com.demo.vedinfotech.entity.ProductEntity;

import java.util.Optional;

public interface ProductService {
    ApiResponse<ProductEntity> createProduct(ProductEntity product, Activity activity);
    ApiResponse<Optional<ProductEntity>> getProduct(Long id);
    ApiResponse<ProductEntity> updateProduct(Long id, ProductEntity product, Activity activity);
    ApiResponse<Void> deleteProduct(Long id, Activity activity);
}
