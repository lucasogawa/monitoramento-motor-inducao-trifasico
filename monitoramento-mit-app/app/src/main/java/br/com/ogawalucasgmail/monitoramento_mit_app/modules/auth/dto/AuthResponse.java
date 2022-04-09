package br.com.ogawalucasgmail.monitoramento_mit_app.modules.auth.dto;

import br.com.ogawalucasgmail.monitoramento_mit_app.modules.usuario.dto.UsuarioResponse;

public class AuthResponse {

    private UsuarioResponse usuario;
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(UsuarioResponse usuario, String token) {
        this.usuario = usuario;
        this.token = token;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResponse usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
