package br.uff.antoniocanhota.dengueapp.android;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class DenunciasActivity extends MapActivity{

	List<Denuncia> arrayDeDenuncias = new ArrayList<Denuncia>();
	
	String webservice_de_listagem_de_denuncias = "http://dengue.herokuapp.com/webservices/denuncias.xml";	

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_denuncias);
		MapView mapView = (MapView) findViewById(R.id.mapa_denuncias);
		MapController mapa; 
		GeoPoint centro = new GeoPoint((int) (Double.parseDouble("-22.890209") * 1E6), (int) (Double.parseDouble("-43.296562") * 1E6));
		
		//Convers�o do XML do webservice em uma lista de den�ncias
		Hashtable<Integer,Denuncia> hash_de_denuncias = Denuncia.processarXMLDenuncias(webservice_de_listagem_de_denuncias);
		Enumeration enum_denuncias = hash_de_denuncias.keys();		

		//Cria��o da listagem de pontos das den�ncias		
		MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay();
	    
		//Itera��o da lista de den�ncias		
		while(enum_denuncias.hasMoreElements()){		

			//Transforma��o dos dados de uma den�ncia em XML em um objeto 'Denuncia'
			Object obj = enum_denuncias.nextElement();
			Denuncia denuncia = hash_de_denuncias.get(obj);
			
			//Cria��o do ponto no mapa			
			Double lat = Double.parseDouble(String.valueOf(denuncia.getLatitude()));
			Double lng = Double.parseDouble(String.valueOf(denuncia.getLongitude()));
  			GeoPoint ponto = new GeoPoint((int)(lat * 1E6), (int)(lng * 1E6)); 
  			
 			//Adicionando o ponto � listagem de pontos 		      			
  			itemizedOverlay.addOverlayItem(ponto, denuncia.getId());
  			mapView.getOverlays().add(itemizedOverlay);
  			arrayDeDenuncias.add(denuncia);
 		    
		}
		
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

	private class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
				
		private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		
		public MyItemizedOverlay() {
			super(boundCenterBottom(getResources().getDrawable(R.drawable.ic_marker)));
		}
		
		public void addOverlayItem(GeoPoint coordenada, int id_da_denuncia) {
			String titulo = "Denúncia #"+Integer.toString(id_da_denuncia);
			OverlayItem overlayItem = new OverlayItem(coordenada,titulo,null);
			overlayItem.setMarker(boundCenterBottom(getResources().getDrawable(R.drawable.ic_marker)));
			mOverlays.add(overlayItem);			
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {			
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}
		
		@Override
		protected boolean onTap(int index){
			Intent intent = new Intent("br.uff.antoniocanhota.dengueapp.android.SHOWDENUNCIAACTIVITY");
			Bundle bundle = new Bundle();
			Denuncia d = arrayDeDenuncias.get(index);
			bundle.putString("id",Integer.toString(d.getId()));
			bundle.putString("dataEHora", d.getdataEHora());
			bundle.putString("enderecoAproximado", "Não determinado");
			bundle.putString("urlDaImagem", d.getFotoUrl());
			bundle.putString("situacao", "Não determinado");
			intent.putExtras(bundle);
			startActivity(intent);
			return true;
		}
		
	}
	
}
