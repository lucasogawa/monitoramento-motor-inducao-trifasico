package br.com.ogawalucasgmail.monitoramento_mit_app.modules.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import br.com.ogawalucasgmail.monitoramento_mit_app.R;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.enums.Modos;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp.OkHttpRequest;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp.OkHttpResponse;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.usuario.dto.UsuarioResponse;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.Alerta.CONFIRMAR_ALTERAR_SITUACAO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.ARQUIVO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.TOKEN;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.URL.URL_ALTERAR_SITUACAO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.URL.URL_USUARIO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.enums.HttpCode.OK;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils.carregandoProgressDialog;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils.erroAlertDialog;

public class UsuarioListagemActivity extends AppCompatActivity {

    private ListView listViewUsuarios;
    private ProgressDialog progressDialog;
    private String token;
    private UsuarioResponse[] usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_listagem);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle("USUÁRIOS");

        inicializarVariaveis();
        configurarClickListView();
    }

    private void inicializarVariaveis() {
        listViewUsuarios = findViewById(R.id.listViewUsuarios);
        progressDialog = carregandoProgressDialog(this);
    }

    private void configurarClickListView() {
        listViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String usuarioId = ((UsuarioResponse) parent.getItemAtPosition(position)).getId();
                mostrarCadastrarEditarUsuarioctivity(Modos.EDITAR, usuarioId);
            }
        });

        listViewUsuarios.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        mostrarConfirmacaoDeAlterarSituacao(usuarios[position]);
                        return true;
                    }
                });
    }

    private void mostrarConfirmacaoDeAlterarSituacao(final UsuarioResponse usuario) {
        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                processarAlterarSituacao(usuario);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

        AlertaUtils.confirmacaoAlertDialog(this,
                String.format(CONFIRMAR_ALTERAR_SITUACAO, usuario.obterTextoAlterarSituacao().toUpperCase(), usuario.getNome()),
                listener);
    }

    private void processarAlterarSituacao(UsuarioResponse usuario) {
        new AlterarSituacao().execute(usuario.getId(), usuario.obterTextoAlterarSituacao());
    }
    private class AlterarSituacao extends AsyncTask<String, Void, OkHttpResponse> {
        @Override
        protected void onPreExecute() {
            iniciarProgressDialog();
        }

        @Override
        protected OkHttpResponse doInBackground(String... strings) {
            return alterarSituacao(strings[0], strings[1]);
        }
        @Override
        protected void onPostExecute(OkHttpResponse response) {
            tratarResponseAlterarSituacao(response);
            finalizarProgressDialog();
        }
    }

    private OkHttpResponse alterarSituacao(String id, String alteracao) {
        try {
            return new OkHttpRequest().PUT(
                    String.format(URL_ALTERAR_SITUACAO, id, alteracao),
                    token,
                    new RequestBody() {
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(BufferedSink sink) throws IOException {
                        }
                    });
        } catch (InterruptedException ex) {
            System.out.println(ex);
            mostrarErroAlertDialog("ERRO AO ALTERAR SITUAÇÃO DO USUÁRIO");
            return null;
        }
    }

    private void tratarResponseAlterarSituacao(OkHttpResponse response) {
        if (response != null && response.getCode().equals(OK.getCodigo())) {
            popularUsuarios();
        } else {
            System.out.println(response != null ? response.getBody() : "");
            mostrarErroAlertDialog("ERRO AO ALTERAR SITUAÇÃO DO USUÁRIO");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.usuario_listagem_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                cancelar();
                return true;
            case R.id.menuItemNovo:
                mostrarCadastrarEditarUsuarioctivity(Modos.CADASTRAR, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void mostrarCadastrarEditarUsuarioctivity(Modos modos, String usuarioId) {
        UsuarioCadastrarEditarActivity.abrir(
            UsuarioListagemActivity.this,
            modos.getCodigo(),
            usuarioId);
    }

    @Override
    protected void onStart() {
        lerPreferenciasToken();
        popularUsuarios();
        super.onStart();
    }

    private void lerPreferenciasToken() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        token = shared.getString(TOKEN, null);
    }

    private void popularUsuarios() {
        new ObterUsuarios().execute();
    }

    private class ObterUsuarios extends AsyncTask<String, Void, OkHttpResponse> {
        @Override
        protected void onPreExecute() {
            iniciarProgressDialog();
        }
        @Override
        protected OkHttpResponse doInBackground(String... strings) {
            return obterUsuarios();
        }

        @Override
        protected void onPostExecute(OkHttpResponse response) {
            finalizarProgressDialog();
            tratarResponseObterUsuarios(response);
        }
    }

    private OkHttpResponse obterUsuarios() {
        try {
            return new OkHttpRequest().GET(URL_USUARIO, token);
        } catch (InterruptedException ex) {
            System.out.println(ex);
            mostrarErroAlertDialog("ERRO AO OBTER USUÁRIOS");
            return null;
        }
    }

    private void tratarResponseObterUsuarios(OkHttpResponse response) {
        if (response != null && response.getCode().equals(OK.getCodigo())) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                usuarios = mapper.readValue(response.getBody(), UsuarioResponse[].class);
                ArrayAdapter<UsuarioResponse> adapterMenus = new ArrayAdapter<>(UsuarioListagemActivity.this,
                        android.R.layout.simple_list_item_1,
                        usuarios);
                listViewUsuarios.setAdapter(adapterMenus);
            } catch (IOException ex) {
                System.out.println(ex);
                mostrarErroAlertDialog("ERRO AO OBTER USUÁRIOS");
            }
        } else {
            System.out.println(response != null ? response.getBody() : "");
            mostrarErroAlertDialog(response != null ? response.getBody() : "");
        }
    }

    public void iniciarProgressDialog() {
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void finalizarProgressDialog() {
        progressDialog.cancel();
    }

    private void mostrarErroAlertDialog(final String mensagem) {
        erroAlertDialog(UsuarioListagemActivity.this, mensagem);
    }
}
