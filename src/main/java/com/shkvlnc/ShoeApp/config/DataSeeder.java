package com.shkvlnc.ShoeApp.config;

import com.shkvlnc.ShoeApp.entity.*;
import com.shkvlnc.ShoeApp.repository.OrderRepository;
import com.shkvlnc.ShoeApp.repository.ProductRepository;
import com.shkvlnc.ShoeApp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedAll(UserRepository userRepository,
                              ProductRepository productRepository,
                              OrderRepository orderRepository) {
        return args -> {
            // ✅ Seed Admin
            User admin = userRepository.findByUsername("admin").orElseGet(() -> {
                User newAdmin = User.builder()
                        .username("admin")
                        .password("{noop}admin123")
                        .role(Role.ADMIN)
                        .email("admin@example.com")
                        .build();
                return userRepository.save(newAdmin);
            });
            System.out.println("✅ Admin account ready: " + admin.getUsername());

            // ✅ Seed Normal User
            User user = userRepository.findByUsername("testuser").orElseGet(() -> {
                User newUser = User.builder()
                        .username("testuser")
                        .password("{noop}user123")
                        .role(Role.USER)
                        .email("testuser@example.com")
                        .build();
                return userRepository.save(newUser);
            });
            System.out.println("✅ User account ready: " + user.getUsername());

            // ✅ Seed Products
            if (productRepository.count() == 0) {
                Product p1 = Product.builder()
                        .name("Sneaker A")
                        .price(99.99)
                        .stock(50)
                        .build();

                Product p2 = Product.builder()
                        .name("Sneaker B")
                        .price(149.99)
                        .stock(30)
                        .build();

                productRepository.save(p1);
                productRepository.save(p2);
                System.out.println("✅ Products seeded: Sneaker A, Sneaker B");
            }

            // ✅ Seed Order
            if (orderRepository.count() == 0) {
                Product product = productRepository.findById(1L)
                        .orElseThrow(() -> new IllegalStateException("Product not found"));

                Order order = Order.builder()
                        .userId(user.getId())
                        .productId(product.getId())
                        .quantity(2)
                        .status(OrderStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .build();

                orderRepository.save(order);
                System.out.println("✅ Order seeded for user " + user.getUsername() +
                        " with product " + product.getName());
            }
        };
    }
}
