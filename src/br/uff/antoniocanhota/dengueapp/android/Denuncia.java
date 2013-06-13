package br.uff.antoniocanhota.dengueapp.android;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;


public class Denuncia {

	private Integer id;
	private Double latitude;
	private Double longitude;
	private String url_da_foto;
	private String data_e_hora;
	private File foto;
	
	public Denuncia(){
		
	}
	
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public Double getLatitude(){
		return latitude;
	}
	
	public String getLatitudeStr(){
		if (latitude != null){
			return String.valueOf(latitude);
		} else {
			return null;
		}
			
	}
	
	public void setLatitude(Double latitude){
		this.latitude = latitude;
	}
	
	public Double getLongitude(){
		return longitude;
	}
	
	public String getLongitudeStr(){
		if (longitude != null){
			return String.valueOf(longitude);
		} else {
			return null;
		}
			
	}
	
	public void setLongitude(Double longitude){
		this.longitude = longitude;
	}
	
	public String getFotoUrl(){
		return url_da_foto;
	}
	
	public void setFotoUrl(String url_da_foto){
		this.url_da_foto = url_da_foto;
	}
	
	public String getdataEHora(){
		return data_e_hora;
	}
	
	public void setDataEHora(String data_e_hora){
		this.data_e_hora = data_e_hora;
	}
	
	public File getFoto(){
		return foto;
	}
	
	public void setFoto(File foto){
		this.foto = foto;
	}
	
	public void setFoto(Bitmap bitmap, Context ctx){
		this.foto = Utilitarios.getFileFromBitmap(bitmap, "foto_denuncia", ctx);
	}
	
//	public static Hashtable<Integer,Denuncia> processarXMLDenuncias(String webservice){
//		InputStream in = null;
//		Hashtable<Integer,Denuncia> hash_de_denuncias = new Hashtable<Integer,Denuncia>();
//		int id = 0;
//
//		try {
//			in = Utilitarios.OpenHttpConnection(webservice);
//			Document doc = null;
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			DocumentBuilder db;
//			try {
//				db = dbf.newDocumentBuilder();
//				doc = db.parse(in);
//			} catch(ParserConfigurationException e){
//				e.printStackTrace();
//			} catch(Exception e){
//				e.printStackTrace();
//			}
//			doc.getDocumentElement().normalize();
//			NodeList denuncias = doc.getElementsByTagName("denuncia");
//			for (int i = 0; i < denuncias.getLength(); i++){
//				Node denuncia_bruta = denuncias.item(i);
//				if (denuncia_bruta.getNodeType() == Node.ELEMENT_NODE){
//					Element denuncia = (Element) denuncia_bruta;
//					Denuncia d = new Denuncia();
//					id = Integer.parseInt(Utilitarios.processarCampoXML(denuncia,"id"));
//					d.setId(id);
//					d.setLatitude(Double.parseDouble(Utilitarios.processarCampoXML(denuncia,"latitude")));
//					d.setLongitude(Double.parseDouble(Utilitarios.processarCampoXML(denuncia,"longitude")));
//					d.setFotoUrl(Utilitarios.processarCampoXML(denuncia, "url_imagem"));
//					d.setDataEHora(Utilitarios.processarCampoXML(denuncia, "data-e-hora"));
//					hash_de_denuncias.put(id,d);
//				}
//				//Toast.makeText(getBaseContext(),latitude, Toast.LENGTH_SHORT).show();
//			}
//
//		}catch (IOException e){
//
//		}
//		return hash_de_denuncias;
//	}
	
}
