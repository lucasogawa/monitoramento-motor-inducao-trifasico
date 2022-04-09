package br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants;

public class URL {

    public static String URL_BASE = "https://monitoramento-mit-api.herokuapp.com";
    public static String URL_AUTH = URL_BASE + "/auth";
    public static String URL_MONITORAMENTO = URL_BASE + "/monitoramento";
    public static String URL_IMAGENS = URL_BASE + "/imagens";
    public static String URL_USUARIO = URL_BASE + "/usuario";
    public static String URL_MOTOR = URL_BASE + "/motor";

    public static String URL_ALTERAR_SITUACAO = URL_USUARIO + "/%s/%s";
}
