package br.com.ogawalucasgmail.monitoramento_mit_app.modules.parametro.dto;

public class ParametroHistoricoResponse {

    private String id;
    private String motor_id;
    private String data_cadastro;
    private String torque;
    private String velocidade;
    private String rms_vab;
    private String rms_vbc;
    private String rms_vca;
    private String rms_ia;
    private String rms_ib;
    private String rms_ic;
    private String dht_vab;
    private String dht_vbc;
    private String dht_vca;
    private String dht_ia;
    private String dht_ib;
    private String dht_ic;
    private int opcaoRms;
    private int opcaoDht;

    public ParametroHistoricoResponse() {
    }

    public ParametroHistoricoResponse(String id, String motor_id, String data_cadastro, String torque,
                                      String velocidade, String rms_vab, String rms_vbc, String rms_vca,
                                      String rms_ia, String rms_ib, String rms_ic, String dht_vab,
                                      String dht_vbc, String dht_vca, String dht_ia, String dht_ib,
                                      String dht_ic, int opcaoRms, int opcaoDht) {
        this.id = id;
        this.motor_id = motor_id;
        this.data_cadastro = data_cadastro;
        this.torque = torque;
        this.velocidade = velocidade;
        this.rms_vab = rms_vab;
        this.rms_vbc = rms_vbc;
        this.rms_vca = rms_vca;
        this.rms_ia = rms_ia;
        this.rms_ib = rms_ib;
        this.rms_ic = rms_ic;
        this.dht_vab = dht_vab;
        this.dht_vbc = dht_vbc;
        this.dht_vca = dht_vca;
        this.dht_ia = dht_ia;
        this.dht_ib = dht_ib;
        this.dht_ic = dht_ic;
        this.opcaoRms = opcaoRms;
        this.opcaoDht = opcaoDht;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMotor_id() {
        return motor_id;
    }

    public void setMotor_id(String motor_id) {
        this.motor_id = motor_id;
    }

    public String getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(String data_cadastro) {
        this.data_cadastro = data_cadastro;
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

    public String getRms_vab() {
        return rms_vab;
    }

    public void setRms_vab(String rms_vab) {
        this.rms_vab = rms_vab;
    }

    public String getRms_vbc() {
        return rms_vbc;
    }

    public void setRms_vbc(String rms_vbc) {
        this.rms_vbc = rms_vbc;
    }

    public String getRms_vca() {
        return rms_vca;
    }

    public void setRms_vca(String rms_vca) {
        this.rms_vca = rms_vca;
    }

    public String getRms_ia() {
        return rms_ia;
    }

    public void setRms_ia(String rms_ia) {
        this.rms_ia = rms_ia;
    }

    public String getRms_ib() {
        return rms_ib;
    }

    public void setRms_ib(String rms_ib) {
        this.rms_ib = rms_ib;
    }

    public String getRms_ic() {
        return rms_ic;
    }

    public void setRms_ic(String rms_ic) {
        this.rms_ic = rms_ic;
    }

    public String getDht_vab() {
        return dht_vab;
    }

    public void setDht_vab(String dht_vab) {
        this.dht_vab = dht_vab;
    }

    public String getDht_vbc() {
        return dht_vbc;
    }

    public void setDht_vbc(String dht_vbc) {
        this.dht_vbc = dht_vbc;
    }

    public String getDht_vca() {
        return dht_vca;
    }

    public void setDht_vca(String dht_vca) {
        this.dht_vca = dht_vca;
    }

    public String getDht_ia() {
        return dht_ia;
    }

    public void setDht_ia(String dht_ia) {
        this.dht_ia = dht_ia;
    }

    public String getDht_ib() {
        return dht_ib;
    }

    public void setDht_ib(String dht_ib) {
        this.dht_ib = dht_ib;
    }

    public String getDht_ic() {
        return dht_ic;
    }

    public void setDht_ic(String dht_ic) {
        this.dht_ic = dht_ic;
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
            data_cadastro,
            velocidade, torque);
    }

    private String toStringRms() {
        String formatRms = "\nRMS\n" +
            "Vab: %s \t-\t Vbc: %s \t-\t Vca: %s\n" +
            "Ia: %s \t-\t Ib: %s \t-\t Ic: %s\n";

        return opcaoRms == 1
            ? String.format(formatRms,
                rms_vab, rms_vbc, rms_vca,
                rms_ia, rms_ib, rms_ic)
            : "";
    }

    private String toStringDht() {
        String formatDht = "\nDHT\n" +
            "Vab: %s \t-\t Vbc: %s \t-\t Vca: %s\n" +
            "Ia: %s \t-\t Ib: %s \t-\t Ic: %s\n";

        return opcaoDht == 1
            ? String.format(formatDht,
                dht_vab, dht_vbc, dht_vca,
                dht_ia, dht_ib, dht_ic)
            : "";
    }
}
