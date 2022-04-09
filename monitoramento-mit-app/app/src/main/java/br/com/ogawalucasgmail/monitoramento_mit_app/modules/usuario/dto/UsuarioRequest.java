package br.com.ogawalucasgmail.monitoramento_mit_app.modules.usuario.dto;

import okhttp3.RequestBody;

import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.MediaType.JSON;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.JsonUtils.objectToJson;

public class UsuarioRequest {

    private String id;
    private String nome;
    private String cpf;
    private String perfil;
    private String usuario;
    private String senha;

    public UsuarioRequest() {
    }

    public UsuarioRequest(String id, String nome, String cpf, String perfil, String usuario,
                          String senha) {
        this.id = id;
        this.nome = nome;
        this.perfil = perfil;
        this.cpf = cpf;
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public RequestBody toRequestBody() {
        return RequestBody.create(JSON, objectToJson(this));
    }
}
