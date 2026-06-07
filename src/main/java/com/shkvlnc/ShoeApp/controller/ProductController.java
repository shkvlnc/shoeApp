package com.shkvlnc.ShoeApp.controller;

import com.shkvlnc.ShoeApp.dto.ProductRequestDTO;
import com.shkvlnc.ShoeApp.dto.ProductResponseDTO;
import com.shkvlnc.ShoeApp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/search")
    public List<ProductResponseDTO> search(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    @PostMapping
    public ProductResponseDTO addProduct(@Valid @RequestBody ProductRequestDTO dto) {
        return productService.saveProduct(dto);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO updateProduct(@PathVariable Long id,
                                            @Valid @RequestBody ProductRequestDTO dto) {
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
