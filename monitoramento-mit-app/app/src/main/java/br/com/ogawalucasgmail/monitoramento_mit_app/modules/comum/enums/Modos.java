package br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.enums;

public enum Modos {

    CADASTRAR(0), EDITAR(1);

    private Integer codigo;

    Modos(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }
}
