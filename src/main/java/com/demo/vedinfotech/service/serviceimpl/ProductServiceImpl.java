package com.demo.vedinfotech.service.serviceimpl;

import com.demo.vedinfotech.dto.ApiResponse;
import com.demo.vedinfotech.entity.Activity;
import com.demo.vedinfotech.entity.ProductEntity;
import com.demo.vedinfotech.exception.ResourceNotFoundException;
import com.demo.vedinfotech.repository.ProductRepository;
import com.demo.vedinfotech.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ApiResponse<ProductEntity> createProduct(ProductEntity product, Activity activity) {
        LOGGER.info("Activity: {}", activity);
        ProductEntity createdProduct = productRepository.save(product);
        return new ApiResponse<>(
                "success",
                HttpStatus.CREATED.value(),
                "Product created successfully",
                HttpStatus.CREATED,
                createdProduct
        );
    }

    @Override
    public ApiResponse<Optional<ProductEntity>> getProduct(Long id) {
        Optional<ProductEntity> product = productRepository.findById(id);
        if (product.isPresent()) {
            return new ApiResponse<>(
                    "success",
                    HttpStatus.OK.value(),
                    "Product found",
                    HttpStatus.OK,
                    product
            );
        } else {
            throw new ResourceNotFoundException(
                    "Product not found with id: " + id,
                    "FAILED",
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND,
                    "Product with the specified ID does not exist"
            );
        }
    }

    @Override
    public ApiResponse<ProductEntity> updateProduct(Long id, ProductEntity product, Activity activity) {
        Optional<ProductEntity> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            LOGGER.info("Activity: UPDATE - Updating product with ID {}", id);
            product.setId(id);
            ProductEntity updatedProduct = productRepository.save(product);
            return new ApiResponse<>(
                    "success",
                    HttpStatus.OK.value(),
                    "Product updated successfully",
                    HttpStatus.OK,
                    updatedProduct
            );
        } else {
            LOGGER.info("Activity: CREATE - Product not found, creating new product with ID {}", id);
            ProductEntity newProduct = productRepository.save(product);
            return new ApiResponse<>(
                    "success",
                    HttpStatus.CREATED.value(),
                    "Product created successfully",
                    HttpStatus.CREATED,
                    newProduct
            );
        }
    }


    @Override
    public ApiResponse<Void> deleteProduct(Long id, Activity activity) {
        Optional<ProductEntity> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            LOGGER.info("Activity: {}", activity);
            productRepository.deleteById(id);
            return new ApiResponse<>(
                    "success",
                    HttpStatus.NO_CONTENT.value(),
                    "Product deleted successfully",
                    HttpStatus.NO_CONTENT,
                    null
            );
        } else {
            throw new ResourceNotFoundException(
                    "Product not found with id: " + id,
                    "FAILED",
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND,
                    "Product with the specified ID does not exist"
            );
        }
    }
}
