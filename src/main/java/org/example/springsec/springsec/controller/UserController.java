package org.example.springsec.springsec.controller;

import lombok.AllArgsConstructor;
import org.example.springsec.springsec.entity.User;
import org.example.springsec.springsec.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("")
    @Transactional
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @PostMapping("/save")
    public ResponseEntity<User> saveUser(User user){
        userService.saveUser(user);
        return ResponseEntity.ok(user);
    }
}
