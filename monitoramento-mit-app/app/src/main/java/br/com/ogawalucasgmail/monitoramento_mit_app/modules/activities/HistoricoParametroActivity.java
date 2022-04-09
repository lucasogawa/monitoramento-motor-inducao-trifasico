package br.com.ogawalucasgmail.monitoramento_mit_app.modules.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.ogawalucasgmail.monitoramento_mit_app.R;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.motor.dto.MotorResponse;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp.OkHttpRequest;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp.OkHttpResponse;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.parametro.dto.ParametroHistoricoResponse;

import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.ARQUIVO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.OPCAO_DHT;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.OPCAO_RMS;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.TOKEN;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.URL.URL_MONITORAMENTO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.URL.URL_MOTOR;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.enums.HttpCode.OK;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils.carregandoProgressDialog;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils.erroAlertDialog;

public class HistoricoParametroActivity extends AppCompatActivity {

    private Spinner spinnerMotor;
    private EditText etData;
    private ListView listViewParametros;
    private ProgressDialog progressDialog;
    private Calendar calendarDia;
    private String token;
    private MotorResponse[] motores;
    private List<String> listaMotores;
    private String motor;
    private String data;
    private int opcaoRms;
    private int opcaoDht;
    private ParametroHistoricoResponse[] parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_parametro);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle("HISTÓRICO");

        inicializarVariaveis();
    }

    private void inicializarVariaveis() {
        spinnerMotor = findViewById(R.id.spinnerMotor);
        listViewParametros = findViewById(R.id.listViewParametros);
        etData = findViewById(R.id.editTextData);
        listViewParametros = findViewById(R.id.listViewParametros);
        progressDialog = carregandoProgressDialog(this);
        listaMotores = new ArrayList<>();

        calendarDia = Calendar.getInstance();
        etData.setFocusable(false);
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarDia.set(Calendar.YEAR, year);
                calendarDia.set(Calendar.MONTH, monthOfYear);
                calendarDia.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        etData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HistoricoParametroActivity.this, date1, calendarDia
                        .get(Calendar.YEAR), calendarDia.get(Calendar.MONTH),
                        calendarDia.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etData.setText(sdf.format(calendarDia.getTime()));
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
        getMenuInflater().inflate(R.menu.historico_parametro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                cancelar();
                return true;
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
                        item.setChecked(false);
                        salvarPreferenciaOpcaoDht(1);
                        break;
                    case 1:
                        item.setChecked(true);
                        salvarPreferenciaOpcaoDht(0);
                        break;
                }
                popularListView(criarAdapter());
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

    public void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onStart() {
        lerPreferenciaTokenEOpcoes();
        new ObterMotores().execute();
        super.onStart();
    }

    private void lerPreferenciaTokenEOpcoes() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        token = shared.getString(TOKEN, null);
        opcaoRms = Integer.valueOf(shared.getString(OPCAO_RMS, "1"));
        opcaoDht = Integer.valueOf(shared.getString(OPCAO_DHT, "1"));
    }

    private class ObterMotores extends AsyncTask<String, Void, OkHttpResponse> {
        @Override
        protected void onPreExecute() {
            iniciarProgressDialog();
        }
        @Override
        protected OkHttpResponse doInBackground(String... strings) {
            return obterMotores();
        }

        @Override
        protected void onPostExecute(OkHttpResponse response) {
            finalizarProgressDialog();
            tratarMotorResponse(response);
        }
    }

    private OkHttpResponse obterMotores() {
        try {
            return new OkHttpRequest().GET(URL_MOTOR, token);
        } catch (InterruptedException ex) {
            System.out.println(ex);
            mostrarErroAlertDialog("ERRO AO OBTER MOTORES");
            return null;
        }
    }

    private void tratarMotorResponse(OkHttpResponse response) {
        if (response != null && response.getCode().equals(OK.getCodigo())) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                motores = mapper.readValue(response.getBody(), MotorResponse[].class);
                popularMotores();
            } catch (IOException ex) {
                System.out.println(ex);
                mostrarErroAlertDialog("ERRO AO OBTER PARÂMETROS");
            }
        } else {
            System.out.println(response != null ? response.getBody() : "");
            mostrarErroAlertDialog(response != null ? response.getBody() : "");
        }
    }

    private void popularMotores() {
        for (MotorResponse motor : motores) {
            listaMotores.add(motor.getDescricao());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HistoricoParametroActivity.this,
                android.R.layout.simple_spinner_item, listaMotores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMotor.setAdapter(adapter);
    }

    public void limpar(View view) {
        spinnerMotor.setSelection(0);
        etData.setText("");
    }

    public void confirmar(View view) {
        obterValoresEditText();

        if (isCampoVazio(data)) {
            mostrarErroAlertDialog("PREENCHA O CAMPO DATA");
            return;
        }

        new ObterParametros().execute();
    }

    private void obterValoresEditText() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        motor = obterMotorId(spinnerMotor.getSelectedItemPosition());
        data = !etData.getText().toString().equals("")
            ? sdf.format(calendarDia.getTime())
            : "";
    }

    private boolean isCampoVazio(String campo) {
        return campo == null || campo.equals("");
    }

    private String obterMotorId(int posicao) {
        return motores[posicao].getId();
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
            tratarParametroResponse(response);
            finalizarProgressDialog();
        }
    }

    private OkHttpResponse obterParametros() {
        try {
            return new OkHttpRequest().GET(URL_MONITORAMENTO + "/parametro/" + motor + "/" + data, token);
        } catch (InterruptedException ex) {
            System.out.println(ex);
            mostrarErroAlertDialog("ERRO AO OBTER PARÂMETROS");
            return null;
        }
    }

    private void tratarParametroResponse(OkHttpResponse response) {
        if (response != null && response.getCode().equals(OK.getCodigo())) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                parametros = mapper.readValue(response.getBody(), ParametroHistoricoResponse[].class);
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

    private ArrayAdapter<ParametroHistoricoResponse> criarAdapter() {
        for (ParametroHistoricoResponse parametro : parametros) {
            parametro.setOpcaoRms(opcaoRms);
            parametro.setOpcaoDht(opcaoDht);
        }

        return new ArrayAdapter<>(HistoricoParametroActivity.this,
            android.R.layout.simple_list_item_1,
            parametros);
    }

    private void popularListView(final ArrayAdapter<ParametroHistoricoResponse> adapterParametros) {
        HistoricoParametroActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listViewParametros.setAdapter(adapterParametros);
            }
        });
    }

    public void iniciarProgressDialog() {
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void finalizarProgressDialog() {
        progressDialog.cancel();
    }

    private void mostrarErroAlertDialog(final String mensagem) {
        erroAlertDialog(HistoricoParametroActivity.this, mensagem);
    }
}
