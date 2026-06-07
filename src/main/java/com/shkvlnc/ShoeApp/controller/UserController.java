package com.shkvlnc.ShoeApp.controller;

import com.shkvlnc.ShoeApp.entity.User;
import com.shkvlnc.ShoeApp.entity.Role;
import com.shkvlnc.ShoeApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        // Default role to USER if not provided
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        if (!user.getPassword().startsWith("{noop}")) {
            user.setPassword("{noop}" + user.getPassword());
        }
        User saved = userService.registerUser(user);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{username}")
    public Optional<User> getUser(@PathVariable String username) {
        return userService.findByUsername(username);
    }
}
