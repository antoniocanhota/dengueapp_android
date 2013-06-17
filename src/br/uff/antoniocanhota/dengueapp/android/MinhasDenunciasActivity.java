package br.uff.antoniocanhota.dengueapp.android;

import android.os.Bundle;
import br.uff.antoniocanhota.dengueapp.android.webservices.GetDenuncias;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MinhasDenunciasActivity extends MapActivity {

	private MapController mapa;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_denuncias);
		MapView mapView = (MapView) findViewById(R.id.mapa_denuncias);

		String androidId = Utilitarios.getAndroidID(getApplicationContext());
		GetDenuncias wsDenuncias = new GetDenuncias(this, mapView, androidId);
		wsDenuncias.execute(null);

		// Configurações diversas de exibição do mapa
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		mapa = mapView.getController();
		mapa.setCenter(Utilitarios.getGrandeRioGeoPoint());
		mapa.setZoom(11);
		mapView.invalidate();

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
