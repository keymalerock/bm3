package com.bm3.accounting.controllers;

import com.bm3.accounting.dto.UserRequestDTO;
import com.bm3.accounting.entity.User;
import com.bm3.accounting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDTO request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setKey(passwordEncoder.encode(request.getKey())); // Hashea la contraseña
        user.setEnabled(true);

        userRepository.save(user);
        return ResponseEntity.ok("Usuario creado exitosamente");
    }
}
