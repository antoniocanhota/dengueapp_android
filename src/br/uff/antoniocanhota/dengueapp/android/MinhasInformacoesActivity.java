package br.uff.antoniocanhota.dengueapp.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MinhasInformacoesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Webservice webservice = new Webservice(getApplicationContext());
        
        setContentView(R.layout.activity_minhas_informacoes);        
        TextView cod_ativacao_campo = (TextView) findViewById(R.id.cod_ativacao_campo);        
        
			cod_ativacao_campo.setText(webservice.getCodigoDeAtivacao());
		               
    }

}
