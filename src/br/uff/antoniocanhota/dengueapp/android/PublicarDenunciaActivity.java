package br.uff.antoniocanhota.dengueapp.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.MapActivity;

public class PublicarDenunciaActivity extends MapActivity {
	private static final float MIN_ACCURACY = 35; // in meters
	private static final int LOCATION_TIMEOUT = 60000;// in miliseconds
	private static final int TIRAR_FOTO = 1020394857;
	private ImageView fotoImageView;
	private Bitmap foto;
	private TextView enderecoTextView;
	private String endereco;
	private Button bt_confirmar_publicacao_de_denuncia;

	private LocationManager locationManager;
	private LocationListener locationListener;
	private Location userLocation;
	private Timer timer;
	
	private static PublicarDenunciaActivity atividade;
	// private MapController mapa;
	// private String lat;
	// private String lng;

	// private Double latitude;
	// private Double longitude;

	// private float userLocationAccuracy = MIN_ACCURACY;

	// private GeoPoint localDaDenuncia;
	// private ProgressBar progressBar;

	private Denuncia denuncia;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		atividade = this;
		userLocation = null;	
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();		
		
		setContentView(R.layout.activity_publicar_denuncia);
		fotoImageView = (ImageView) findViewById(R.id.foto_da_denuncia);
		enderecoTextView = (TextView) findViewById(R.id.endereco_aproximado_da_denuncia);
		bt_confirmar_publicacao_de_denuncia = (Button) findViewById(R.id.bt_confirmar_publicacao_de_denuncia);
	}

	@Override
	public void onResume() {
		super.onResume();

		startLocationServices();
		// progressBar = (ProgressBar) findViewById(R.id.progressBar);

		// ---use the LocationManager class to obtain locations data---
		

		
//		imgFoto = (ImageView) findViewById(R.id.foto_da_denuncia);
//		Button bt_confirmar_publicacao_de_denuncia = (Button) findViewById(R.id.bt_confirmar_publicacao_de_denuncia);

		// Centraliza e foca no local do usuГЎiro
		// MapView mapView = (MapView)
		// findViewById(R.id.mapa_publicar_denuncia);
		// mapa = mapView.getController();
		//
		// // TODO: REMOVER ESTE BACALHAU DAQUI DEPOIS
		// if (lat != null && lng != null) {
		// double lat_db = Double.parseDouble(lat);
		// double lng_db = Double.parseDouble(lng);
		// localDaDenuncia = new GeoPoint((int) (lat_db * 1E6),
		// (int) (lng_db * 1E6));
		//
		// mapa.animateTo(localDaDenuncia);
		// mapa.setZoom(15);
		// }

		
		//ImageView bt_tirar_foto = (ImageView) findViewById(R.id.foto_da_denuncia);

		fotoImageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, TIRAR_FOTO);
			}
		});
		if (foto != null){
			fotoImageView.setImageBitmap(foto);
		}
		enderecoTextView.setText(endereco);
		bt_confirmar_publicacao_de_denuncia
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {						
						buildDenuncia();
						if (validateDenuncia()) {
							stopLocationServices();
							PostDenunciaTask postDenunciaTask = new PostDenunciaTask(denuncia,PublicarDenunciaActivity.this, atividade);
							postDenunciaTask.execute(null);								
						}
					}
				});

	}
	
	private void resetLocationTimeout(){
		timer = new Timer();
		timer.schedule(new LocationTimeout(), LOCATION_TIMEOUT);
	}

	private void startLocationServices(){			
		try {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		} catch (Exception e) {
			// Esse request location foi colocado com try/catch devido a um bug
			// do simulador do Android 4.0+
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
		resetLocationTimeout();
	}
	
	private void stopLocationServices(){
		timer.cancel();
		locationManager.removeUpdates(locationListener);		
	}
	
	private void buildDenuncia() {
		this.denuncia = new Denuncia();
		denuncia.setLatitude(userLocation.getLatitude());
		denuncia.setLongitude(userLocation.getLongitude());
		denuncia.setFoto(foto);
	}

	private boolean validateDenuncia() {
		boolean result = true;
		if (denuncia.getFoto() == null) {
			result = false;
			Toast.makeText(PublicarDenunciaActivity.this,
					"Você precisa tirar uma foto do local antes de publicar!",
					Toast.LENGTH_SHORT).show();
		}
		if (denuncia.getLatitude() == null || denuncia.getLongitude() == null) {
			result = false;
			Toast.makeText(
					PublicarDenunciaActivity.this,
					"Ainda não foi possível obter sua localização. Aguarde alguns segundos e tente reenviar a denúncia.",
					Toast.LENGTH_SHORT).show();
		}
		return result;
	}

	private boolean sendDenuncia() {
		Webservice ws = new Webservice(PublicarDenunciaActivity.this);
		return ws.postDenuncia(denuncia);
	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			if (loc != null) {

				if ((userLocation != null && loc.getAccuracy() <= userLocation
						.getAccuracy())
						|| (userLocation == null && loc.getAccuracy() <= MIN_ACCURACY)) {

					userLocation = loc;
					//TextView addressView = (TextView) findViewById(R.id.endereco_aproximado_da_denuncia);
					endereco = "Localização obtida com sucesso!\n\n"
							+ "Latitude: " + loc.getLatitude() + "\n"
							+ "Longitude:" + loc.getLongitude();
					enderecoTextView.setText(endereco);
				}
			}

			// if (loc != null && loc.getAccuracy() <= MIN_ACCURACY
			// && loc.getAccuracy() <= userLocation.getAccuracy()) {

			// Geocoder geoCoder = new Geocoder(getBaseContext(),
			// Locale.getDefault());
			// TextView addressView = (TextView)
			// findViewById(R.id.endereco_aproximado_da_denuncia);
			// addressView.setText("LocalizaГ§ГЈo obtida com sucesso!\n\n"
			// + "Latitude: " + loc.getLatitude() + "\n"
			// + "Longitude:" + loc.getLongitude());

			// try {
			// List<Address> addresses = geoCoder.getFromLocation(
			// loc.getLatitude(),
			// loc.getLongitude(),
			// 1);
			// String add = "";
			// if (addresses.size() > 0){
			// for (int i=0; i<addresses.get(0).getMaxAddressLineIndex();
			// i++)
			// add += addresses.get(0).getAddressLine(i) + "\n";
			// }
			// if (add == "") {
			// addressView.setText("Latitude: "+loc.getLatitude()+"\n"+"Longitude:"+loc.getLongitude());
			// }else{
			// addressView.setText(add);
			// }
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// addressView.setText("Latitude: "+loc.getLatitude()+"\n"+"Longitude:"+loc.getLongitude());
			// e.printStackTrace();
			// }
			// }

			// localDaDenuncia = new GeoPoint((int) (loc.getLatitude()),
			// (int) (loc.getLongitude()));
			// p = new GeoPoint((int) (loc.getLatitude() * 1E6),(int)
			// (loc.getLongitude() * 1E6));
			// mc.animateTo(p);
			// mc.setZoom(18);

			// userLocation = loc;
			// latitude = loc.getLatitude();
			// longitude = loc.getLongitude();

			// lat = String.valueOf((loc.getLatitude()));
			// lng = String.valueOf((loc.getLongitude()));
		}

		public void onProviderDisabled(String provider) {
			Utilitarios
					.showToast(
							"Por favor, reative o sensor GPS para o correto funcionamento do DengueApp. Pode ser necessário reiniciar a aplicação.",
							PublicarDenunciaActivity.this);
		}

		public void onProviderEnabled(String provider) {//
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TIRAR_FOTO) {
			if (resultCode == RESULT_OK) {
				foto = (Bitmap) data.getExtras().get("data");
				fotoImageView.setImageBitmap(foto);
				// A foto poderГЎ ser salva aqui
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Cancelou", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Saiu", Toast.LENGTH_SHORT).show();
			}

		}
	}

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub return false;
		return false;
	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				PublicarDenunciaActivity.this);

		alertDialog.setTitle("GPS Desativado");
		alertDialog
				.setMessage("O GPS precisa estar ativo para publicar uma denúncia. Deseja alterar essa configuração?");

		alertDialog.setPositiveButton("Configurações",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						PublicarDenunciaActivity.this.startActivity(intent);
					}
				});
		alertDialog.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						finish();
					}
				});

		alertDialog.show();
	}

	public void showLocationTimeoutAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				PublicarDenunciaActivity.this);

		alertDialog.setTitle("Localização indeterminada");
		alertDialog
				.setMessage("Não foi possível obter sua localização. Deseja continuar tentando?");

		alertDialog.setPositiveButton("Sim",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						resetLocationTimeout();
					}
				});
		alertDialog.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						finish();
					}
				});

		alertDialog.show();
	}

	class LocationTimeout extends TimerTask {
		public void run() {			
			Looper.prepare();			
			if (userLocation == null){				
				showLocationTimeoutAlert();
			} else {
				stopLocationServices();
			}
			Looper.loop();			
		}
	}

	// private class TimeoutTimer extends TimerTask {
	// private Timer timer;
	//
	// public TimeoutTimer(){
	// timer = new Timer();
	// timer.schedule(new TimeoutTimer(), LOCATION_TIMEOUT);
	// }
	//
	// public void run() {
	// timer.cancel();
	// }
	//
	// public void stop(){
	// timer.cancel();
	// }
	// }
}
