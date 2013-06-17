package br.uff.antoniocanhota.dengueapp.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
	private static final int TIRAR_FOTO = 1020394857;
	private ImageView imgFoto;
	private Bitmap bitmap;

	private LocationManager lm;
	private LocationListener locationListener;
	// private MapController mapa;
	// private String lat;
	// private String lng;

	// private Double latitude;
	// private Double longitude;
	private Location userLocation;
	// private float userLocationAccuracy = MIN_ACCURACY;

	// private GeoPoint localDaDenuncia;
	// private ProgressBar progressBar;

	private Denuncia denuncia;

	@Override
	public void onResume() {
		super.onResume();

		userLocation = null;

		// progressBar = (ProgressBar) findViewById(R.id.progressBar);

		// ---use the LocationManager class to obtain locations data---
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// finish();

			// startActivity(new
			// Intent("br.uff.antoniocanhota.dengueapp.android.MAINACTIVITY"));
			showSettingsAlert();
			// finish();
		} else {

			locationListener = new MyLocationListener();

			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListener);
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListener);

			setContentView(R.layout.activity_publicar_denuncia);
			imgFoto = (ImageView) findViewById(R.id.foto_da_denuncia);

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
							Utilitarios.showToast("Enviando denúncia...",
									PublicarDenunciaActivity.this);
							buildDenuncia();
							if (validateDenuncia() && sendDenuncia()) {
								Toast.makeText(PublicarDenunciaActivity.this,
										"Denúncia enviada com sucesso.",
										Toast.LENGTH_SHORT).show();

							}
							lm.removeUpdates(locationListener);
							finish();
						}
					});
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private void buildDenuncia() {
		this.denuncia = new Denuncia();
		denuncia.setLatitude(userLocation.getLatitude());
		denuncia.setLongitude(userLocation.getLongitude());
		denuncia.setFoto(bitmap);
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
			Utilitarios.showToast("jjkjk", PublicarDenunciaActivity.this);
			if (loc != null) {

				if ((userLocation != null && loc.getAccuracy() <= userLocation
						.getAccuracy())
						|| (userLocation == null && loc.getAccuracy() <= MIN_ACCURACY)) {

					userLocation = loc;
					TextView addressView = (TextView) findViewById(R.id.endereco_aproximado_da_denuncia);
					addressView.setText("Localização obtida com sucesso!\n\n"
							+ "Latitude: " + loc.getLatitude() + "\n"
							+ "Longitude:" + loc.getLongitude());

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
			// Utilitarios.showToast("Por favor, reative o sensor GPS para o correto funcionamento do DengueApp. Pode ser necessário reiniciar a aplicação.",
			// PublicarDenunciaActivity.this);
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
				bitmap = (Bitmap) data.getExtras().get("data");
				imgFoto.setImageBitmap(bitmap);
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

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.delete);

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						PublicarDenunciaActivity.this.startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						finish();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

}
