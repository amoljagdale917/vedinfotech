package com.demo.vedinfotech.service.serviceimpl;

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

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductEntity createProduct(ProductEntity product, Activity activity) {
        logger.info("Activity: {}", activity);
        return productRepository.save(product);
    }

    @Override
    public Optional<ProductEntity> getProduct(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public ProductEntity updateProduct(Long id, ProductEntity product, Activity activity) {
        Optional<ProductEntity> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            logger.info("Activity: {}", activity);
            product.setId(id);
            return productRepository.save(product);
        } else {
            logger.info("Activity: CREATE (ID not found, creating new product)");
            return productRepository.save(product);
        }
    }

    @Override
    public void deleteProduct(Long id, Activity activity) {
        Optional<ProductEntity> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            logger.info("Activity: {}", activity);
            productRepository.deleteById(id);
        } else {
            logger.warn("Activity: DELETE (ID not found, nothing to delete)");
            throw new ResourceNotFoundException("Product not found with id: " + id, "FAILED", HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "Product with the specified ID does not exist");
        }
    }
}
