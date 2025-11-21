package com.example.HuertoHogar.service;

import com.example.HuertoHogar.model.Usuario;
import com.example.HuertoHogar.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public Usuario registerUser(Usuario usuario) {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        // Codificar la contraseña antes de guardarla
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        // Asignar rol por defecto
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("user");
        }
        return usuarioRepository.save(usuario);
    }

    public String loginUser(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (passwordEncoder.matches(password, usuario.getPassword())) {
            // Si la contraseña es correcta, genera un token
            return jwtService.generateToken(usuario);
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }

    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario updateUsuario(Long id, Usuario usuarioDetails) {
        Usuario existingUsuario = usuarioRepository.findById(id).orElse(null);
        if (existingUsuario != null) {
            existingUsuario.setNombre(usuarioDetails.getNombre());
            existingUsuario.setEmail(usuarioDetails.getEmail());

     
            return usuarioRepository.save(existingUsuario);
        }
        return null;
    }

    public void deleteUsuario(Long id) {
        Usuario existingUsuario = usuarioRepository.findById(id).orElse(null);
        if (existingUsuario != null) {
            usuarioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }
}
