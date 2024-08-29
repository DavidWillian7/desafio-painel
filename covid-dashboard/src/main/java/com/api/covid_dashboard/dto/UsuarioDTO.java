package com.api.covid_dashboard.dto;

import com.api.covid_dashboard.model.Usuario;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioDTO {
    private String nome;
    private LocalDate dataNascimento;
    private String email;
    private String senha;

    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setDataNascimento(this.dataNascimento);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        return usuario;
    }
}
