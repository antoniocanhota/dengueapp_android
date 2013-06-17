package br.uff.antoniocanhota.dengueapp.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowDenunciaActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_denuncia);
        Bundle bundle = getIntent().getExtras();
        
        ImageView foto = (ImageView) findViewById(R.id.denuncia_foto_value);
        TextView id = (TextView) findViewById(R.id.denuncia_id_value); 
        TextView publicadaEm = (TextView) findViewById(R.id.denuncia_publicada_em_value);
        TextView enderecoAproximado = (TextView) findViewById(R.id.denuncia_endereco_aproximado_value);
        TextView situacao = (TextView) findViewById(R.id.denuncia_situacao_value);
        
        GetDenunciaTask getDenunciaTask = new GetDenunciaTask(bundle,id,publicadaEm,enderecoAproximado,situacao,foto,this);        
        getDenunciaTask.execute(null);               
        
    }

}
