package br.uff.antoniocanhota.dengueapp.android;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
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
		endereco = "O aplicativo está tentando determinar sua localização...\n\n" +
				"(Procure um local a céu aberto ou próximo a uma janela). ";
		bt_confirmar_publicacao_de_denuncia = (Button) findViewById(R.id.bt_confirmar_publicacao_de_denuncia);
	}

	@Override
	public void onResume() {
		super.onResume();

		startLocationServices();

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
					getEndereco();
//					endereco = "Localização obtida com sucesso!\n\n"
//							+ "Latitude: " + loc.getLatitude() + "\n"
//							+ "Longitude:" + loc.getLongitude();
					enderecoTextView.setText(endereco);
				}
			}
			
		}

		private void getEndereco() {
			
			Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
			try {
				List<Address> enderecos = geoCoder.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), 1);
				if (enderecos.size() > 0){
					for (int i=0; i<enderecos.get(0).getMaxAddressLineIndex();i++){
						endereco ="";
						endereco += "\n" + enderecos.get(0).getAddressLine(1) + "\n\n(Endereço aproximado. Erro estimado em um raio de "+userLocation.getAccuracy()+"m.)";
					}
				}
				
			} catch (IOException e) {
				endereco = "\nLocalização obtida. ("+userLocation.getLatitude()+","+userLocation.getLongitude()+")\n\n(Erro estimado em um raio de "+userLocation.getAccuracy()+"m.)";
			}
			
		}

		public void onProviderDisabled(String provider) {
			showSettingsAlert();
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

}
