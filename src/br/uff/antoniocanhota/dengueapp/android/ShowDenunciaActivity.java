package br.uff.antoniocanhota.dengueapp.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowDenunciaActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //1 Inicializando
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_denuncia);
        
        //2 Preenchimeto dos dados na tela
        Bundle bundle = getIntent().getExtras();
        //2.1 Obtendo a foto da denúncia
        try {
        	Bitmap imagem = BitmapFactory.decodeStream((InputStream)new URL(bundle.getString("urlDaImagem")).getContent());
        	ImageView foto = (ImageView) findViewById(R.id.denuncia_foto_value);
        	foto.setImageBitmap(imagem);
        } catch (MalformedURLException e){
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        //2.2 Preenchendo demais dados        
        TextView id = (TextView) findViewById(R.id.denuncia_id_value); 
        id.setText("Denúncia #"+bundle.getString("id"));
        TextView publicadaEm = (TextView) findViewById(R.id.denuncia_publicada_em_value);
        publicadaEm.setText(bundle.getString("dataEHora"));
        TextView enderecoAproximado = (TextView) findViewById(R.id.denuncia_endereco_aproximado_value);
        enderecoAproximado.setText(bundle.getString("enderecoAproximado"));
        TextView situacao = (TextView) findViewById(R.id.denuncia_situacao_value);
        situacao.setText(bundle.getString("situacao"));        
        
    }

}
