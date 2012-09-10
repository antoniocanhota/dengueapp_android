package br.uff.antoniocanhota.dengueapp.android;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.os.Bundle;

public class MinhasDenunciasActivity extends MapActivity {

	MapController mapa; 
	GeoPoint centro;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_minhas_denuncias);
		
		MapView mapView = (MapView) findViewById(R.id.mapa_minhas_denuncias);
	    mapView.setBuiltInZoomControls(true);
	    mapView.setSatellite(true);
	    
	    mapa = mapView.getController();
	    double latitude = Double.parseDouble("-22.890209");
	    double longitude = Double.parseDouble("-43.296562");
	    centro = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
	    mapa.animateTo(centro); 
	    mapa.setZoom(11); 
	    mapView.invalidate();
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
