package br.uff.antoniocanhota.dengueapp.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Toast;

public class DenunciasActivity extends MapActivity{
	
	MapController mapa; 
	GeoPoint centro;
	String txt;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Hashtable<Integer,Denuncia> hash_de_denuncias = processarXMLDenuncias();
		Enumeration enum_denuncias = hash_de_denuncias.keys();
		//trecho abaixo apenas para testar se consigo ler o xml
		while(enum_denuncias.hasMoreElements()){
			Object obj = enum_denuncias.nextElement();
			Denuncia denuncia = hash_de_denuncias.get(obj);
			String lat = String.valueOf(denuncia.getLatitude());
			String lng = String.valueOf(denuncia.getLongitude());
			Toast.makeText(getBaseContext(),"Denuncia: "+lat+","+lng, Toast.LENGTH_SHORT).show();	
		}
		setContentView(R.layout.activity_denuncias);
		
		MapView mapView = (MapView) findViewById(R.id.mapa_denuncias);
	    mapView.setBuiltInZoomControls(true);
	    mapView.setSatellite(true);
		
	    mapa = mapView.getController();
	    double latitude = Double.parseDouble("-22.890209");
	    double longitude = Double.parseDouble("-43.296562");
	    centro = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
	    mapa.animateTo(centro); 
	    mapa.setZoom(11); 
	    mapView.invalidate();
	   
//		MapOverlay mapOverlay = new MapOverlay(); 
//		List<Overlay> listOfOverlays = mapView.getOverlays(); 
//		listOfOverlays.clear(); 
//		listOfOverlays.add(mapOverlay);
		
	}

	class MapOverlay extends com.google.android.maps.Overlay{
		
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when){
			super.draw(canvas, mapView, shadow);
			
			Point screenPts = new Point();
			GeoPoint p = null;
			mapView.getProjection().toPixels(p , screenPts);
			
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_point);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
			
			return true;
		}
		
	}
	
	private Hashtable<Integer,Denuncia> processarXMLDenuncias(){
		InputStream in = null;
		Hashtable<Integer,Denuncia> hash_de_denuncias = new Hashtable<Integer,Denuncia>();
		int id = 0;
		
		try {
			in = Utilitarios.OpenHttpConnection("http://10.0.2.2:3000/webservices/denuncias");
			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				doc = db.parse(in);
			} catch(ParserConfigurationException e){
				e.printStackTrace();
			} catch(Exception e){
				e.printStackTrace();
			}
			doc.getDocumentElement().normalize();
			NodeList denuncias = doc.getElementsByTagName("denuncia");
			for (int i = 0; i < denuncias.getLength(); i++){
				Node denuncia_bruta = denuncias.item(i);
				if (denuncia_bruta.getNodeType() == Node.ELEMENT_NODE){
					Element denuncia = (Element) denuncia_bruta;
					Denuncia d = new Denuncia();
					d.setLatitude(Double.parseDouble(Utilitarios.processarCampoXML(denuncia,"latitude")));
					d.setLongitude(Double.parseDouble(Utilitarios.processarCampoXML(denuncia,"longitude")));
					id = Integer.parseInt(Utilitarios.processarCampoXML(denuncia,"id"));
					//<data-e-hora type="datetime">2012-06-05T10:24:21-03:00</data-e-hora>
					hash_de_denuncias.put(id,d);
				}
				//Toast.makeText(getBaseContext(),latitude, Toast.LENGTH_SHORT).show();
			}
			
		}catch (IOException e){
			
		}
		return hash_de_denuncias;
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
