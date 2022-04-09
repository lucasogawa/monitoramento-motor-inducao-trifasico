package br.com.ogawalucasgmail.monitoramento_mit_app.modules.parametro.dto;

import br.com.ogawalucasgmail.monitoramento_mit_app.modules.grandeza.dto.GrandezaResponse;

public class ParametroResponse {

    private String id;
    private String motor;
    private String torque;
    private String velocidade;
    private GrandezaResponse rms;
    private GrandezaResponse dht;
    private int opcaoRms;
    private int opcaoDht;

    public ParametroResponse() {
    }

    public ParametroResponse(String id, String motor, String torque, String velocidade,
                             GrandezaResponse rms, GrandezaResponse dht,
                             int opcaoRms, int opcaoDht) {
        this.id = id;
        this.motor = motor;
        this.torque = torque;
        this.velocidade = velocidade;
        this.rms = rms;
        this.dht = dht;
        this.opcaoRms = opcaoRms;
        this.opcaoDht = opcaoDht;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getTorque() {
        return torque;
    }

    public void setTorque(String torque) {
        this.torque = torque;
    }

    public String getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(String velocidade) {
        this.velocidade = velocidade;
    }

    public GrandezaResponse getRms() {
        return rms;
    }

    public void setRms(GrandezaResponse rms) {
        this.rms = rms;
    }

    public GrandezaResponse getDht() {
        return dht;
    }

    public void setDht(GrandezaResponse dht) {
        this.dht = dht;
    }

    public int getOpcaoRms() {
        return opcaoRms;
    }

    public void setOpcaoRms(int opcaoRms) {
        this.opcaoRms = opcaoRms;
    }

    public int getOpcaoDht() {
        return opcaoDht;
    }

    public void setOpcaoDht(int opcaoDht) {
        this.opcaoDht = opcaoDht;
    }

    @Override
    public String toString(){
        return toStringDefault()
            + toStringRms()
            + toStringDht();
    }

    private String toStringDefault() {
        String formatDefault = "\n%s\n" +
            "Velocidade: %s \t-\t Torque: %s\n";

        return String.format(formatDefault,
            motor,
            velocidade, torque);
    }

    private String toStringRms() {
        String formatRms = "\nRMS\n" +
            "Vab: %s \t-\t Vbc: %s \t-\t Vca: %s\n" +
            "Ia: %s \t-\t Ib: %s \t-\t Ic: %s\n";

        return opcaoRms == 1
            ? String.format(formatRms,
                rms.getVab(), rms.getVbc(), rms.getVca(),
                rms.getIa(), rms.getIb(), rms.getIc())
            : "";
    }

    private String toStringDht() {
        String formatDht = "\nDHT\n" +
            "Vab: %s \t-\t Vbc: %s \t-\t Vca: %s\n" +
            "Ia: %s \t-\t Ib: %s \t-\t Ic: %s\n";

        return opcaoDht == 1
            ? String.format(formatDht,
                dht.getVab(), dht.getVbc(), dht.getVca(),
                dht.getIa(), dht.getIb(), dht.getIc())
            : "";
    }
}
