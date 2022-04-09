package br.com.ogawalucasgmail.monitoramento_mit_app.modules.usuario.enums;

import java.util.Arrays;
import java.util.List;

public enum EPerfilUsuario {

    ADMINISTRADOR,
    USUARIO;

    public static List<String> perfilsString() {
        return Arrays.asList(ADMINISTRADOR.name(), USUARIO.name());
    }
}
