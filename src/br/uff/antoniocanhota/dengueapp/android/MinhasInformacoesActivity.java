package br.uff.antoniocanhota.dengueapp.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MinhasInformacoesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_informacoes);
        TextView imei_campo = (TextView) findViewById(R.id.imei_campo);
        imei_campo.setText(Utilitarios.getDeviceID(getApplicationContext()));
        TextView telefone_campo = (TextView) findViewById(R.id.telefone_campo);
        telefone_campo.setText(Utilitarios.getPhoneNumber(getApplicationContext()));
        TextView cod_ativacao_campo = (TextView) findViewById(R.id.cod_ativacao_campo);
        cod_ativacao_campo.setText(Utilitarios.getCodigoAtivacao(getApplicationContext()));
    }

}
