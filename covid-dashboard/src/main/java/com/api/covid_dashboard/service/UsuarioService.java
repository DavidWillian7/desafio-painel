package com.api.covid_dashboard.service;

import com.api.covid_dashboard.model.Usuario;
import com.api.covid_dashboard.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final String ADMIN_EMAIL = "admin@gmail.com";

    public Usuario criarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Email já está em uso");
        }
        
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        
        if (ADMIN_EMAIL.equals(usuario.getEmail())) {
            usuario.setRole("ADMIN");
        } else {
            usuario.setRole("USER");
        }
        
        return usuarioRepository.save(usuario);
    }

    public boolean verificarSenha(String senhaRaw, String senhaEncoded) {
        return passwordEncoder.matches(senhaRaw, senhaEncoded);
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o email: " + email));
    }

    public Usuario atualizarRoleParaAdmin(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        
        if (!usuarioOptional.isPresent()) {
            throw new RuntimeException("Usuário não encontrado com o email: " + email);
        }
        
        Usuario usuario = usuarioOptional.get();
        usuario.setRole("ADMIN");
        return usuarioRepository.save(usuario);
    }
}
