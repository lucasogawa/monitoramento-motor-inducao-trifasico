package br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.enums;

public enum HttpCode {

    OK(200);

    private Integer codigo;

    HttpCode(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }
}
