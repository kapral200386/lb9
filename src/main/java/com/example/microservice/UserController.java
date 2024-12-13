package com.example.microservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(users);
    }


    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Name is required");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid email format");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password is required");
        }


        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email is already in use");
        }


        user = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully with ID: " + user.getId());
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody User user
    ) {

        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with ID: " + id);
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Name is required");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid email format");
        }


        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        User updatedUser = userRepository.save(existingUser);

        return ResponseEntity.ok("User updated successfully with ID: " + updatedUser.getId());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with ID: " + id);
        }


        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully with ID: " + id);
    }
}
