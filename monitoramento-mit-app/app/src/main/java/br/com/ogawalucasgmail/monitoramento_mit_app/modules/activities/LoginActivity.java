package br.com.ogawalucasgmail.monitoramento_mit_app.modules.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import br.com.ogawalucasgmail.monitoramento_mit_app.R;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.auth.dto.AuthResponse;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.auth.model.Auth;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp.OkHttpRequest;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp.OkHttpResponse;

import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.ARQUIVO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.PERFIL;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.SENHA;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.TOKEN;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.USUARIO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.URL.URL_AUTH;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.enums.HttpCode.OK;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils.carregandoProgressDialog;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils.erroAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private EditText eTUsuario;
    private EditText eTSenha;
    private String usuario;
    private String senha;
    private ProgressDialog progressDialog;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("LOGIN");

        inicializarVariaveis();
    }

    private void inicializarVariaveis() {
        eTUsuario = findViewById(R.id.editTextUsuario);
        eTSenha = findViewById(R.id.editTextSenha);
        progressDialog = carregandoProgressDialog(this);
    }

    private void lerPreferenciasToken() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        token = shared.getString(TOKEN, null);
    }

    @Override
    protected void onStart() {
        lerPreferenciasToken();
        processarVerificacaoToken();
        super.onStart();
    }

    private void processarVerificacaoToken() {
        lerPreferenciasUsuarioESenha();

        if (token != null) {
            new VerificarToken().execute();
        }
    }

    private void lerPreferenciasUsuarioESenha() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        eTUsuario.setText(shared.getString(USUARIO, null));
        eTSenha.setText(shared.getString(SENHA, null));
    }

    private class VerificarToken extends AsyncTask<String, Void, OkHttpResponse> {
        @Override
        protected void onPreExecute() {
            iniciarProgressDialog();
        }
        @Override
        protected OkHttpResponse doInBackground(String... strings) {
            return verificarToken();
        }

        @Override
        protected void onPostExecute(OkHttpResponse response) {
            finalizarProgressDialog();
            if (response.getCode() == 200) {
                abrirMonitoramento();
            } else {
                mostrarErroAlertDialog("FAVOR REALIZAR LOGIN");
            }
        }
    }

    private OkHttpResponse verificarToken() {
        try {
            return new OkHttpRequest().GET(URL_AUTH + "/verificar-token", token);
        } catch (InterruptedException ex) {
            System.out.println(ex);
            mostrarErroAlertDialog("ERRO AO VERIFICAR TOKEN");
            return null;
        }
    }

    private void abrirMonitoramento(){
        cancelar();
        Intent intent = new Intent(this, ParametroActivity.class);
        startActivity(intent);
    }

    public void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void limpar(View view) {
        eTUsuario.setText("");
        eTSenha.setText("");
    }

    public void confirmar(View view) {
        obterValoresEditText();

        if (isCampoVazio(usuario)) {
            mostrarErroAlertDialog("PREENCHA O CAMPO USUÁRIO");
            return;
        }
        if (isCampoVazio(senha)) {
            mostrarErroAlertDialog("PREENCHA O CAMPO SENHA");
            return;
        }

        new Autenticar().execute();
    }

    private void obterValoresEditText() {
        this.usuario = eTUsuario.getText().toString().toUpperCase();
        this.senha = eTSenha.getText().toString();
    }

    private boolean isCampoVazio(String campo) {
        return campo == null || campo.equals("");
    }

    private class Autenticar extends AsyncTask<String, Void, OkHttpResponse> {
        @Override
        protected void onPreExecute() {
            iniciarProgressDialog();
        }
        @Override
        protected OkHttpResponse doInBackground(String... strings) {
            return autenticar();
        }

        @Override
        protected void onPostExecute(OkHttpResponse response) {
            finalizarProgressDialog();
            tratarRespostaAutenticacao(response);
        }
    }

    private OkHttpResponse autenticar() {
        try {
            return new OkHttpRequest().POST(URL_AUTH, "", new Auth(usuario, senha).toRequestBody());
        } catch (InterruptedException ex) {
            System.out.println(ex);
            mostrarErroAlertDialog("ERRO AO FAZER LOGIN");
            return null;
        }
    }

    private void tratarRespostaAutenticacao(OkHttpResponse response) {
        if (response != null && response.getCode().equals(OK.getCodigo())) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                salvarPreferenciaTokenEPerfil(mapper.readValue(response.getBody(), AuthResponse.class));
                salvarPreferenciaUsuarioESenha();
                abrirMonitoramento();
            } catch (IOException ex) {
                System.out.println(ex);
                mostrarErroAlertDialog("ERRO AO FAZER LOGIN");
            }
        } else {
            System.out.println(response != null ? response.getBody() : "");
            mostrarErroAlertDialog(response != null ? response.getBody() : "");
        }
    }

    private void salvarPreferenciaTokenEPerfil(AuthResponse authResponse) {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putString(TOKEN, "Bearer " + authResponse.getToken());
        editor.putString(PERFIL, authResponse.getUsuario().getPerfil().name());

        editor.commit();

        finish();
    }

    private void salvarPreferenciaUsuarioESenha() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putString(USUARIO, usuario);
        editor.putString(SENHA, senha);

        editor.commit();

        finish();
    }

    public void iniciarProgressDialog() {
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void finalizarProgressDialog() {
        progressDialog.cancel();
    }

    private void mostrarErroAlertDialog(final String mensagem) {
        erroAlertDialog(LoginActivity.this, mensagem);
    }
}
