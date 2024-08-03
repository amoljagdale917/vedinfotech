package com.demo.vedinfotech.service.serviceimpl;

import com.demo.vedinfotech.dto.ApiResponse;
import com.demo.vedinfotech.entity.Activity;
import com.demo.vedinfotech.entity.ProductEntity;
import com.demo.vedinfotech.exception.ResourceNotFoundException;
import com.demo.vedinfotech.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductEntity product;

    @BeforeEach
    public void setUp() {
        product = new ProductEntity();
        product.setId(1L);
        product.setName("Test Product");
    }

    @Test
    public void testCreateProductSuccess() {
        when(productRepository.save(any(ProductEntity.class))).thenReturn(product);

        ApiResponse<ProductEntity> response = productService.createProduct(product, Activity.CREATE);

        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertNotNull(response.getData());
        assertEquals(product.getName(), response.getData().getName());
    }

    @Test
    public void testGetProductSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ApiResponse<Optional<ProductEntity>> response = productService.getProduct(1L);

        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertTrue(response.getData().isPresent());
        assertEquals(product.getName(), response.getData().get().getName());
    }

    @Test
    public void testGetProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProduct(1L);
        });

        assertEquals("Product not found with id: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testUpdateProductSuccess() {
        // Arrange: Mock the behavior of the repository
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(product);

        // Act: Call the updateProduct method
        ApiResponse<ProductEntity> response = productService.updateProduct(1L, product, Activity.UPDATE);

        // Assert: Verify that the response indicates success
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertEquals(product.getName(), response.getData().getName());

        // Verify: Ensure the repository save method was called
        verify(productRepository, times(1)).save(product);
    }


    @Test
    public void testUpdateProductNotFound_CreatesNewProduct() {
        // Arrange: Mock the behavior of the repository
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: Call the updateProduct method, which should create a new product
        ApiResponse<ProductEntity> response = productService.updateProduct(1L, product, Activity.UPDATE);

        // Assert: Verify that the response indicates creation
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertEquals(product.getName(), response.getData().getName());

        // Verify: Ensure the repository save method was called
        verify(productRepository, times(1)).save(product);
    }


    @Test
    public void testDeleteProductSuccess() {
        // Arrange: Mock the behavior of the repository
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(1L);

        // Act: Call the deleteProduct method
        ApiResponse<Void> response = productService.deleteProduct(1L, Activity.DELETE);

        // Assert: Verify that the response indicates success
        assertEquals(HttpStatus.NO_CONTENT, response.getHttpStatus());
        assertEquals("success", response.getStatus());
        assertNull(response.getData());

        // Verify: Ensure the repository deleteById method was called
        verify(productRepository, times(1)).deleteById(1L);
    }


    @Test
    public void testDeleteProductNotFound() {
        // Arrange: Mock the behavior of the repository to return empty
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Call the deleteProduct method and expect an exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(1L, Activity.DELETE);
        });

        // Assert: Verify the exception details
        assertEquals("Product not found with id: 1", exception.getMessage());
        assertEquals("FAILED", exception.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());

        // Verify: Ensure the repository deleteById method was not called
        verify(productRepository, times(0)).deleteById(anyLong());
    }

}
