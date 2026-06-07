package com.shkvlnc.ShoeApp.config;

import com.shkvlnc.ShoeApp.entity.Product;
import com.shkvlnc.ShoeApp.entity.User;
import com.shkvlnc.ShoeApp.entity.Role;   // ✅ import the enum
import com.shkvlnc.ShoeApp.repository.ProductRepository;
import com.shkvlnc.ShoeApp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .password("{noop}admin123") // ✅ matches NoOpPasswordEncoder
                        .role(Role.ADMIN)           // ✅ use enum instead of string
                        .email("admin@example.com")
                        .build();
                userRepository.save(admin);
                System.out.println("Seeded default admin account: admin/admin123");
            }
        };
    }

    @Bean
    CommandLineRunner seedProducts(ProductRepository productRepository) {
        return args -> {
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

                System.out.println("✅ Seeded products: Sneaker A, Sneaker B");
            }
        };
    }
}
