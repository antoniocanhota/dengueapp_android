package br.uff.antoniocanhota.dengueapp.android;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.os.Bundle;

public class DenunciasActivity extends MapActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_denuncias);
		
		MapView mapView = (MapView) findViewById(R.id.mapa_denuncias);
	    mapView.setBuiltInZoomControls(true);
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
