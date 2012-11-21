package br.uff.antoniocanhota.dengueapp.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.entity.*;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PublicarDenunciaActivity extends Activity {
	private static final int TIRAR_FOTO = 1020394857;
	private ImageView imgFoto;
	private Bitmap bitmap;

	String webservice_de_publicar_denuncia = "http://10.0.2.2:3000/webservices/denuncias/publicar";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publicar_denuncia);
		imgFoto = (ImageView) findViewById(R.id.foto_da_denuncia);

		Button bt_confirmar_publicacao_de_denuncia = (Button) findViewById(R.id.bt_confirmar_publicacao_de_denuncia);
		Button bt_tirar_foto = (Button) findViewById(R.id.bt_tirar_foto);

		bt_tirar_foto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, TIRAR_FOTO);
			}
		});

		bt_confirmar_publicacao_de_denuncia
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						// Geração de coordenadas aleatórias no grande Rio
						double minLat = -22.00;
						double maxLat = -22.20;
						double latitude = minLat
								+ (double) (Math.random() * ((maxLat - minLat) + 1));
						double minLon = -42.50;
						double maxLon = -43.00;
						double longitude = minLon
								+ (double) (Math.random() * ((maxLon - minLon) + 1));
						String lat = String.valueOf(latitude);
						String lng = String.valueOf(longitude);
						try {
							PublicarDenunciaActivity.enviar(
									getApplicationContext(), lat, lng, bitmap);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TIRAR_FOTO) {
			if (resultCode == RESULT_OK) {
				bitmap = (Bitmap) data.getExtras().get("data");
				imgFoto.setImageBitmap(bitmap);
				// A foto poderá ser salva aqui
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Cancelou", Toast.LENGTH_SHORT);
			} else {
				Toast.makeText(this, "Saiu", Toast.LENGTH_SHORT);
			}

		}
	}

	private static void enviar(Context ctx, String lat, String lng,
			Bitmap imagem) throws IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				ctx.getString(R.string.webservice_publicar_denuncia));
		MultipartEntity mpEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		
		// Object filename;
		// create a file to write bitmap data
		File f = new File(ctx.getCacheDir(), "foto_denuncia.jpg");
		f.createNewFile();

		// Convert bitmap to byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		imagem.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
		byte[] bitmapdata = bos.toByteArray();

		// write the bytes in file
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(bitmapdata);
		fos.flush();
		fos.close();

		// adiciona os parametros à requisição POST
		mpEntity.addPart("denuncia[foto]", new FileBody(f, "image/jpeg"));
		mpEntity.addPart("denuncia[latitude]",
				new StringBody(lat, Charset.forName("UTF-8")));
		mpEntity.addPart("denuncia[longitude]",
				new StringBody(lng, Charset.forName("UTF-8")));
		mpEntity.addPart(
				"dispositivo[identificador_do_hardware]",
				new StringBody(Utilitarios.getDeviceID(ctx), Charset
						.forName("UTF-8")));
		mpEntity.addPart(
				"dispositivo[identificador_do_android]",
				new StringBody(Utilitarios.getAndroidID(ctx), Charset
						.forName("UTF-8")));
		mpEntity.addPart("dispositivo[numero_do_telefone]", new StringBody(
				Utilitarios.getPhoneNumber(ctx), Charset.forName("UTF-8")));

		try {
			post.setEntity(mpEntity);
			HttpResponse response = client.execute(post);
			Toast.makeText(ctx, "Denúncia Publicada", Toast.LENGTH_SHORT)
					.show();
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
