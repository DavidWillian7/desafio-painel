package com.api.covid_dashboard.controller;

import com.api.covid_dashboard.dto.LoginRequestDTO;
import com.api.covid_dashboard.dto.UsuarioDTO;
import com.api.covid_dashboard.dto.EmailDTO;
import com.api.covid_dashboard.model.Usuario;
import com.api.covid_dashboard.security.JwtUtil;
import com.api.covid_dashboard.service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Autenticar usuário", description = "Autentica um usuário e retorna um token JWT")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = jwtUtil.generateToken((Usuario) auth.getPrincipal());
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    @Operation(summary = "Criar usuário", description = "Cria um usuário e retorna um token JWT")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = usuarioDTO.toUsuario();
            usuarioService.criarUsuario(usuario);
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken(authentication);

            return ResponseEntity.ok(jwt);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Adicionar admin", description = "Adiciona a permissão de admin para o email do usuário fornecido. Requer permissão de ADMIN.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/atualizar-role")
    public ResponseEntity<?> atualizarRoleParaAdmin(@RequestBody EmailDTO emailDTO, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String role = jwtUtil.extractRole(token);

            if (role == null || !role.equals("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado. Permissão de ADMIN necessária.");
            }

            try {
                if (emailDTO.getEmail() == null || emailDTO.getEmail().isEmpty()) {
                    return ResponseEntity.badRequest().body("Email não fornecido");
                }

                usuarioService.atualizarRoleParaAdmin(emailDTO.getEmail());
                return ResponseEntity.ok("Role do usuário atualizada para ADMIN");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar role do usuário");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token não fornecido");
        }
    }
}