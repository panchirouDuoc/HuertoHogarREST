package com.example.HuertoHogar.service;

import com.example.HuertoHogar.model.Usuario;
import com.example.HuertoHogar.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    public Usuario registerUser(Usuario usuario) {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        if ("admin".equalsIgnoreCase(usuario.getUsername())) {
            usuario.setRol("ROLE_ADMIN");
        } else if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("ROLE_USER");
        }

        return usuarioRepository.save(usuario);
    }

    public String loginUser(String username, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado después de la autenticación"));
        
        return jwtService.generateToken(usuario);
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

    public Usuario updateUserProfile(Long id, Usuario profileDetails) {
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        existingUsuario.setNombre(profileDetails.getNombre());
        existingUsuario.setApellido(profileDetails.getApellido());
        existingUsuario.setEmail(profileDetails.getEmail());
        existingUsuario.setDireccion(profileDetails.getDireccion());
        existingUsuario.setTelefono(profileDetails.getTelefono());
        if (profileDetails.getFoto() != null) {
            existingUsuario.setFoto(profileDetails.getFoto());
        }

        return usuarioRepository.save(existingUsuario);
    }
}
