package br.com.ogawalucasgmail.monitoramento_mit_app.modules.motor.dto;

public class MotorResponse {

    private String id;
    private String descricao;
    private String caminho_arquivo;
    private String data_cadastro;

    public MotorResponse() {
    }

    public MotorResponse(String id, String descricao, String caminho_arquivo, String data_cadastro) {
        this.id = id;
        this.descricao = descricao;
        this.caminho_arquivo = caminho_arquivo;
        this.data_cadastro = data_cadastro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminho_arquivo() {
        return caminho_arquivo;
    }

    public void setCaminho_arquivo(String caminho_arquivo) {
        this.caminho_arquivo = caminho_arquivo;
    }

    public String getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(String data_cadastro) {
        this.data_cadastro = data_cadastro;
    }
}
