package br.com.ogawalucasgmail.monitoramento_mit_app.modules.usuario.dto;

import br.com.ogawalucasgmail.monitoramento_mit_app.modules.usuario.enums.EPerfilUsuario;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.usuario.enums.ESituacaoUsuario;

public class UsuarioResponse {

    private String id;
    private String nome;
    private String cpf;
    private String usuario;
    private EPerfilUsuario perfil;
    private ESituacaoUsuario situacao;
    private String data_cadastro;

    public UsuarioResponse() {
    }

    public UsuarioResponse(String id, String nome, String cpf, String usuario, EPerfilUsuario perfil, ESituacaoUsuario situacao, String data_cadastro) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.usuario = usuario;
        this.perfil = perfil;
        this.situacao = situacao;
        this.data_cadastro = data_cadastro;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public EPerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(EPerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public ESituacaoUsuario getSituacao() {
        return situacao;
    }

    public void setSituacao(ESituacaoUsuario situacao) {
        this.situacao = situacao;
    }

    public String getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(String data_cadastro) {
        this.data_cadastro = data_cadastro;
    }

    public String toString() {
        String format = "%s\t-\t%s\n" +
                        "%s";

        return String.format(format, nome, situacao,
                            cpf);
    }

    public String obterTextoAlterarSituacao() {
        return ESituacaoUsuario.ATIVO.equals(situacao)
                ? "inativar"
                : "ativar";
    }
}
