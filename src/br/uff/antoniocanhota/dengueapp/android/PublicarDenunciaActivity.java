package br.uff.antoniocanhota.dengueapp.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PublicarDenunciaActivity extends MapActivity {
	private static final int TIRAR_FOTO = 1020394857;
	private ImageView imgFoto;
	private Bitmap bitmap;

	private LocationManager lm;
	private LocationListener locationListener;
	private MapController mapa;
	private String lat;
	private String lng;
	private GeoPoint localDaDenuncia;
	private ProgressBar progressBar;

	static String webservice_de_publicar_denuncia = "http://dengue.herokuapp.com/webservices/denuncias/publicar";
	//static String webservice_de_publicar_denuncia = "http://10.0.2.2:3000/webservices/denuncias/publicar";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		
		// ---use the LocationManager class to obtain locations data---
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				locationListener);
		
		setContentView(R.layout.activity_publicar_denuncia);
		imgFoto = (ImageView) findViewById(R.id.foto_da_denuncia);

		// Centraliza e foca no local do usuáiro
//		MapView mapView = (MapView) findViewById(R.id.mapa_publicar_denuncia);
//		mapa = mapView.getController();
//
//		// TODO: REMOVER ESTE BACALHAU DAQUI DEPOIS
//		if (lat != null && lng != null) {
//			double lat_db = Double.parseDouble(lat);
//			double lng_db = Double.parseDouble(lng);
//			localDaDenuncia = new GeoPoint((int) (lat_db * 1E6),
//					(int) (lng_db * 1E6));
//
//			mapa.animateTo(localDaDenuncia);
//			mapa.setZoom(15);
//		}

		Button bt_confirmar_publicacao_de_denuncia = (Button) findViewById(R.id.bt_confirmar_publicacao_de_denuncia);
		ImageView bt_tirar_foto = (ImageView) findViewById(R.id.foto_da_denuncia);

		bt_tirar_foto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, TIRAR_FOTO);
			}
		});

		bt_confirmar_publicacao_de_denuncia
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						try {
							Boolean ret = PublicarDenunciaActivity.enviar(
									getApplicationContext(), lat, lng, bitmap);
							// startActivity(new
							// Intent("android.intent.action.MAIN"));
							if (ret == true) {
								Toast.makeText(getApplicationContext(),
										"Denúncia enviada com sucesso.",
										Toast.LENGTH_SHORT).show();
								finish();
								
							} else {
								Toast.makeText(
										getApplicationContext(),
										"Erro ao enviar a denúcia. Tente novamente em breve",
										Toast.LENGTH_SHORT).show();
							}
							lm.removeUpdates(locationListener);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}
	
	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
				try {
					List<Address> addresses = geoCoder.getFromLocation(
							loc.getLatitude(),
							loc.getLongitude(),
							1);
					String add = "";
					if (addresses.size() > 0){						
						for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); i++)
							add += addresses.get(0).getAddressLine(i) + "\n";
					}
					TextView addressView = (TextView) findViewById(R.id.endereco_aproximado_da_denuncia);
					if (add == "") {
						addressView.setText("Latitude: "+loc.getLatitude()+"\n"+"Longitude:"+loc.getLongitude());
					}else{
						addressView.setText(add);
					}							
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			localDaDenuncia = new GeoPoint((int) (loc.getLatitude()),
					(int) (loc.getLongitude()));
			// p = new GeoPoint((int) (loc.getLatitude() * 1E6),(int)
			// (loc.getLongitude() * 1E6));
			// mc.animateTo(p);
			// mc.setZoom(18);

			lat = String.valueOf((loc.getLatitude()));
			lng = String.valueOf((loc.getLongitude()));
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

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

	private static boolean enviar(Context ctx, String lat, String lng,
			Bitmap imagem) throws IOException {

		Boolean resultado = false;
		if (imagem == null) {
			Toast.makeText(ctx,
					"Você precisa tirar uma foto do local antes de publicar!",
					Toast.LENGTH_SHORT).show();
		} else {
			if (lat == null || lng == null) {
				Toast.makeText(
						ctx,
						"Ainda não conseguimos obter sua localização. Aguarde alguns segundos e tente novamente.",
						Toast.LENGTH_SHORT).show();
			} else {

				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(
						webservice_de_publicar_denuncia);
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
				mpEntity.addPart("denuncia[foto]",
						new FileBody(f, "image/jpeg"));
				mpEntity.addPart("denuncia[latitude]", new StringBody(lat,
						Charset.forName("UTF-8")));
				mpEntity.addPart("denuncia[longitude]", new StringBody(lng,
						Charset.forName("UTF-8")));
				mpEntity.addPart(
						"dispositivo[identificador_do_hardware]",
						new StringBody(Utilitarios.getDeviceIDCoded(ctx), Charset
								.forName("UTF-8")));
				mpEntity.addPart(
						"dispositivo[identificador_do_android]",
						new StringBody(Utilitarios.getAndroidID(ctx), Charset
								.forName("UTF-8")));
				// a condicional a seguir é feita para evitar erro ao usar tablets, que não possuem
				// a informação do número do telefone
				String numero_do_telefone = Utilitarios.getPhoneNumberCoded(ctx);
				if (numero_do_telefone != null) {
					mpEntity.addPart("dispositivo[numero_do_telefone]",
							new StringBody(Utilitarios.getPhoneNumberCoded(ctx),
									Charset.forName("UTF-8")));
				}
				;

				try {
					post.setEntity(mpEntity);
					HttpResponse response = client.execute(post);
					resultado = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					resultado = false;
					e.printStackTrace();
				}
			}
		}
		return resultado;

	}

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub return false;
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_publicar_denuncia, menu);
		return true;
	}

}
