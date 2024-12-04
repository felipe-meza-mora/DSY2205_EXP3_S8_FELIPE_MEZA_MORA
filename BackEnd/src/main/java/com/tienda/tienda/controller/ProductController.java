package com.tienda.tienda.controller;

import com.tienda.tienda.model.Product;
import com.tienda.tienda.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    @Autowired
    private ProductService productService;

    // Endpoint para agregar un producto
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok("Producto añadido correctamente: " + savedProduct.getNombre());
    }

    // Endpoint para obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        Map<String, Object> response = new HashMap<>();

        if (product.isPresent()) {
            response.put("message", "Producto encontrado: " + product.get().getNombre());
            response.put("product", product.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Producto no encontrado");
            return ResponseEntity.status(404).body(response);
        }
    }

    // Endpoint para obtener todos los productos
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Se han encontrado " + products.size() + " productos.");
        response.put("products", products);

        return ResponseEntity.ok(response);
    }

    // Endpoint para actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updatedProduct) {
        Optional<Product> product = productService.updateProduct(id, updatedProduct);
        return product.map(p -> ResponseEntity.ok("Producto actualizado correctamente: " + p.getNombre()))
                      .orElseGet(() -> ResponseEntity.status(404).body("Producto no encontrado para actualización"));
    }

    // Endpoint para eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Producto eliminado correctamente: " + product.get().getNombre());
        } else {
            return ResponseEntity.status(404).body("Producto no encontrado para eliminar");
        }
    }
}