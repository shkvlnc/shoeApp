package com.shkvlnc.ShoeApp.service;

import com.shkvlnc.ShoeApp.entity.Product;
import com.shkvlnc.ShoeApp.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
