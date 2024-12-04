package com.tienda.tienda.service;

import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Método para guardar un producto
    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Método para obtener un producto por su id
    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Método para obtener todos los productos
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Método para eliminar un producto
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Método para actualizar un producto
    @Override
    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setNombre(updatedProduct.getNombre());
            product.setPrecio(updatedProduct.getPrecio());
            product.setDescripcion(updatedProduct.getDescripcion());
            product.setImagen(updatedProduct.getImagen());
            return Optional.of(productRepository.save(product));
        }
        return Optional.empty();
    }
}