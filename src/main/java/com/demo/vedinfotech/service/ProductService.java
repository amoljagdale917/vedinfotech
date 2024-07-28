package com.demo.vedinfotech.service;

import com.demo.vedinfotech.entity.Activity;
import com.demo.vedinfotech.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductEntity createProduct(ProductEntity product, Activity activity);

    Optional<ProductEntity> getProduct(Long id);

    ProductEntity updateProduct(Long id, ProductEntity product, Activity activity);

    void deleteProduct(Long id, Activity activity);
}
