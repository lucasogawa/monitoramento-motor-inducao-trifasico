package br.com.ogawalucasgmail.monitoramento_mit_app.modules.grafico.dto;

public class GraficoResponse {

    private String motor;
    private String arquivo;

    public GraficoResponse() {
    }

    public GraficoResponse(String motor, String arquivo) {
        this.motor = motor;
        this.arquivo = arquivo;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }
}
