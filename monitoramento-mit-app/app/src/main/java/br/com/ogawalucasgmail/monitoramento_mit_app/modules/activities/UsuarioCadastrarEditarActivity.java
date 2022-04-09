package br.com.ogawalucasgmail.monitoramento_mit_app.modules.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import br.com.ogawalucasgmail.monitoramento_mit_app.R;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp.OkHttpRequest;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp.OkHttpResponse;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.usuario.dto.UsuarioRequest;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.usuario.dto.UsuarioResponse;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.usuario.enums.EPerfilUsuario;

import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.ARQUIVO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.TOKEN;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.URL.URL_USUARIO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.enums.HttpCode.OK;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.enums.Modos.EDITAR;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils.carregandoProgressDialog;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils.erroAlertDialog;

public class UsuarioCadastrarEditarActivity extends AppCompatActivity {

    private static final String MODO = "MODO";
    private static final String ID = "ID";

    private EditText etNome;
    private EditText etCpf;
    private EditText etUsuario;
    private EditText etSenha;
    private Spinner spinnerPerfil;
    private ProgressDialog progressDialog;

    private int modo;
    private String id;
    private String token;
    private List<String> perfis;
    private String nome;
    private String cpf;
    private String perfil;
    private String usuario;
    private String senha;

    public static void abrir(Activity activity, int requestCode, String usuarioId){
        Intent intent = new Intent(activity, UsuarioCadastrarEditarActivity.class);

        intent.putExtra(MODO, requestCode);
        intent.putExtra(ID, usuarioId);

        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_cadastrar_editar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle("USUÁRIOS");

        tratarIntent();

        inicializarVariaveis();
        popularPerfil();
    }

    private void tratarIntent() {
        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        modo = bundle.getInt(MODO);
        id = bundle.getString(ID);
    }

    private void inicializarVariaveis() {
        etNome = findViewById(R.id.editTextNome);
        etCpf = findViewById(R.id.editTextCpf);
        etUsuario = findViewById(R.id.editTextUsuario);
        etSenha = findViewById(R.id.editTextSenha);
        progressDialog = carregandoProgressDialog(this);
        spinnerPerfil = findViewById(R.id.spinnerPerfil);
        perfis = EPerfilUsuario.perfilsString();
    }

    private void popularPerfil() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UsuarioCadastrarEditarActivity.this,
                android.R.layout.simple_spinner_item, perfis);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPerfil.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                cancelar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onStart() {
        lerPreferenciasToken();
        if (EDITAR.getCodigo().equals(modo)) {
            new ObterUsuario().execute();
        }
        super.onStart();
    }

    private void lerPreferenciasToken() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        token = shared.getString(TOKEN, null);
    }

    private class ObterUsuario extends AsyncTask<String, Void, OkHttpResponse> {
        @Override
        protected void onPreExecute() {
            iniciarProgressDialog();
        }
        @Override
        protected OkHttpResponse doInBackground(String... strings) {
            return buscarUsuario();
        }

        @Override
        protected void onPostExecute(OkHttpResponse response) {
            finalizarProgressDialog();
            tratarResponseObterUsuario(response);
        }
    }

    private OkHttpResponse buscarUsuario() {
        try {
            return new OkHttpRequest().GET(URL_USUARIO + "/" + id, token);
        } catch (InterruptedException ex) {
            System.out.println(ex);
            mostrarErroAlertDialog("ERRO BUSCAR USUÁRIO");
            return null;
        }
    }

    private void tratarResponseObterUsuario(OkHttpResponse response) {
        if (response != null && response.getCode().equals(OK.getCodigo())) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                UsuarioResponse usuario = mapper.readValue(response.getBody(), UsuarioResponse.class);
                popularUsuario(usuario);
            } catch (IOException ex) {
                System.out.println(ex);
                mostrarErroAlertDialog("ERRO AO BUSCAR USUÁRIO");
            }
        } else {
            System.out.println(response != null ? response.getBody() : "");
            mostrarErroAlertDialog("ERRO AO BUSCAR USUÁRIO");
        }
    }

    private void popularUsuario(UsuarioResponse usuario) {
        etNome.setText(usuario.getNome());
        etCpf.setText(usuario.getCpf());
        spinnerPerfil.setSelection(obterPosicaoSpinnerPerfil(usuario.getPerfil()));
        etUsuario.setText(usuario.getUsuario());
    }

    private int obterPosicaoSpinnerPerfil(EPerfilUsuario perfil) {
        return EPerfilUsuario.ADMINISTRADOR.equals(perfil)
            ? 0
            : 1;
    }

    public void limpar(View view){
        etNome.setText("");
        etCpf.setText("");
        etUsuario.setText("");
        etSenha.setText("");
    }

    public void confirmar(View view) {
        obterValoresEditText();

        if (isCampoVazio(nome)) {
            mostrarErroAlertDialog("PREENCHA O CAMPO NOME");
            return;
        }
        if (isCampoVazio(cpf)) {
            mostrarErroAlertDialog("PREENCHA O CAMPO CPF");
            return;
        }
        if (isCampoVazio(usuario)) {
            mostrarErroAlertDialog("PREENCHA O CAMPO USUÁRIO");
            return;
        }
        if (isCampoVazio(senha)) {
            mostrarErroAlertDialog("PREENCHA O CAMPO SENHA");
            return;
        }

        new EditarSalvarUsuario().execute();
    }

    private void obterValoresEditText() {
        this.nome = etNome.getText().toString().toUpperCase();
        this.cpf = etCpf.getText().toString();
        this.perfil = obterPerfilSpinner(spinnerPerfil.getSelectedItemPosition());
        this.usuario = etUsuario.getText().toString().toUpperCase();
        this.senha = etSenha.getText().toString();
    }

    private boolean isCampoVazio(String campo) {
        return campo == null || campo.equals("");
    }

    private String obterPerfilSpinner(int posicao) {
        return 0 == posicao
                ? EPerfilUsuario.ADMINISTRADOR.name()
                : EPerfilUsuario.USUARIO.name();
    }

    private class EditarSalvarUsuario extends AsyncTask<String, Void, OkHttpResponse> {
        @Override
        protected void onPreExecute() {
            iniciarProgressDialog();
        }
        @Override
        protected OkHttpResponse doInBackground(String... strings) {
            return EDITAR.getCodigo().equals(modo) ? editar() : cadastrar();
        }

        @Override
        protected void onPostExecute(OkHttpResponse response) {
            tratarResponseSalvarEditar(response);
            finalizarProgressDialog();
        }
    }

    private OkHttpResponse cadastrar() {
        try {
            return new OkHttpRequest().POST(URL_USUARIO, token, new UsuarioRequest(null, nome, cpf, perfil, usuario, senha).toRequestBody());
        } catch (InterruptedException ex) {
            System.out.println(ex);
            mostrarErroAlertDialog("ERRO AO CADASTAR USUÁRIO");
            return null;
        }
    }

    private OkHttpResponse editar() {
        try {
            return new OkHttpRequest().PUT(URL_USUARIO, token, new UsuarioRequest(id, nome, cpf, perfil, usuario, senha).toRequestBody());
        } catch (InterruptedException ex) {
            System.out.println(ex);
            mostrarErroAlertDialog("ERRO AO EDITAR USUÁRIO");
            return null;
        }
    }

    private void tratarResponseSalvarEditar(OkHttpResponse response) {
        if (!OK.getCodigo().equals(response.getCode())) {
            System.out.println(response.getBody());
            mostrarErroAlertDialog(response.getBody());
        } else {
            fecharActivity();
        }
    }

    private void fecharActivity() {
        setResult(Activity.RESULT_CANCELED);
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
        erroAlertDialog(UsuarioCadastrarEditarActivity.this, mensagem);
    }
}
