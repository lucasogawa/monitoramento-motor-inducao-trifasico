package br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp;

public class OkHttpResponse {

    private Integer code;
    private String body;

    public OkHttpResponse() {
    }

    public OkHttpResponse(Integer code, String body) {
        this.code = code;
        this.body = body;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
