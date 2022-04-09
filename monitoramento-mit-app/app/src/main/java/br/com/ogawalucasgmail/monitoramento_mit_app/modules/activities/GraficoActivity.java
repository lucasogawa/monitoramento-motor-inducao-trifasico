package br.com.ogawalucasgmail.monitoramento_mit_app.modules.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

import br.com.ogawalucasgmail.monitoramento_mit_app.R;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.grafico.dto.GraficoResponse;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp.OkHttpRequest;
import br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp.OkHttpResponse;

import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.ARQUIVO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.SharedPreferences.TOKEN;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.URL.URL_IMAGENS;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.URL.URL_MONITORAMENTO;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.enums.HttpCode.OK;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils.carregandoProgressDialog;
import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils.AlertaUtils.erroAlertDialog;

public class GraficoActivity extends AppCompatActivity {

    private static String MOTOR_ID = "MOTOR_ID";

    private TextView tvMotor;
    private ImageView imageViewGrafico;
    private ProgressDialog progressDialog;
    private String token;
    private String motorId;

    public static void abrir(Activity activity, String motorId, int requestCode){
        Intent intent = new Intent(activity, GraficoActivity.class);
        intent.putExtra(MOTOR_ID, motorId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle("MONITORAMENTO");

        inicializarVariaveis();
    }

    private void inicializarVariaveis() {
        tvMotor = findViewById(R.id.textViewMotor);
        imageViewGrafico = findViewById(R.id.imageViewGrafico);
        progressDialog = carregandoProgressDialog(this);
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
        obterMotorId();
        new PopularGrafico().execute();
        super.onStart();
    }

    private void lerPreferenciasToken() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        token = shared.getString(TOKEN, null);
    }

    private void obterMotorId() {
        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        motorId = bundle.getString(MOTOR_ID);
    }

    private class PopularGrafico extends AsyncTask<String, Void, OkHttpResponse> {
        @Override
        protected void onPreExecute() {
            iniciarProgressDialog();
        }
        @Override
        protected OkHttpResponse doInBackground(String... strings) {
            return obterGrafico();
        }

        @Override
        protected void onPostExecute(OkHttpResponse response) {
            tratarResponsePopularGrafico(response);
            finalizarProgressDialog();
        }
    }

    private OkHttpResponse obterGrafico() {
        try {
            return new OkHttpRequest().GET(URL_MONITORAMENTO + "/graficos/" + motorId, token);
        } catch (InterruptedException ex) {
            System.out.println(ex);
            mostrarErroAlertDialog("ERRO AO OBTER GRÁFICO");
            return null;
        }
    }

    private void tratarResponsePopularGrafico(OkHttpResponse response) {
        if (response != null && response.getCode().equals(OK.getCodigo())) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                GraficoResponse grafico = mapper.readValue(response.getBody(), GraficoResponse.class);
                mostrarMotorEGrafico(grafico);
            } catch (IOException ex) {
                System.out.println(ex);
                mostrarErroAlertDialog("ERRO AO OBTER GRÁFICO");
            }
        } else {
            System.out.println(response != null ? response.getBody() : "");
            mostrarErroAlertDialog(response != null ? response.getBody() : "");
        }
    }

    private void mostrarMotorEGrafico(final GraficoResponse response) {
        tvMotor.setText(response.getMotor());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(URL_IMAGENS + "/" + response.getArquivo());
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    setarGrafico(bitmap);
                } catch (Exception ex) {
                    System.out.println(ex);
                    mostrarErroAlertDialog("ERRO AO OBTER GRÁFICO");
                }
            }
        });
    }

    private void setarGrafico(final Bitmap bitmap) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = (int) (displayMetrics.heightPixels * 0.6);
        final int width = (int) (displayMetrics.widthPixels * 0.95);

        GraficoActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageViewGrafico.setImageBitmap(Bitmap.createScaledBitmap(
                    bitmap,
                    width,
                    height,
                    false));
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
        GraficoActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            erroAlertDialog(GraficoActivity.this, mensagem);
            }
        });
    }
}
