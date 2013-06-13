package br.uff.antoniocanhota.dengueapp.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class DenunciasActivity extends MapActivity{
	
	//String webservice_de_listagem_de_denuncias = "http://dengue.herokuapp.com/webservices/denuncias.xml";	

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_denuncias);
		MapView mapView = (MapView) findViewById(R.id.mapa_denuncias);
		MapController mapa; 
		GeoPoint centro = new GeoPoint((int) (Double.parseDouble("-22.890209") * 1E6), (int) (Double.parseDouble("-43.296562") * 1E6));
		
		//Convers�o do XML do webservice em uma lista de den�ncias
		//Hashtable<Integer,Denuncia> hash_de_denuncias = Denuncia.processarXMLDenuncias(webservice_de_listagem_de_denuncias);
		//Enumeration enum_denuncias = hash_de_denuncias.keys();
		List<Denuncia> denuncias = new ArrayList<Denuncia>();
		try {
			denuncias = Webservice.getDenuncias();
		} catch (IOException e) {
			Utilitarios.showToastIOException(getApplicationContext());
		}

		//Cria��o da listagem de pontos das den�ncias		
		MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(this);
	    
		//Itera��o da lista de den�ncias		
		for (Denuncia denuncia : denuncias){
			//Cria��o do ponto no mapa			
			Double lat = Double.parseDouble(String.valueOf(denuncia.getLatitude()));
			Double lng = Double.parseDouble(String.valueOf(denuncia.getLongitude()));
  			GeoPoint ponto = new GeoPoint((int)(lat * 1E6), (int)(lng * 1E6)); 
  			
 			//Adicionando o ponto � listagem de pontos 		      			
  			itemizedOverlay.addOverlayItem(ponto, denuncia);
  			mapView.getOverlays().add(itemizedOverlay);
		}
		
//		while( enum_denuncias.hasMoreElements()){		
//
//			//Transforma��o dos dados de uma den�ncia em XML em um objeto 'Denuncia'
//			Object obj = enum_denuncias.nextElement();
//			Denuncia denuncia = hash_de_denuncias.get(obj);
//			
//			//Cria��o do ponto no mapa			
//			Double lat = Double.parseDouble(String.valueOf(denuncia.getLatitude()));
//			Double lng = Double.parseDouble(String.valueOf(denuncia.getLongitude()));
//  			GeoPoint ponto = new GeoPoint((int)(lat * 1E6), (int)(lng * 1E6)); 
//  			
// 			//Adicionando o ponto � listagem de pontos 		      			
//  			itemizedOverlay.addOverlayItem(ponto, denuncia);
//  			mapView.getOverlays().add(itemizedOverlay);
//  			 		    
//		}
		
		//Configura��es diversas de exibi��o do mapa
	    mapView.setBuiltInZoomControls(true);
	    mapView.setSatellite(true);
	    mapa = mapView.getController();	   
	    mapa.animateTo(centro); 
	    mapa.setZoom(11); 	 	    
	    mapView.invalidate();

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	
}
