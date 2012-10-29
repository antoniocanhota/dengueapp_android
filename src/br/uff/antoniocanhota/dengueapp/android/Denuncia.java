package br.uff.antoniocanhota.dengueapp.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Denuncia {

	private Double latitude;
	private Double longitude;
	
	public Denuncia(){
		
	}
	
	public Double getLatitude(){
		return latitude;
	}
	
	public void setLatitude(Double latitude){
		this.latitude = latitude;
	}
	
	public Double getLongitude(){
		return longitude;
	}
	
	public void setLongitude(Double longitude){
		this.longitude = longitude;
	}
	
	public static Hashtable<Integer,Denuncia> processarXMLDenuncias(String webservice){
		InputStream in = null;
		Hashtable<Integer,Denuncia> hash_de_denuncias = new Hashtable<Integer,Denuncia>();
		int id = 0;

		try {
			in = Utilitarios.OpenHttpConnection(webservice);
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
	
}
