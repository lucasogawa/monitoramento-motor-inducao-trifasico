package br.com.ogawalucasgmail.monitoramento_mit_app.modules.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import br.com.ogawalucasgmail.monitoramento_mit_app.R;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp.OkHttpRequest;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp.OkHttpResponse;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.parametro.dto.ParametroResponse;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.usuario.enums.EPerfilUsuario;

import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.ARQUIVO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.OPCAO_DHT;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.OPCAO_RMS;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.PERFIL;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.TOKEN;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.URL.URL_MONITORAMENTO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.enums.HttpCode.OK;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils.carregandoProgressDialog;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils.erroAlertDialog;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.usuario.enums.EPerfilUsuario.ADMINISTRADOR;

public class ParametroActivity extends AppCompatActivity {

    private ListView listViewParametros;
    private ProgressDialog progressDialog;
    private String token;
    private EPerfilUsuario perfil;
    private int opcaoRms;
    private int opcaoDht;
    private ParametroResponse[] parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametro);

        setTitle("MONITORAMENTO");

        inicializarVariaveis();
        configurarClickListView();
    }

    private void inicializarVariaveis() {
        listViewParametros = findViewById(R.id.listViewParametros);
        progressDialog = carregandoProgressDialog(this);
    }

    private void configurarClickListView() {
        listViewParametros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String motorId = ((ParametroResponse) parent.getItemAtPosition(position)).getId();
                GraficoActivity.abrir(ParametroActivity.this, motorId, 0);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch(opcaoRms){
            case 0:
                menu.findItem(R.id.menuItemRms).setChecked(false);
                break;
            case 1:
                menu.findItem(R.id.menuItemRms).setChecked(true);
                break;
        }
        switch(opcaoDht){
            case 0:
                menu.findItem(R.id.menuItemDht).setChecked(false);
                break;
            case 1:
                menu.findItem(R.id.menuItemDht).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.parametro_activity, menu);
        menu.findItem(R.id.menuItemUsuario).setVisible(ADMINISTRADOR.equals(perfil));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuItemRms:
                switch (opcaoRms){
                    case 0:
                        salvarPreferenciaOpcaoRms(1);
                        break;
                    case 1:
                        salvarPreferenciaOpcaoRms(0);
                        break;
                }
                popularListView(criarAdapter());
                return true;
            case R.id.menuItemDht:
                switch (opcaoDht){
                    case 0:
                        salvarPreferenciaOpcaoDht(1);
                        break;
                    case 1:
                        salvarPreferenciaOpcaoDht(0);
                        break;
                }
                popularListView(criarAdapter());
                return true;
            case R.id.menuItemUsuario:
                mostrarUsuarioListagemActivity();
                return true;
            case R.id.menuItemHistorico:
                mostrarHistoricoParametroActivity();
                return true;
            case R.id.menuItemLogout:
                logout();
                cancelar();
                mostrarLoginActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void salvarPreferenciaOpcaoRms(int novoValor) {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putString(OPCAO_RMS, Integer.toString(novoValor));
        editor.commit();

        opcaoRms = novoValor;
    }

    private void salvarPreferenciaOpcaoDht(int novoValor) {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putString(OPCAO_DHT, Integer.toString(novoValor));
        editor.commit();

        opcaoDht = novoValor;
    }

    private void mostrarUsuarioListagemActivity(){
        Intent intent = new Intent(this, UsuarioListagemActivity.class);
        startActivity(intent);
    }

    private void mostrarHistoricoParametroActivity(){
        Intent intent = new Intent(this, HistoricoParametroActivity.class);
        startActivity(intent);
    }

    private void logout() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putString(TOKEN, null);

        editor.commit();

        finish();
    }

    public void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void mostrarLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        lerPreferenciaTokenPerfilEOpcoes();
        new ObterParametros().execute();
        super.onStart();
    }

    private void lerPreferenciaTokenPerfilEOpcoes() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        token = shared.getString(TOKEN, null);
        perfil = EPerfilUsuario.valueOf(shared.getString(PERFIL, null));
        opcaoRms = Integer.valueOf(shared.getString(OPCAO_RMS, "1"));
        opcaoDht = Integer.valueOf(shared.getString(OPCAO_DHT, "1"));
    }

    private class ObterParametros extends AsyncTask<String, Void, OkHttpResponse> {
        @Override
        protected void onPreExecute() {
            iniciarProgressDialog();
        }
        @Override
        protected OkHttpResponse doInBackground(String... strings) {
            return obterParametros();
        }

        @Override
        protected void onPostExecute(OkHttpResponse response) {
            tratarResponse(response);
            finalizarProgressDialog();
            // TODO DESCOMENTAR aguardar5SegundosERequisitar();
        }
    }

    private OkHttpResponse obterParametros() {
        try {
            return new OkHttpRequest().GET(URL_MONITORAMENTO + "/parametro", token);
        } catch (InterruptedException ex) {
            System.out.println(ex);
            mostrarErroAlertDialog("ERRO AO OBTER PARÂMETROS");
            return null;
        }
    }

    private void tratarResponse(OkHttpResponse response) {
        if (response != null && response.getCode().equals(OK.getCodigo())) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                parametros = mapper.readValue(response.getBody(), ParametroResponse[].class);
                popularListView(criarAdapter());
            } catch (IOException ex) {
                System.out.println(ex);
                mostrarErroAlertDialog("ERRO AO OBTER PARÂMETROS");
            }
        } else {
            System.out.println(response != null ? response.getBody() : "");
            mostrarErroAlertDialog(response != null ? response.getBody() : "");
        }
    }

    private ArrayAdapter<ParametroResponse> criarAdapter() {
        for (ParametroResponse parametro : parametros) {
            parametro.setOpcaoRms(opcaoRms);
            parametro.setOpcaoDht(opcaoDht);
        }

        return new ArrayAdapter<>(ParametroActivity.this,
            android.R.layout.simple_list_item_1,
            parametros);
    }

    private void popularListView(final ArrayAdapter<ParametroResponse> adapterParametros) {
        ParametroActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listViewParametros.setAdapter(adapterParametros);
            }
        });
    }

    public void aguardar5SegundosERequisitar() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ParametroActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tratarResponse(obterParametros());
                    }
                });
            }
        }, 5000);
    }

    public void iniciarProgressDialog() {
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void finalizarProgressDialog() {
        progressDialog.cancel();
    }

    private void mostrarErroAlertDialog(final String mensagem) {
        erroAlertDialog(ParametroActivity.this, mensagem);
    }
}
