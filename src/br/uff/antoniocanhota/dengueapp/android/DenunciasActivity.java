package br.uff.antoniocanhota.dengueapp.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

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
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

public class DenunciasActivity extends MapActivity{

	MapController mapa; 
	GeoPoint centro;
	String txt;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_denuncias);
		MapView mapView = (MapView) findViewById(R.id.mapa_denuncias);
		
		//Convers�o do XML do webservice em uma lista de den�ncias
		Hashtable<Integer,Denuncia> hash_de_denuncias = processarXMLDenuncias();
		Enumeration enum_denuncias = hash_de_denuncias.keys();
		
		//Cria��o da listagem de pontos das den�ncias
		List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.ic_marker);
	    DenunciasItemizedOverlay itemizedoverlay = new DenunciasItemizedOverlay(drawable, this);
	    
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
 		    OverlayItem overlayitem = new OverlayItem(ponto, "T�tulo do box", "Texto do box");	
 		    itemizedoverlay.addOverlay(overlayitem);
 		    
		}
		
		//Configura��es diversas de exibi��o do mapa
	    mapView.setBuiltInZoomControls(true);
	    mapView.setSatellite(true);
	    mapa = mapView.getController();
	    double latitude_central = Double.parseDouble("-22.890209");
	    double longitude_central = Double.parseDouble("-43.296562");
	    centro = new GeoPoint((int) (latitude_central * 1E6), (int) (longitude_central * 1E6));
	    mapa.animateTo(centro); 
	    mapa.setZoom(11); 
	    
	    //Carregamento dos pontos no mapa
	    mapOverlays.add(itemizedoverlay);
	    
	    mapView.invalidate();

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