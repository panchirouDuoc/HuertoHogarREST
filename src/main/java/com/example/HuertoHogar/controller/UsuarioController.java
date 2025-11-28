package com.example.HuertoHogar.controller;

import com.example.HuertoHogar.model.Usuario;
import com.example.HuertoHogar.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody Usuario usuario) {
        try {
            Usuario newUser = usuarioService.registerUser(usuario);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        try {
            String token = usuarioService.loginUser(username, password);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Credenciales inválidas"), HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/perfil")
    public ResponseEntity<Usuario> getCurrentUserProfile(@AuthenticationPrincipal Usuario usuario) {
    
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/perfil")
    public ResponseEntity<Usuario> updateUserProfile(
            @AuthenticationPrincipal Usuario currentUser,
            @RequestBody Usuario profileDetails) {
        Usuario updatedUsuario = usuarioService.updateUserProfile(currentUser.getId(), profileDetails);
        return ResponseEntity.ok(updatedUsuario);
    }

    @GetMapping("/auth/me")
    public ResponseEntity<Usuario> getAuthenticatedUser(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(usuario);
    }
}
