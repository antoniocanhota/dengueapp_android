package br.uff.antoniocanhota.dengueapp.android;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PublicarDenunciaActivity extends Activity implements OnClickListener {
	private static final int TIRAR_FOTO = 1020394857;
	private ImageView imgFoto;
	private Button bt_tirar_foto;
	
	String webservice_de_publicar_denuncia = "http://10.0.2.2:3000/webservices/denuncias/publicar";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_publicar_denuncia);
		imgFoto = (ImageView) findViewById(R.id.foto_da_denuncia);
		Button bt_confirmar_publicacao_de_denuncia = (Button) findViewById(R.id.bt_confirmar_publicacao_de_denuncia);
		bt_tirar_foto = (Button) findViewById(R.id.bt_tirar_foto);

		bt_tirar_foto.setOnClickListener(this);
		
		bt_confirmar_publicacao_de_denuncia
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						
						//Geração de coordenadas aleatórias no grande Rio
						double minLat = -22.00;
					    double maxLat = -22.20;      
					    double latitude = minLat + (double)(Math.random() * ((maxLat - minLat) + 1));
					    double minLon = -42.50;
					    double maxLon = -43.00;     
					    double longitude = minLon + (double)(Math.random() * ((maxLon - minLon) + 1));
					    String lat = String.valueOf(latitude);
						String lng = String.valueOf(longitude);
						
						PublicarDenunciaActivity.enviar(getApplicationContext(),lat,lng);
					}
				});
		
	}

    // Método é subscrito para conseguir obeter o resultado da Intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Aqui verifica se o resultado é da Requisição TIRAR_FOTO.
        if (requestCode == TIRAR_FOTO) {
            //Ok
            if (resultCode == RESULT_OK) {
                // Aqui pego a imagem
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                // Seta ela no ImaView do Layout
                imgFoto.setImageBitmap(bitmap);
                
                //Aqui posso salvar a foto se quizer.
            } else if (resultCode == RESULT_CANCELED) {//Cancelou a foto
                Toast.makeText(this, "Cancelou", Toast.LENGTH_SHORT);
            } else { //Saiu da Intent
                Toast.makeText(this, "Saiu", Toast.LENGTH_SHORT);
            }

        }
    }
	
	private static void enviar(Context ctx, String lat, String lng) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(ctx.getString(R.string.webservice_publicar_denuncia));
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();		
		
		NameValuePair nv1 = new BasicNameValuePair("denuncia[latitude]", lat);
		nameValuePairs.add(nv1);
		
		NameValuePair nv2 = new BasicNameValuePair("denuncia[longitude]", lng);
		nameValuePairs.add(nv2);		
		
		NameValuePair nv3 = new BasicNameValuePair("dispositivo[identificador_do_hardware]", Utilitarios.getDeviceID(ctx));
		nameValuePairs.add(nv3);
		
		NameValuePair nv4 = new BasicNameValuePair("dispositivo[identificador_do_android]", Utilitarios.getAndroidID(ctx));
		nameValuePairs.add(nv4);

		NameValuePair nv5 = new BasicNameValuePair("dispositivo[numero_do_telefone]", Utilitarios.getPhoneNumber(ctx));
		nameValuePairs.add(nv5);
		
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);
			Toast.makeText(ctx,"Denúncia Publicada",Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_publicar_denuncia, menu);
		return true;
	}

	public void onClick(View view) {
		// Verifico se o botão clicado foi o btnIniciar
        if (view == bt_tirar_foto) {
            // Crio a Intent para Camera do Android
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Inicio a Intent como uma Intent que irá retornar algo, nesse caso
            // a Camera, e passo um código para requisição.
            startActivityForResult(intent, TIRAR_FOTO);
        }
		
	}

}
