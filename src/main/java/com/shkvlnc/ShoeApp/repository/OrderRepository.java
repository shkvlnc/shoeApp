package com.shkvlnc.ShoeApp.repository;

import com.shkvlnc.ShoeApp.entity.Order;
import com.shkvlnc.ShoeApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
