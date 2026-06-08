package com.shkvlnc.ShoeApp.service;

import com.shkvlnc.ShoeApp.dto.ProductRequestDTO;
import com.shkvlnc.ShoeApp.dto.ProductResponseDTO;
import com.shkvlnc.ShoeApp.entity.Product;
import com.shkvlnc.ShoeApp.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(p -> new ProductResponseDTO(p.getId(), p.getName(), p.getPrice(), p.getStock()))
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> searchProducts(String keyword) {
        return productRepository.findByNameContaining(keyword)
                .stream()
                .map(p -> new ProductResponseDTO(p.getId(), p.getName(), p.getPrice(), p.getStock()))
                .collect(Collectors.toList());
    }

    public ProductResponseDTO saveProduct(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        Product saved = productRepository.save(product);
        return new ProductResponseDTO(saved.getId(), saved.getName(), saved.getPrice(), saved.getStock());
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        Product saved = productRepository.save(product);
        return new ProductResponseDTO(saved.getId(), saved.getName(), saved.getPrice(), saved.getStock());
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id " + id);
        }
        productRepository.deleteById(id);
    }
}
