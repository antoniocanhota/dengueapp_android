package br.uff.antoniocanhota.dengueapp.android;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MinhasInformacoesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_informacoes);        
        TextView cod_ativacao_campo = (TextView) findViewById(R.id.cod_ativacao_campo);        
        try {
			cod_ativacao_campo.setText(Webservice.getCodigoDeAtivacao());
		} catch (IOException e) {
			Utilitarios.showToastIOException(getApplicationContext());			 
		}               
    }

}
