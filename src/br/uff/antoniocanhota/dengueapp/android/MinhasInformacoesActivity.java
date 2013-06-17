package br.uff.antoniocanhota.dengueapp.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MinhasInformacoesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_informacoes);        
        TextView paragraph = (TextView) findViewById(R.id.paragraph);
        TextView labelEmphasis = (TextView) findViewById(R.id.labelEmphasis);
        
        GetRegistroDoDispositivoTask getRegistroDoDispositivoTask = new GetRegistroDoDispositivoTask(paragraph, labelEmphasis, MinhasInformacoesActivity.this);
        getRegistroDoDispositivoTask.execute(null);
//        Webservice webservice = new Webservice(getApplicationContext());
//        Dispositivo dispositivo = webservice.getRegistroDoDispositivo();
        
              
		               
    }

}
