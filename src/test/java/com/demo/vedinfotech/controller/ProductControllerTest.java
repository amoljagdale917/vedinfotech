package com.demo.vedinfotech.controller;

import com.demo.vedinfotech.dto.ApiResponse;
import com.demo.vedinfotech.dto.ProductRequest;
import com.demo.vedinfotech.entity.Activity;
import com.demo.vedinfotech.entity.ProductEntity;
import com.demo.vedinfotech.exception.ResourceNotFoundException;
import com.demo.vedinfotech.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductEntity product;

    @BeforeEach
    public void setUp() {
        product = new ProductEntity();
        product.setId(1L);
        product.setName("Test Product");
    }

    @Test
    public void testHandleCreateProductRequest() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setActivity(Activity.CREATE);
        request.setProduct(product);

        ApiResponse<ProductEntity> response = new ApiResponse<>(
                "success",
                HttpStatus.CREATED.value(),
                "Product created successfully",
                HttpStatus.CREATED,
                product
        );

        when(productService.createProduct(any(ProductEntity.class), any(Activity.class))).thenReturn(response);

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = productController.handleProductRequest(request);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("Product created successfully", responseEntity.getBody().getDescription());
        assertNotNull(responseEntity.getBody().getData());
        assertEquals(product.getName(), ((ProductEntity) responseEntity.getBody().getData()).getName());

        verify(productService, times(1)).createProduct(any(ProductEntity.class), any(Activity.class));
    }

    @Test
    public void testHandleUpdateProductRequest() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setActivity(Activity.UPDATE);
        request.setProduct(product);

        ApiResponse<ProductEntity> response = new ApiResponse<>(
                "success",
                HttpStatus.OK.value(),
                "Product updated successfully",
                HttpStatus.OK,
                product
        );

        when(productService.updateProduct(anyLong(), any(ProductEntity.class), any(Activity.class))).thenReturn(response);

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = productController.handleProductRequest(request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("Product updated successfully", responseEntity.getBody().getDescription());
        assertNotNull(responseEntity.getBody().getData());
        assertEquals(product.getName(), ((ProductEntity) responseEntity.getBody().getData()).getName());

        verify(productService, times(1)).updateProduct(anyLong(), any(ProductEntity.class), any(Activity.class));
    }

    @Test
    public void testHandleUpdateProductRequestNotFound_CreatesNewProduct() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setActivity(Activity.UPDATE);
        request.setProduct(product);

        ApiResponse<ProductEntity> response = new ApiResponse<>(
                "success",
                HttpStatus.CREATED.value(),
                "Product created successfully",
                HttpStatus.CREATED,
                product
        );

        when(productService.updateProduct(anyLong(), any(ProductEntity.class), any(Activity.class))).thenReturn(response);

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = productController.handleProductRequest(request);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("Product created successfully", responseEntity.getBody().getDescription());
        assertNotNull(responseEntity.getBody().getData());
        assertEquals(product.getName(), ((ProductEntity) responseEntity.getBody().getData()).getName());

        verify(productService, times(1)).updateProduct(anyLong(), any(ProductEntity.class), any(Activity.class));
    }

    @Test
    public void testHandleDeleteProductRequest() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setActivity(Activity.DELETE);
        request.setProduct(product);

        ApiResponse<Void> response = new ApiResponse<>(
                "success",
                HttpStatus.NO_CONTENT.value(),
                "Product deleted successfully",
                HttpStatus.NO_CONTENT,
                null
        );

        when(productService.deleteProduct(anyLong(), any(Activity.class))).thenReturn(response);

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = productController.handleProductRequest(request);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("Product deleted successfully", responseEntity.getBody().getDescription());
        assertNull(responseEntity.getBody().getData());

        verify(productService, times(1)).deleteProduct(anyLong(), any(Activity.class));
    }

    @Test
    public void testHandleDeleteProductRequestNoId() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setActivity(Activity.DELETE);
        request.setProduct(new ProductEntity()); // Product without an ID

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = productController.handleProductRequest(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("failed", responseEntity.getBody().getStatus());
        assertEquals("Product id must be provided for delete operation", responseEntity.getBody().getDescription());

        verify(productService, never()).deleteProduct(anyLong(), any(Activity.class));
    }

    @Test
    public void testHandleInvalidActivity() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setActivity(null); // Invalid activity
        request.setProduct(new ProductEntity());

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = productController.handleProductRequest(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("failed", responseEntity.getBody().getStatus());
        assertEquals("Invalid activity type", responseEntity.getBody().getDescription());

        verify(productService, never()).createProduct(any(ProductEntity.class), any(Activity.class));
        verify(productService, never()).updateProduct(anyLong(), any(ProductEntity.class), any(Activity.class));
        verify(productService, never()).deleteProduct(anyLong(), any(Activity.class));
    }

    @Test
    public void testHandleResourceNotFoundException() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setActivity(Activity.DELETE);
        ProductEntity product = new ProductEntity();
        product.setId(999L); // Non-existent ID
        request.setProduct(product);

        // Mock service to throw ResourceNotFoundException
        when(productService.deleteProduct(anyLong(), any(Activity.class)))
                .thenThrow(new ResourceNotFoundException(
                        "Product not found with id: 999",
                        "FAILED",
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND,
                        "Product with the specified ID does not exist"
                ));

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = productController.handleProductRequest(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("FAILED", responseEntity.getBody().getStatus());
        assertEquals("Product not found with id: 999", responseEntity.getBody().getDescription());

        verify(productService, times(1)).deleteProduct(anyLong(), any(Activity.class));
    }

    @Test
    public void testHandleGlobalException() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setActivity(Activity.CREATE);
        request.setProduct(new ProductEntity());

        // Mock service to throw a generic Exception
        when(productService.createProduct(any(ProductEntity.class), any(Activity.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = productController.handleProductRequest(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("failed", responseEntity.getBody().getStatus());
        assertEquals("An unexpected error occurred", responseEntity.getBody().getDescription());

        verify(productService, times(1)).createProduct(any(ProductEntity.class), any(Activity.class));
    }

    @Test
    public void testGetProductSuccess() {
        // Arrange
        Long productId = 1L;
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        product.setName("Test Product");

        ApiResponse<Optional<ProductEntity>> response = new ApiResponse<>(
                "success",
                HttpStatus.OK.value(),
                "Product retrieved successfully",
                HttpStatus.OK,
                Optional.of(product)
        );

        when(productService.getProduct(productId)).thenReturn(response);

        // Act
        ResponseEntity<ApiResponse<Optional<ProductEntity>>> responseEntity = productController.getProduct(productId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertTrue(responseEntity.getBody().getData().isPresent());
        assertEquals(product.getName(), responseEntity.getBody().getData().get().getName());

        verify(productService, times(1)).getProduct(productId);
    }

    @Test
    public void testGetProductNotFound() {
        // Arrange
        Long productId = 999L;

        // Mock the service to throw ResourceNotFoundException
        when(productService.getProduct(productId)).thenThrow(
                new ResourceNotFoundException(
                        "Product not found with id: " + productId,
                        "FAILED",
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND,
                        "Product with the specified ID does not exist"
                )
        );

        // Act
        ResponseEntity<ApiResponse<Optional<ProductEntity>>> responseEntity = productController.getProduct(productId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("FAILED", responseEntity.getBody().getStatus());
        assertEquals("Product not found with id: " + productId, responseEntity.getBody().getDescription());
        assertFalse(responseEntity.getBody().getData().isPresent());

        verify(productService, times(1)).getProduct(productId);
    }

    @Test
    public void testHandleUnknownActivityType() {
        // Arrange
        ProductRequest request = new ProductRequest();
        ProductEntity product = new ProductEntity();
        request.setProduct(product);

        // We simulate an invalid enum value by using a casting trick or mocking
        Activity invalidActivity = Activity.valueOf("UNKNOWN"); // Simulating invalid activity type

        request.setActivity(invalidActivity);

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = productController.handleProductRequest(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("failed", responseEntity.getBody().getStatus());
        assertEquals("Invalid activity type", responseEntity.getBody().getDescription());
        assertNull(responseEntity.getBody().getData());

        verify(productService, never()).createProduct(any(ProductEntity.class), any(Activity.class));
        verify(productService, never()).updateProduct(anyLong(), any(ProductEntity.class), any(Activity.class));
        verify(productService, never()).deleteProduct(anyLong(), any(Activity.class));
    }





}
