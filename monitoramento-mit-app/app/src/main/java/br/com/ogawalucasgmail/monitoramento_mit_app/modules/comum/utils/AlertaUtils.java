package br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class AlertaUtils {

    public static ProgressDialog carregandoProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("CARREGANDO");
        progressDialog.setMessage("POR FAVOR, AGUARDE");

        return progressDialog;
    }

    public static void erroAlertDialog(Context contexto, String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        builder.setTitle("ERRO");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(mensagem);

        builder.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void confirmacaoAlertDialog(Context contexto, String mensagem, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        builder.setTitle("CONFIRMAÇÃO");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setMessage(mensagem);

        builder.setPositiveButton("SIM", listener);
        builder.setNegativeButton("NÃO", listener);

        AlertDialog alert = builder.create();
        alert.show();
    }
}
