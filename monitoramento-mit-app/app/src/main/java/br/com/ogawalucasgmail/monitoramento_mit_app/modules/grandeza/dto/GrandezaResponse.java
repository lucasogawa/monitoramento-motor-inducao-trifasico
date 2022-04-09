package br.com.ogawalucasgmail.monitoramento_mit_app.modules.grandeza.dto;

public class GrandezaResponse {

    private String vab;
    private String vbc;
    private String vca;
    private String ia;
    private String ib;
    private String ic;

    public GrandezaResponse() {
    }

    public GrandezaResponse(String vab, String vbc, String vca, String ia, String ib, String ic) {
        this.vab = vab;
        this.vbc = vbc;
        this.vca = vca;
        this.ia = ia;
        this.ib = ib;
        this.ic = ic;
    }

    public String getVab() {
        return vab;
    }

    public void setVab(String vab) {
        this.vab = vab;
    }

    public String getVbc() {
        return vbc;
    }

    public void setVbc(String vbc) {
        this.vbc = vbc;
    }

    public String getVca() {
        return vca;
    }

    public void setVca(String vca) {
        this.vca = vca;
    }

    public String getIa() {
        return ia;
    }

    public void setIa(String ia) {
        this.ia = ia;
    }

    public String getIb() {
        return ib;
    }

    public void setIb(String ib) {
        this.ib = ib;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }
}
