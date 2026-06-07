package com.shkvlnc.ShoeApp.service;

import com.shkvlnc.ShoeApp.entity.User;
import com.shkvlnc.ShoeApp.entity.Role;
import com.shkvlnc.ShoeApp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.USER); // ✅ enum instead of string
        }
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
