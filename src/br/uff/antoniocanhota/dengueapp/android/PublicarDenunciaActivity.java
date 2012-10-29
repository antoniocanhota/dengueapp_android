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
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PublicarDenunciaActivity extends Activity {

	String webservice_de_publicar_denuncia = "http://10.0.2.2:3000/webservices/denuncias/publicar";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publicar_denuncia);

		Button bt_confirmar_publicacao_de_denuncia = (Button) findViewById(R.id.bt_confirmar_publicacao_de_denuncia);

		bt_confirmar_publicacao_de_denuncia
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						//Coordenadas na Ilha do Governador!
						String lat = "-22.806393";
						String lng = "-43.211889";
						String denunciante_id = "1";
						PublicarDenunciaActivity.enviar(getApplicationContext(),lat,lng,denunciante_id);
					}
				});
		
	}

	private static void enviar(Context ctx, String lat, String lng, String denunciante_id) {
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

}
