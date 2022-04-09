package br.com.ogawalucasgmail.monitoramento_mit_app.modules.auth.model;

import okhttp3.RequestBody;

import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.MediaType.JSON;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.JsonUtils.objectToJson;

public class Auth {

    private String usuario;
    private String senha;

    public Auth(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
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
