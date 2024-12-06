package com.tienda.tienda.controller;

import com.tienda.tienda.model.Product;
import com.tienda.tienda.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setNombre("Producto de prueba");
        product.setPrecio(100.0);
        product.setDescripcion("Descripción de prueba");
        product.setImagen("imagen.png");
    }

    @Test
    void testAddProduct() {
        Product newProduct = new Product();
        newProduct.setNombre("Producto Nuevo");
        newProduct.setPrecio(100.0);
        newProduct.setDescripcion("Descripción del producto");
        newProduct.setImagen("nuevo_producto.png");

        when(productService.saveProduct(any(Product.class))).thenReturn(newProduct);

        ResponseEntity<String> response = productController.addProduct(newProduct);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Producto añadido correctamente: Producto Nuevo", response.getBody());
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setNombre("Producto Encontrado");
        product.setPrecio(50.0);
        product.setDescripcion("Descripción de producto");
        product.setImagen("producto_encontrado.png");

        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        ResponseEntity<Map<String, Object>> response = productController.getProductById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Producto encontrado: Producto Encontrado", response.getBody().get("message"));
        assertNotNull(response.getBody().get("product"));
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setNombre("Producto 1");
        product1.setPrecio(50.0);

        Product product2 = new Product();
        product2.setNombre("Producto 2");
        product2.setPrecio(70.0);

        List<Product> products = List.of(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<Map<String, Object>> response = productController.getAllProducts();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se han encontrado 2 productos.", response.getBody().get("message"));
        assertTrue(((List<Product>) response.getBody().get("products")).size() > 0);
    }

    @Test
    void testUpdateProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setNombre("Producto Actualizado");
        updatedProduct.setPrecio(120.0);

        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setNombre("Producto Existente");
        existingProduct.setPrecio(100.0);

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(Optional.of(updatedProduct));

        ResponseEntity<String> response = productController.updateProduct(1L, updatedProduct);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Producto actualizado correctamente: Producto Actualizado", response.getBody());
    }
    
    @SuppressWarnings("null")
    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setNombre("Producto a Eliminar");

        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        ResponseEntity<String> response = productController.deleteProduct(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Producto eliminado correctamente: Producto a Eliminar", response.getBody());
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Object>> response = productController.getProductById(1L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Producto no encontrado", response.getBody().get("message"));
    }

    @Test
    void testGetAllProductsEmpty() {
        when(productService.getAllProducts()).thenReturn(new ArrayList<>());  // Lista vacía

        ResponseEntity<Map<String, Object>> response = productController.getAllProducts();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se han encontrado 0 productos.", response.getBody().get("message"));
    }

    @Test
    void testUpdateProductNotFound() {
        Product updatedProduct = new Product();
        updatedProduct.setNombre("Nuevo Producto");
        updatedProduct.setPrecio(100.0);

        when(productService.updateProduct(999L, updatedProduct)).thenReturn(Optional.empty()); // Producto no encontrado

        ResponseEntity<String> response = productController.updateProduct(999L, updatedProduct);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Producto no encontrado para actualización", response.getBody());
    }

    @Test
    void testDeleteProductNotFound() {
        when(productService.getProductById(999L)).thenReturn(Optional.empty()); // Producto no encontrado

        ResponseEntity<String> response = productController.deleteProduct(999L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Producto no encontrado para eliminar", response.getBody());
    }

    @Test
    void testDeleteProductSuccess() {
        Product product = new Product();
        product.setId(1L);
        product.setNombre("Producto A");

        when(productService.getProductById(1L)).thenReturn(Optional.of(product)); // Producto encontrado

        ResponseEntity<String> response = productController.deleteProduct(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Producto eliminado correctamente: Producto A", response.getBody());
    }

}
